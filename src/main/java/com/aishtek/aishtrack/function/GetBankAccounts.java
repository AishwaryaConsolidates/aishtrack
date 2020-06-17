package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.ArrayList;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.dao.BankAccountDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.services.AishwaryaDetailsService;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetBankAccounts extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {
        ArrayList<NameId> bankAccounts = new ArrayList<NameId>();

        String supplierId = serverlessInput.getQueryStringParameters().get("supplierId");
        String forAishwarya = serverlessInput.getQueryStringParameters().get("forAishwarya");

        if (!Util.isNullOrEmpty(supplierId)) {
          bankAccounts = BankAccountDAO.forSupplier(connection, Util.getInt(supplierId));
        } else if (!Util.isNullOrEmpty(forAishwarya)) {
          bankAccounts = AishwaryaDetailsService.getCurrentAishwaryaBankAccounts(connection);
        }
        output = createSuccessOutput();
        output.setBody(new Gson().toJson(bankAccounts));
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