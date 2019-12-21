package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import com.aishtek.aishtrack.dao.ExpenseReportDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class SearchExpenseReports extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {
        int technicianId = 0;
        int customerId = 0;
        int settled = 0;
        
        technicianId = Util.getInt(serverlessInput.getQueryStringParameters().get("technicianId"));
        customerId = Util.getInt(serverlessInput.getQueryStringParameters().get("customerId"));
        settled = Util.getInt(serverlessInput.getQueryStringParameters().get("settled"));

        ArrayList<HashMap<String, String>> expenseReports =
            ExpenseReportDAO.searchFor(connection, technicianId, customerId, settled);

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(expenseReports));
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