package com.aishtek.aishtrack.function;

import java.sql.Connection;
import com.aishtek.aishtrack.beans.ExpenseReport;
import com.aishtek.aishtrack.dao.ExpenseReportDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetExpenseReport extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {

        int expenseReportId =
            Util.getInt(serverlessInput.getQueryStringParameters().get("expenseReportId"));

        // get expense report
        ExpenseReport expenseReport = ExpenseReportDAO.findById(connection, expenseReportId);
        expenseReport.prepareForPrint();

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(expenseReport));
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