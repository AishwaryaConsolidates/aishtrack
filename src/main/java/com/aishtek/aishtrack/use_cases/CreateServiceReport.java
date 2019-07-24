package com.aishtek.aishtrack.use_cases;

import java.io.IOException;
import com.aishtek.aishtrack.models.ServiceReport;
import com.aishtek.aishtrack.models.ServiceReportTechnician;
import com.aishtek.aishtrack.models.Technician;
import com.aishtek.aishtrack.models.WorkOrder;
import com.aishtek.aishtrack.services.EmailSenderService;

public class CreateServiceReport {

  public static String technicianEmailSubject = "You have been assigned a service report";

  public static ServiceReport process(int workOrderId, int[] technicianIds, String notes) {
    WorkOrder workOrder = WorkOrder.findById(workOrderId);

    // create blank service report
    ServiceReport serviceReport =
        ServiceReport.createBlankServiceReport(workOrder.getCustomer(), notes);

    // create service report technician
    for (int technicianId : technicianIds) {
      ServiceReportTechnician.assignServiceReportTechnicain(serviceReport.getServiceRportId(),
          technicianId);
    }
    // change status of work order
    workOrder.markAsAssigned();
    
    // send email with link for service report update
    sendEmailToTechnicians(serviceReport);

    // notify customer
    sendEmailToCustomer(serviceReport);

    return serviceReport;
  }

  private static void sendEmailToTechnicians(ServiceReport serviceReport) {
    try {
      serviceReport.refresh();
      for (Technician technician : serviceReport.getTechnicians()) {
        String[] to = {technician.getEmailAddress()};
        String[] emailBodies = technicianEmailBodies(serviceReport.getCode());
        EmailSenderService.sendEmail(to, technicianEmailSubject, emailBodies[0], emailBodies[1]);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String[] technicianEmailBodies(String serviceReportCode) {
    String url = "http://aishtek.aishtrack.com/" + serviceReportCode;
    String[] emailBodies = new String[2];
    emailBodies[0] =
        "You have been assigned the following service report, please click on the link to start work on it. <a href=\""
            + url + "\">";
    emailBodies[1] = "You have been assigned the following service report " + url;
    return emailBodies;
  }

  private static void sendEmailToCustomer(ServiceReport serviceReport) {
    try {
      serviceReport.refresh();
      String[] to = {serviceReport.getCustomer().getContactPerson().getEmailAddress()};
      String[] emailBodies = customerEmailBodies(serviceReport);
      EmailSenderService.sendEmail(to, technicianEmailSubject, emailBodies[0], emailBodies[1]);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String[] customerEmailBodies(ServiceReport serviceReport) {
    String[] emailBodies = new String[2];
    emailBodies[0] = "The Following Technician(s) have been assigned to address your problem ";
    String technicians = "";
    for (Technician technician : serviceReport.getTechnicians()) {
      technicians = technicians + technician.getFullName();
    }
    emailBodies[0] = "You have been assigned the following service report " + technicians;
    emailBodies[1] = "You have been assigned the following service report " + technicians;
    return emailBodies;
  }
}
