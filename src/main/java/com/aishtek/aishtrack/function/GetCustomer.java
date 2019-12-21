package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.dao.AddressDAO;
import com.aishtek.aishtrack.dao.CustomerDAO;
import com.aishtek.aishtrack.dao.WorkOrderDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
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

        ArrayList<HashMap<String, String>> workOrders =
            WorkOrderDAO.searchFor(connection, "", Integer.parseInt(customerId), null);
        customer.setWorkOrders(workOrders);

        ArrayList<HashMap<String, String>> contactPersons =
            CustomerDAO.getContactPersons(connection, Integer.parseInt(customerId));
        customer.setContactPersons(contactPersons);

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