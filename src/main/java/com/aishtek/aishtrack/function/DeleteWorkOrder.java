package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.sql.SQLException;
import com.aishtek.aishtrack.dao.ServiceReportDAO;
import com.aishtek.aishtrack.dao.VisitDAO;
import com.aishtek.aishtrack.dao.WorkOrderDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class DeleteWorkOrder extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        if (response.deleteWorkOrderId > 0) {
          deleteWorkOrder(connection, response.deleteWorkOrderId);
        }
        output = createSuccessOutput("" + response.deleteWorkOrderId);
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

  public void deleteWorkOrder(Connection connection, Integer id) throws SQLException {
    VisitDAO.deleteForWorkOrder(connection, id);
    ServiceReportDAO.deleteForWorkOrder(connection, id);
    WorkOrderDAO.delete(connection, id);
  }

  public Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
  }

  class Response {
    public Integer deleteWorkOrderId;
  }
}