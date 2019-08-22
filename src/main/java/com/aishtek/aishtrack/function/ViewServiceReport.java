package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.HashMap;
import com.aishtek.aishtrack.dao.ServiceReportDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ViewServiceReport extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        String serviceReporCode =
            serverlessInput.getQueryStringParameters().get("serviceReportCode");

        HashMap<String, String> serviceReport =
            ServiceReportDAO.findByCode(connection, serviceReporCode);
        output = createSuccessOutput(serviceReport);
        // get technician details

        // get visit details
      } catch (Exception e) {
        output = createFailureOutput(e);
      }
    } catch (Exception e) {
      output = createFailureOutput(e);
    }

    return output;
  }
}
