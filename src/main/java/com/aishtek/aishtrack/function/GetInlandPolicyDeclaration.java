package com.aishtek.aishtrack.function;

import java.sql.Connection;
import com.aishtek.aishtrack.beans.InlandPolicyDeclaration;
import com.aishtek.aishtrack.dao.InlandPolicyDeclarationDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetInlandPolicyDeclaration extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {

        int id = Util.getInt(serverlessInput.getQueryStringParameters().get("id"));

        // get visit
        InlandPolicyDeclaration declaration = InlandPolicyDeclarationDAO.findById(connection, id);
        declaration.setInvoiceDateString(Util.formatDate(declaration.getInvoiceDate()));
        declaration.setReceiptDateString(Util.formatDate(declaration.getReceiptDate()));

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(declaration));
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