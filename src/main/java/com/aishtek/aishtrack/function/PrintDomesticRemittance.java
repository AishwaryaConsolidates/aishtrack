package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.HashMap;
import com.aishtek.aishtrack.dao.DomesticRemittanceDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.services.EncryptionService;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class PrintDomesticRemittance extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {
        EncryptionService encryptionService = new EncryptionService();
        int id = Util.getInt(serverlessInput.getQueryStringParameters().get("id"));

        // get visit
        HashMap<String, String> remittance = DomesticRemittanceDAO.findForPrint(connection, id);

        remittance.put("supplierBankAccountNumber",
            encryptionService.decrypt(remittance.get("supplierBankAccountNumberEncrypted"),
                Integer.parseInt(remittance.get("supplierBankAccountId"))));
        remittance.remove("supplierBankAccountNumberEncrypted");
        // remittance.put("fromBankAccountNumber",
        // encryptionService.decrypt(remittance.get("fromBankAccountNumberEncrypted")));
        // remittance.remove("fromBankAccountNumberEncrypted");


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