package com.aishtek.aishtrack.function;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import com.aishtek.aishtrack.dao.ServiceReportDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.services.EmailSenderService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class SendServiceReportFeedbackEmail extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        sendServiceReportFeedbackEmail(connection, Integer.parseInt(response.id));
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

  public void sendServiceReportFeedbackEmail(Connection connection, int serviceReportId)
      throws SQLException, IOException {
    ArrayList<String> toEmails = ServiceReportDAO.getEmailForFeedback(connection, serviceReportId);
    String serviceReportCode = ServiceReportDAO.getCodeForId(connection, serviceReportId);

    String[] to = (String[]) toEmails.toArray();
    String[] emailBodies = feedbackEmailBodies(connection, serviceReportCode);
    EmailSenderService.sendEmail(to, "Feedback for Aishwarya Work Order", emailBodies[0],
        emailBodies[1]);
  }

  private String[] feedbackEmailBodies(Connection connection, String serviceReportCode)
      throws SQLException {
    String[] emailBodies = new String[2];
    String feedbackLink = "";
    String message =
        "We have completed the work order, and, would like your feedback on how we have performed, please click on the following link and let us know how we did.";
    message = message + "<a href=\"" + feedbackLink + "\">" + feedbackLink + "</a>";

    emailBodies[0] = message;
    emailBodies[1] = message;
    return emailBodies;
  }

  public Response getParams(String jsonString) {
    return new Gson().fromJson(jsonString, Response.class);
  }

  class Response {
    private String id;
  }
}
