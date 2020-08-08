package com.aishtek.aishtrack.function;

import java.sql.Connection;
import com.aishtek.aishtrack.dao.SupplierDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class UpdateSupplier extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());

        int supplierId = intForId(response.supplierId); // for updates

        supplierId = SupplierDAO.save(connection, supplierId, response.name, response.type);

        output = createSuccessOutput("" + supplierId);
        connection.commit();
      } catch (Exception e) {
        connection.rollback();
        output = createFailureOutput(e);
      }
    } catch (Exception e) {
      output = createFailureOutput(e);
    }
    return output;
  }

  private Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
  }

  class Response {
    public String supplierId;
    public String name;
    private String type;
  }
}