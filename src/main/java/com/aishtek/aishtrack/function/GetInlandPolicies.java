package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.ArrayList;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.services.AishwaryaDetailsService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetInlandPolicies extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {
        ArrayList<NameId> inlandPolicies = new ArrayList<NameId>();

        inlandPolicies = AishwaryaDetailsService.getCurrentInlandPolicies(connection);

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(inlandPolicies));
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