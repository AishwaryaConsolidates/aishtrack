package com.aishtek.aishtrack.function;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.google.gson.Gson;

// TODO clean this up, can createSuccessOutput take object
public class BaseFunction {

  public static final String delimiter = "!@#";

  protected Connection getConnection() throws SQLException {
    Connection connection = DriverManager.getConnection(
        "jdbc:postgresql://aishtek.cufbjsmbrpfk.ap-south-1.rds.amazonaws.com/aishtek", "aishtek",
        "a1shwarya");
    connection.setAutoCommit(false);
    return connection;
  }

  protected ServerlessOutput createSuccessOutput() {
    ServerlessOutput output = new ServerlessOutput();
    output.setStatusCode(200);
    Map<String, String> headers = new HashMap<String, String>();
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Content-Type", "application/json");
    output.setHeaders(headers);
    return output;
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

  protected ServerlessOutput createSuccessOutput(HashMap<String, String> result) {
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

  protected ArrayList<Integer> getIntegerList(String csv) {
    ArrayList<Integer> intList = new ArrayList<Integer>();
    String[] elements = csv.split(delimiter);
    for (String element : elements) {
      intList.add(Integer.parseInt(element));
    }
    return intList;
  }

  protected ArrayList<String> getStringList(String csv) {
    return new ArrayList<String>(Arrays.asList(csv.split(delimiter)));
  }
}
