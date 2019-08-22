package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.ArrayList;
import com.aishtek.aishtrack.beans.ServiceReport;
import com.aishtek.aishtrack.dao.ServiceReportDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.aishtek.aishtrack.utils.WorkStatus;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetServiceReports extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {

        String workOrderId = serverlessInput.getQueryStringParameters().get("workOrderId");

        ArrayList<ServiceReport> serviceReports = ServiceReportDAO.searchFor(connection, "", 0,
            Util.getInt(workOrderId), WorkStatus.openStatuses());

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(serviceReports));
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