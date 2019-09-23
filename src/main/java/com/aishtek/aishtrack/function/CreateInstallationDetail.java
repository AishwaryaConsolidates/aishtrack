package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import com.aishtek.aishtrack.dao.ServiceReportDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.utils.Util;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class CreateInstallationDetail extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output = new ServerlessOutput();
    try (Connection connection = getConnection()) {
      try {
        Response response = getParams(serverlessInput.getBody());

        String[] keys = response.key.split(delimiter);
        String[] values = response.value.split(delimiter);
        boolean damaged = false;
        if (!Util.isNullOrEmpty(response.equipmentDamaged)
            && response.equipmentDamaged.compareTo("on") == 0) {
          damaged = true;
        }
        ServiceReportDAO.updateEquipmentDamaged(connection, response.serviceReportId, damaged);

        HashMap<String, HashMap<String, String>> hashMap = ServiceReportDAO.addInstallationDetail(
            connection, response.serviceReportId, response.detail, keys, values);

        output = createSuccessOutputForHashMapHashMap(hashMap);
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

  public Response getParams(String jsonString) {
    return (new Gson()).fromJson(jsonString, Response.class);
  }

  private ServerlessOutput createSuccessOutputForHashMapHashMap(
      HashMap<String, HashMap<String, String>> result) {
    ServerlessOutput output = new ServerlessOutput();
    output.setStatusCode(200);
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Access-Control-Allow-Origin", "*");
    output.setHeaders(headers);
    output.setBody(new Gson().toJson(result));
    return output;
  }
  class Response {
    public Integer serviceReportId;
    public String detail;
    public String key;
    public String value;
    public String equipmentDamaged;
  }
}