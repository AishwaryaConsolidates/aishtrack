package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.aishtek.aishtrack.dao.LeaveApplicationDAO;
import com.aishtek.aishtrack.dao.TechnicianDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class SearchLeaveApplications extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {
        String technicianEmail; //only for technicians
        int technicianId = 0;
        Date startDate = null;
        Date endDate = null;
        
        technicianEmail = serverlessInput.getQueryStringParameters().get("technicianEmail");
        technicianId = Util.getInt(serverlessInput.getQueryStringParameters().get("technicianIds"));
        if(!Util.isNullOrEmpty(technicianEmail)) {
          technicianId = TechnicianDAO.getTechnicianIdFor(connection, technicianEmail);
        }

        if (!Util.isNullOrEmpty(serverlessInput.getQueryStringParameters().get("startDate"))) {
          startDate = new SimpleDateFormat("dd/MM/yyyy")
              .parse(serverlessInput.getQueryStringParameters().get("startDate"));
        }
        if (!Util.isNullOrEmpty(serverlessInput.getQueryStringParameters().get("endDate"))) {
          endDate = new SimpleDateFormat("dd/MM/yyyy")
              .parse(serverlessInput.getQueryStringParameters().get("endDate"));
        }

        ArrayList<HashMap<String, String>> leaveApplications =
            LeaveApplicationDAO.searchFor(connection, technicianId, startDate, endDate,
                serverlessInput.getQueryStringParameters().get("status"));

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(leaveApplications));
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