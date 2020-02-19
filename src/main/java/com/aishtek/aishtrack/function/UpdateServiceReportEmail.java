package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.sql.SQLException;
import com.aishtek.aishtrack.dao.ServiceReportDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class UpdateServiceReportEmail extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        updateServiceReportEmail(connection, Integer.parseInt(response.emailServiceReportId),
            response.additionalEmail);
        output = createSuccessOutput("");
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

  public void updateServiceReportEmail(Connection connection, int serviceReportId, String email)
      throws SQLException {
    ServiceReportDAO.updateAdditionalEmail(connection, serviceReportId, email);
  }

  public Response getParams(String jsonString) {
    return new Gson().fromJson(jsonString, Response.class);
  }

  class Response {
    private String emailServiceReportId;
    private String additionalEmail;
  }
}
