package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import com.aishtek.aishtrack.dao.VisitDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetVisitFiles extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {
        int visitId = Util.getInt(serverlessInput.getQueryStringParameters().get("visitId"));
        int serviceReportId =
            Util.getInt(serverlessInput.getQueryStringParameters().get("serviceReportId"));

        ArrayList<HashMap<String, String>> visitFiles = new ArrayList<HashMap<String, String>>();
        if (visitId > 0) {
          visitFiles = VisitDAO.getVisitFiles(connection, visitId);
        } else if (serviceReportId > 0) {
          visitFiles = VisitDAO.getScoutingReportFiles(connection, visitId);
        }

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(visitFiles));
      } catch (Exception e) {
        connection.rollback();
        output = createFailureOutput(e);
      }
    } catch (Exception e) {
      output = createFailureOutput(e);
    }
    return output;
  }
}