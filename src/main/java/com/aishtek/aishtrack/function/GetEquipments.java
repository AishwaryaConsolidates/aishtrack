package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.ArrayList;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.dao.CategoryDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class GetEquipments extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        String categoryId = serverlessInput.getQueryStringParameters().get("categoryId");
        ArrayList<NameId> subCategories =
            CategoryDAO.getEquipments(connection, Util.getInt(categoryId));

        output = createSuccessOutput();
        output.setBody(new Gson().toJson(subCategories));
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