package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.sql.SQLException;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.dao.WorkOrderDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class CreateWorkOrder extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        if (Util.isNullOrEmpty(response.id)) {
            int workOrderId = createWorkOrder(connection, response.customerId, response.type, response.notes);
            output = createSuccessOutput("" + workOrderId);
        } else {
          updateWorkOrder(connection, Integer.parseInt(response.id), response.customerId,
              response.type, response.notes);
          output = createSuccessOutput("");
        }
        connection.commit();
      } catch (Exception e) {
        connection.rollback();
        output = createFailureOutput(e);;
      }
    } catch (Exception e) {
      output = createFailureOutput(e);
    }
    return output;
  }

  public int createWorkOrder(Connection connection, int customerId, String type, String notes)
      throws SQLException {
    WorkOrder workOrder = new WorkOrder(customerId, type, notes);
    return WorkOrderDAO.create(connection, workOrder);
  }

  public void updateWorkOrder(Connection connection, int id, int customerId, String type,
      String notes) throws SQLException {
    WorkOrder workOrder = WorkOrderDAO.findById(connection, id);
    workOrder.setCustomerId(customerId);
    workOrder.setType(type);
    workOrder.setNotes(notes);
    WorkOrderDAO.update(connection, workOrder);
  }

  public Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
  }

  class Response {
    public String id;
    public Integer customerId;
    public String notes;
    public String type;
  }
}