package com.aishtek.aishtrack.function;

import java.sql.Connection;
import com.aishtek.aishtrack.beans.LeaveApplication;
import com.aishtek.aishtrack.dao.LeaveApplicationDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetLeaveApplication extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {

        int leaveApplicationId =
            Util.getInt(serverlessInput.getQueryStringParameters().get("leaveApplicationId"));

        // get expense report
        LeaveApplication leaveApplication =
            LeaveApplicationDAO.findById(connection, leaveApplicationId);
        leaveApplication.setFromDateString(Util.formatDate(leaveApplication.getFromDate()));
        leaveApplication.setToDateString(Util.formatDate(leaveApplication.getToDate()));
        leaveApplication
            .setSignatureDateString(Util.formatDate(leaveApplication.getSignatureDate()));
        output = createSuccessOutput();
        output.setBody(new Gson().toJson(leaveApplication));
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