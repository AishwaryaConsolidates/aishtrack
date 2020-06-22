package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.aishtek.aishtrack.dao.DomesticRemittanceDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class SearchDomesticRemittances extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {
        int supplierId = 0;
        Date startDate = null;
        Date endDate = null;
        
        supplierId = Util.getInt(serverlessInput.getQueryStringParameters().get("supplierId"));
        if (!Util.isNullOrEmpty(serverlessInput.getQueryStringParameters().get("startDate"))) {
          startDate = new SimpleDateFormat("dd/MM/yyyy")
              .parse(serverlessInput.getQueryStringParameters().get("startDate"));
        }
        if (!Util.isNullOrEmpty(serverlessInput.getQueryStringParameters().get("endDate"))) {
          endDate = new SimpleDateFormat("dd/MM/yyyy")
              .parse(serverlessInput.getQueryStringParameters().get("endDate"));
        }

        ArrayList<HashMap<String, String>> remittances =
            DomesticRemittanceDAO.searchFor(connection, supplierId, startDate, endDate);

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(remittances));
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