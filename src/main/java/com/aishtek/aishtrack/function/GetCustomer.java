package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.ArrayList;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.beans.ServiceReport;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.dao.AddressDAO;
import com.aishtek.aishtrack.dao.CustomerDAO;
import com.aishtek.aishtrack.dao.PersonDAO;
import com.aishtek.aishtrack.dao.ServiceReportDAO;
import com.aishtek.aishtrack.dao.WorkOrderDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.aishtek.aishtrack.utils.WorkStatus;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetCustomer extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {

        String customerId = serverlessInput.getQueryStringParameters().get("customerId");

        Customer customer = CustomerDAO.findById(connection, Util.getInt(customerId));
        customer.setAddress(AddressDAO.findById(connection, customer.getAddressId()));
        customer.setContactPerson(PersonDAO.findById(connection, customer.getContactPersonId()));

        ArrayList<WorkOrder> workOrders = WorkOrderDAO.searchFor(connection, "",
            Integer.parseInt(customerId), WorkStatus.openStatuses());
        customer.setWorkOrders(workOrders);
        ArrayList<ServiceReport> serviceReports = ServiceReportDAO.searchFor(connection, "",
            Integer.parseInt(customerId), 0, WorkStatus.openStatuses());
        customer.setServiceReports(serviceReports);

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(customer));
      } catch (Exception e) {
        connection.rollback();
        output = createFailureOutput(e);
      }
    } catch (Exception e) {
      output = createFailureOutput(e);
    }
    return output;
  }
}