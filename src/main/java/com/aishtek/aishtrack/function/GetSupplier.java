package com.aishtek.aishtrack.function;

import java.sql.Connection;
import com.aishtek.aishtrack.beans.Supplier;
import com.aishtek.aishtrack.dao.BankAccountDAO;
import com.aishtek.aishtrack.dao.SupplierDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetSupplier extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;

    try (Connection connection = getConnection()) {
      try {

        int supplierId = Util.getInt(serverlessInput.getQueryStringParameters().get("supplierId"));

        Supplier supplier = SupplierDAO.findById(connection, supplierId);
        supplier.setAddresses(SupplierDAO.getAddresses(connection, supplierId));
        supplier.setBankAccounts(BankAccountDAO.forSupplier(connection, supplierId));

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(supplier));
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