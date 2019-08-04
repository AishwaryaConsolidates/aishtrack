package com.aishtek.aishtrack.function;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.dao.WorkOrderDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetWorkOrders extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output = new ServerlessOutput();

    try (Connection connection = getConnection()) {
      try {
        String customerName = serverlessInput.getQueryStringParameters().get("name");
        String status = serverlessInput.getQueryStringParameters().get("status");
        ArrayList<WorkOrder> workOrders = WorkOrderDAO.searchFor(connection, customerName, status);

        output.setStatusCode(200);
        output.setBody(new Gson().toJson(workOrders));
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
}