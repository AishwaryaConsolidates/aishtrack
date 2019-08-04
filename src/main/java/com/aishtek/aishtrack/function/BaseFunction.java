package com.aishtek.aishtrack.function;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.google.gson.Gson;

public class BaseFunction {

  protected Connection getConnection() throws SQLException {
    Connection connection = DriverManager.getConnection(
        "jdbc:postgresql://aishtek.cufbjsmbrpfk.ap-south-1.rds.amazonaws.com/aishtek", "aishtek",
        "a1shwarya");
    connection.setAutoCommit(false);
    return connection;
  }

  protected ServerlessOutput createSuccessOutput(String result) {
    ServerlessOutput output = new ServerlessOutput();
    output.setStatusCode(200);
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Access-Control-Allow-Origin", "*");
    output.setHeaders(headers);
    output.setBody(new Gson().toJson(result));
    return output;
  }

  protected ServerlessOutput createFailureOutput(Exception e) {
    ServerlessOutput output = new ServerlessOutput();
    output.setStatusCode(500);
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Access-Control-Allow-Origin", "*");
    output.setHeaders(headers);
    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw));
    output.setBody(new Gson().toJson(sw.toString()));
    return output;
  }
}
