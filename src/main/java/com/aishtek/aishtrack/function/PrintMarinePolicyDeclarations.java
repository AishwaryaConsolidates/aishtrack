package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.aishtek.aishtrack.beans.DeclarationsReport;
import com.aishtek.aishtrack.dao.MarinePolicyDAO;
import com.aishtek.aishtrack.dao.MarinePolicyDeclarationDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class PrintMarinePolicyDeclarations extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {
        Date startDate = null;
        Date endDate = null;
        
        if (!Util.isNullOrEmpty(serverlessInput.getQueryStringParameters().get("startDate"))) {
          startDate = new SimpleDateFormat("dd/MM/yyyy")
              .parse(serverlessInput.getQueryStringParameters().get("startDate"));
        }
        if (!Util.isNullOrEmpty(serverlessInput.getQueryStringParameters().get("endDate"))) {
          endDate = new SimpleDateFormat("dd/MM/yyyy")
              .parse(serverlessInput.getQueryStringParameters().get("endDate"));
        }   

        ArrayList<DeclarationsReport> policyReports =
            MarinePolicyDAO.getMarinePoliciyReports(connection, startDate, endDate);

        for (DeclarationsReport policyReport : policyReports) {

          policyReport.setAmountUsed(MarinePolicyDAO.getAmountUsed(connection,
              policyReport.getPolicyId(), startDate));

          policyReport.setDeclarations(
              MarinePolicyDeclarationDAO.searchFor(connection, 0, startDate, endDate,
                  policyReport.getPolicyId()));

          policyReport.calculateAmounts();
        }

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(policyReports));
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