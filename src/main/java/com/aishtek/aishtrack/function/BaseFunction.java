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

// TODO clean this up, can createSuccessOutput take object, or atleast consolidate
public class BaseFunction {

  public static final String delimiter = "!@#";
  // TODO move this somewhere, secretsmanager costs money, environment variables in not tenable as
  // its at function level
  // staging
  // private static final String dbConnectURL =
  // "jdbc:postgresql://aishtek.cufbjsmbrpfk.ap-south-1.rds.amazonaws.com/aishtek";
  // private static final String dbUsername = "aishtek";
  // private static final String dbPassword = "a1shwarya";
  // protected static final String bucketName = "aishtrackfiles";
  // protected static final String expensesBucketName = "aishtrackexpensefiles";
  // protected static final String fileBaseURL =
  // "https://aishtrackfiles.s3.ap-south-1.amazonaws.com/";
  // protected static final String expensesFileBaseURL =
  // "https://aishtrackexpensefiles.s3.ap-south-1.amazonaws.com/";
  // protected static final String feedbackURL =
  // "https://aishtek.s3.amazonaws.com/aishtrack/serviceReports/serviceReportFeedback.html?serviceReportCode=";

  // production
  private static final String dbConnectURL =
      "jdbc:postgresql://aishtek.c5z8niycvgrg.ap-south-1.rds.amazonaws.com/aishtek";
  private static final String dbUsername = "aishtek";
  private static final String dbPassword = "a1sht3k.com";
  protected static final String bucketName = "aishtrackuploadedfiles";
  protected static final String expensesBucketName = "aishtrackexpensesfiles";
  protected static final String fileBaseURL =
      "http://aishtrackuploadedfiles.s3-website.ap-south-1.amazonaws.com/";
  protected static final String expensesFileBaseURL =
      "http://aishtrackexpensesfiles.s3-website.ap-south-1.amazonaws.com/";
  protected static final String feedbackURL =
      "https://aishtrack.s3.amazonaws.com/serviceReports/serviceReportFeedback.html?serviceReportCode=";


  protected Connection getConnection() throws SQLException {
    Connection connection = DriverManager.getConnection(dbConnectURL, dbUsername, dbPassword);
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
    ServerlessOutput output = createSuccessOutput();
    output.setBody(new Gson().toJson(result));
    return output;
  }

  protected ServerlessOutput createSuccessOutput(HashMap<String, String> result) {
    ServerlessOutput output = createSuccessOutput();
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
