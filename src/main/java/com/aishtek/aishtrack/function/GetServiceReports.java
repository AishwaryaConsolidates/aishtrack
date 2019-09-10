package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import com.aishtek.aishtrack.dao.ServiceReportDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetServiceReports extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {
        int workOrderId = 0;
        int customerId = 0;
        String customerName = "";
        String[] statuses = null;
        
        workOrderId = Util.getInt(serverlessInput.getQueryStringParameters().get("workOrderId"));
        customerName = serverlessInput.getQueryStringParameters().get("customerName");

        ArrayList<HashMap<String, String>> serviceReports = ServiceReportDAO.searchFor(connection, customerName, customerId, workOrderId, statuses);

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(serviceReports));
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