package com.aishtek.aishtrack.function;

import java.sql.Connection;
import com.aishtek.aishtrack.beans.OutwardRemittance;
import com.aishtek.aishtrack.dao.OutwardRemittanceDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetOutwardRemittance extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {

        int id = Util.getInt(serverlessInput.getQueryStringParameters().get("id"));

        // get visit
        OutwardRemittance remittance = OutwardRemittanceDAO.findById(connection, id);
        remittance.setSignatureDateString(Util.formatDate(remittance.getSignatureDate()));

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(remittance));
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