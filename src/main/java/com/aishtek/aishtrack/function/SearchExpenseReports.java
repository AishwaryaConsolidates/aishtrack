package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.aishtek.aishtrack.dao.ExpenseReportDAO;
import com.aishtek.aishtrack.dao.TechnicianDAO;
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
        String technicianEmail; //only for technicians
        int technicianId = 0;
        int customerId = 0;
        int settled = 0;
        Date startDate = null;
        Date endDate = null;
        
        technicianEmail = serverlessInput.getQueryStringParameters().get("technicianEmail");
        technicianId = Util.getInt(serverlessInput.getQueryStringParameters().get("technicianIds"));
        if(!Util.isNullOrEmpty(technicianEmail)) {
          technicianId = TechnicianDAO.getTechnicianIdFor(connection, technicianEmail);
        }
        customerId = Util.getInt(serverlessInput.getQueryStringParameters().get("customerId"));
        settled = Util.getInt(serverlessInput.getQueryStringParameters().get("settled"));
        if (!Util.isNullOrEmpty(serverlessInput.getQueryStringParameters().get("startDate"))) {
          startDate = new SimpleDateFormat("dd/MM/yyyy")
              .parse(serverlessInput.getQueryStringParameters().get("startDate"));
        }
        if (!Util.isNullOrEmpty(serverlessInput.getQueryStringParameters().get("endDate"))) {
          startDate = new SimpleDateFormat("dd/MM/yyyy")
              .parse(serverlessInput.getQueryStringParameters().get("endDate"));
        }

        ArrayList<HashMap<String, String>> expenseReports =
            ExpenseReportDAO.searchFor(connection, technicianId, customerId, settled, startDate,
                endDate);

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