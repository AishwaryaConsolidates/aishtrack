package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.ArrayList;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.dao.CustomerDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetCustomers extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {
        String customerName = null;
        if (serverlessInput.getQueryStringParameters() != null
            && serverlessInput.getQueryStringParameters().get("name") != null) {
          customerName = serverlessInput.getQueryStringParameters().get("name");
        }

        ArrayList<Customer> customers = CustomerDAO.searchFor(connection, customerName);
        ArrayList<NameId> dropdown = NameId.convertCustomersToNameId(customers);

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(dropdown));
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