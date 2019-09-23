package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.aishtek.aishtrack.beans.Visit;
import com.aishtek.aishtrack.dao.VisitDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetVisits extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {
        String serviceReportId = serverlessInput.getQueryStringParameters().get("serviceReportId");
        String details = serverlessInput.getQueryStringParameters().get("details");

        if (details != null && details.compareTo("true") == 0) {
          ArrayList<Visit> visits =
              VisitDAO.getVisitsDetails(connection, Util.getInt(serviceReportId));
          output = createSuccessOutputForVisits(visits);
        } else {
          ArrayList<HashMap<String, String>> visits = new ArrayList<HashMap<String, String>>();
          visits = VisitDAO.getVisits(connection, Util.getInt(serviceReportId));
          output = createSuccessOutputForArrayHash(visits);
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

  private ServerlessOutput createSuccessOutputForVisits(ArrayList<Visit> result) {
    ServerlessOutput output = new ServerlessOutput();
    output.setStatusCode(200);
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Access-Control-Allow-Origin", "*");
    output.setHeaders(headers);
    output.setBody(new Gson().toJson(result));
    return output;
  }

  private ServerlessOutput createSuccessOutputForArrayHash(
      ArrayList<HashMap<String, String>> result) {
    ServerlessOutput output = new ServerlessOutput();
    output.setStatusCode(200);
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Access-Control-Allow-Origin", "*");
    output.setHeaders(headers);
    output.setBody(new Gson().toJson(result));
    return output;
  }
}