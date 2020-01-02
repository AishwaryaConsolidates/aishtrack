package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.sql.SQLException;
import com.aishtek.aishtrack.dao.ServiceReportDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class UpdateServiceReportEquipment extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());
        updateServiceReportEquipment(connection, Integer.parseInt(response.id),
            response.categoryId, response.equipmentId, response.brand, response.model,
            response.serialNumber, response.partNumber);
        output = createSuccessOutput("");
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

  public void updateServiceReportEquipment(Connection connection, int serviceReportId,
      int categoryId, int equipmentId, String brand, String model, String serialNumber,
      String partNumber)
      throws SQLException {
    ServiceReportDAO.updateEquipment(connection, serviceReportId, categoryId, equipmentId, brand,
        model, serialNumber, partNumber);
  }

  public Response getParams(String jsonString) {
    return new Gson().fromJson(jsonString, Response.class);
  }

  class Response {
    private String id;
    private Integer categoryId;
    private Integer equipmentId;
    private String brand;
    private String model;
    private String serialNumber;
    private String partNumber;
  }
}
