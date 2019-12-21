package com.aishtek.aishtrack.function;

import java.sql.Connection;
import com.aishtek.aishtrack.dao.ServiceReportDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class UpdateServiceReportFeedback extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  public static String technicianEmailSubject = "You have been assigned a service report";
  public static String customerEmailSubject = "Aishtek Service Report Assigned";

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        if (!Util.isNullOrEmpty(response.serviceReportCode)) {
          ServiceReportDAO.updateFeedback(connection, response.serviceReportCode,
              response.serviceRating, response.signedBy, response.customerRemarks);
        }

        output = createSuccessOutput(response.serviceReportCode);
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
    return new Gson().fromJson(jsonString, Response.class);
  }

  class Response {
    private String serviceReportCode;
    public Integer serviceRating;
    private String customerRemarks;
    private String signedBy;
  }
}
