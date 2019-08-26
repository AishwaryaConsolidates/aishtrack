package com.aishtek.aishtrack.function;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.beans.ServiceReport;
import com.aishtek.aishtrack.beans.Technician;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.dao.CustomerDAO;
import com.aishtek.aishtrack.dao.PersonDAO;
import com.aishtek.aishtrack.dao.ServiceReportDAO;
import com.aishtek.aishtrack.dao.WorkOrderDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.services.EmailSenderService;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class CreateServiceReport extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  public static String technicianEmailSubject = "You have been assigned a service report";
  public static String customerEmailSubject = "Aishtek Service Report Assigned";

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        if (Util.isNullOrEmpty(response.id)) {
          Date reportDate = new SimpleDateFormat("dd/MM/yyyy").parse(response.reportDate);
          int serviceReportId = createServiceReport(connection, response.workOrderId,
              response.contactPersonId, response.categoryId, response.equipmentId, response.notes,
              response.brand, response.model, response.serialNumber, response.partNumber,
              reportDate, getIntegerList(response.technicianIds));
          output = createSuccessOutput("" + serviceReportId);
        } else {
          updateServiceReport(connection, Integer.parseInt(response.id), response.contactPersonId,
              response.categoryId, response.equipmentId, response.brand, response.model,
              response.serialNumber, response.partNumber, response.notes);
          output = createSuccessOutput("");
        }
        connection.commit();
      } catch (Exception e) {
        connection.rollback();
        output = createFailureOutput(e);
      }
    } catch (Exception e) {
      output = createFailureOutput(e);
    }

    return output;
  }

  public int createServiceReport(Connection connection, int workOrderId, int contactPersonId,
      int categoryId, int equipmentId, String notes, String brand, String model,
      String serialNumber, String partNumber, Date reportDate, ArrayList<Integer> technicianIds)
      throws SQLException {
    WorkOrder workOrder = WorkOrderDAO.findById(connection, workOrderId);
    Customer customer = CustomerDAO.findById(connection, workOrder.getCustomerId());

    // create service report
    int serviceReportId = ServiceReportDAO.create(connection, workOrder.getId(),
        new ServiceReport(customer, contactPersonId, categoryId, equipmentId, notes, brand, model,
            serialNumber, partNumber, reportDate));

    // create service report technician
    if (technicianIds != null && technicianIds.size() > 0) {
      ServiceReportDAO.assignTechniciansToServiceReport(connection, serviceReportId, technicianIds);

      // change status of work order
      WorkOrderDAO.markAsAssigned(connection, workOrder.getId());

      ServiceReport serviceReport = ServiceReportDAO.findById(connection, serviceReportId);
      // notify technicians
      sendEmailToTechnicians(connection, serviceReport);

      // notify customer
      sendEmailToCustomer(connection, serviceReport);
    }
    
    return serviceReportId;
  }

  public void updateServiceReport(Connection connection, int serviceReportId, int contactPersonId,
      int categoryId, int equipmentId, String brand, String model, String serialNumber,
      String partNumber, String notes)
      throws SQLException {
    ServiceReportDAO.update(connection, serviceReportId, contactPersonId, categoryId, equipmentId,
        brand, model, serialNumber, partNumber, notes);
  }

  private void sendEmailToTechnicians(Connection connection, ServiceReport serviceReport)
      throws SQLException {
    try {
      for (Technician technician : ServiceReportDAO.getTechniciansFor(connection,
          serviceReport.getId())) {
        String[] to = {technician.getPerson().getEmail()};
        String[] emailBodies = technicianEmailBodies(serviceReport.getCode());
        EmailSenderService.sendEmail(to, technicianEmailSubject, emailBodies[0], emailBodies[1]);
      }
    } catch (Exception e) {
      System.out.println(e);
      e.printStackTrace();
    }
  }

  private String[] technicianEmailBodies(String serviceReportCode) {
    String url =
        "https://aishtek.s3.amazonaws.com/aishtrack/serviceReports/viewServiceReports.html?code="
            + serviceReportCode;
    String[] emailBodies = new String[2];
    emailBodies[0] =
        "You have been assigned the following service report, please click on the link to start work on it. <a href=\""
            + url + "\">" + url + "</a>";
    emailBodies[1] = "You have been assigned the following service report " + url;
    return emailBodies;
  }

  private void sendEmailToCustomer(Connection connection, ServiceReport serviceReport)
      throws SQLException {
    try {
      Customer customer = CustomerDAO.findById(connection, serviceReport.getCustomerId());
      Person person = PersonDAO.findById(connection, customer.getContactPersonId());
      String[] to = {person.getEmail()};
      String[] emailBodies = customerEmailBodies(connection, serviceReport);
      EmailSenderService.sendEmail(to, customerEmailSubject, emailBodies[0], emailBodies[1]);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String[] customerEmailBodies(Connection connection, ServiceReport serviceReport)
      throws SQLException {
    String[] emailBodies = new String[2];
    emailBodies[0] = "The Following Technician(s) have been assigned to address your problem ";
    String technicians = "";
    for (Technician technician : ServiceReportDAO.getTechniciansFor(connection,
        serviceReport.getId())) {
      technicians = technicians + technician.getPerson().getFullName();
    }

    emailBodies[0] = "You have been assigned the following service report " + technicians;
    emailBodies[1] = "You have been assigned the following service report " + technicians;
    return emailBodies;
  }

  public Response getParams(String jsonString) {
    return new Gson().fromJson(jsonString, Response.class);
  }

  class Response {
    private String id;
    private int workOrderId;
    public Integer contactPersonId;
    private String notes;
    private Integer categoryId;
    private Integer equipmentId;
    private String brand;
    private String model;
    private String serialNumber;
    private String partNumber;
    private String reportDate;
    private String technicianIds;
  }
}
