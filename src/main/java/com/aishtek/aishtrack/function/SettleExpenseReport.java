package com.aishtek.aishtrack.function;

import java.sql.Connection;
import com.aishtek.aishtrack.dao.ExpenseReportDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class SettleExpenseReport extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output = new ServerlessOutput();
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        ExpenseReportDAO.settle(connection, Util.getInt(response.expenseReportId));
        output = createSuccessOutput("" + response.expenseReportId);
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

  public Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
  }

  class Response {
    public String expenseReportId;
  }
}