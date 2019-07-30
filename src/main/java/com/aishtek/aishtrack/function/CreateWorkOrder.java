package com.aishtek.aishtrack.function;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.dao.WorkOrderDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class CreateWorkOrder extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output = new ServerlessOutput();

    try (Connection connection = getConnection()) {
      connection.setAutoCommit(false);
      try {
        Response response = getParams(serverlessInput.getBody());

        int workOrderId =
            createWorkOrder(connection, response.customerId, response.type, response.notes);

        output.setStatusCode(200);
        output.setBody(new Gson().toJson(workOrderId));

        connection.commit();
      } catch (Exception e) {
        connection.rollback();
      }
    } catch (Exception e) {
      output.setStatusCode(500);
      StringWriter sw = new StringWriter();
      e.printStackTrace(new PrintWriter(sw));
      output.setBody(sw.toString());
    }
    return output;
  }

  public int createWorkOrder(Connection connection, int customerId, String type, String notes)
      throws SQLException {
    WorkOrder workOrder = new WorkOrder(customerId, type, notes);
    return WorkOrderDAO.create(connection, workOrder);
  }

  public Response getParams(String jsonString) {
    return new Gson().fromJson(jsonString, Response.class);
  }

  class Response {
    private int customerId;
    private String notes;
    private String type;
  }
}