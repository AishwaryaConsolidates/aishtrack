package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.dao.WorkOrderDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetWorkOrders extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {
        String customerName = serverlessInput.getQueryStringParameters().get("name");
        String status = serverlessInput.getQueryStringParameters().get("status");
        String workOrderId = serverlessInput.getQueryStringParameters().get("workOrderId");
        String customerId = serverlessInput.getQueryStringParameters().get("customerId");

        String[] statuses = null;
        if (!Util.isNullOrEmpty(status)) {
          statuses = new String[1];
          statuses[0] = status;
        }
        
        if (Util.isNullOrEmpty(workOrderId)) {
          output = searchWorkOrders(connection, customerName, customerId, statuses);
        } else {
          output = getWorkOrder(connection, workOrderId);
        }
      } catch (Exception e) {
        connection.rollback();
        output = createFailureOutput(e);
      }
    } catch (Exception e) {
      output = createFailureOutput(e);
    }
    return output;
  }

  private ServerlessOutput searchWorkOrders(Connection connection, String customerName,
      String customerId, String[] status) throws SQLException {
    ServerlessOutput output;
    ArrayList<WorkOrder> workOrders =
        WorkOrderDAO.searchFor(connection, customerName, Util.getInt(customerId), status);

    output = createSuccessOutput();
    output.setBody(new Gson().toJson(workOrders));
    return output;
  }

  private ServerlessOutput getWorkOrder(Connection connection, String workOrderId)
      throws SQLException {
    ServerlessOutput output;
    WorkOrder workOrder = WorkOrderDAO.findById(connection, Util.getInt(workOrderId));

    output = createSuccessOutput();
    output.setBody(new Gson().toJson(workOrder));
    return output;
  }
}