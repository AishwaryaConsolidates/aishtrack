package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.HashMap;
import com.aishtek.aishtrack.dao.WorkOrderDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ViewWorkOrder extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        int workOrderId =
            Util.getInt(serverlessInput.getQueryStringParameters().get("id"));

        HashMap<String, String> workOrder = WorkOrderDAO.findForView(connection, workOrderId);
        output = createSuccessOutput(workOrder);
      } catch (Exception e) {
        output = createFailureOutput(e);
      }
    } catch (Exception e) {
      output = createFailureOutput(e);
    }

    return output;
  }
}
