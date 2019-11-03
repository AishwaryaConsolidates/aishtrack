package com.aishtek.aishtrack.function;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.aishtek.aishtrack.test.BaseIntegrationTest;
import com.google.gson.Gson;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class BaseFunctionTest extends BaseIntegrationTest {

  @Test
  public void createFailureOutputSetCORSTest() {
    Exception e = new Exception("Something went wrong");
    ServerlessOutput serverlessOutput = (new BaseFunction()).createFailureOutput(e);

    Map<String, String> headers = serverlessOutput.getHeaders();
    assertEquals(headers.get("Access-Control-Allow-Origin"), "*");
  }

  @Test
  public void createFailureOutputSetStatusCode500() {
    Exception e = new Exception("Something went wrong");
    ServerlessOutput serverlessOutput = (new BaseFunction()).createFailureOutput(e);
    assertEquals(serverlessOutput.getStatusCode(), new Integer(500));
  }

  @Test
  public void createSuccessOutputtSetCORSTest() {
    ServerlessOutput serverlessOutput = (new BaseFunction()).createSuccessOutput();

    Map<String, String> headers = serverlessOutput.getHeaders();
    assertEquals(headers.get("Access-Control-Allow-Origin"), "*");
  }

  @Test
  public void createSuccessOutputSetStatusCode200() {
    ServerlessOutput serverlessOutput = (new BaseFunction()).createSuccessOutput();
    assertEquals(serverlessOutput.getStatusCode(), new Integer(200));
  }

  @Test
  public void createSuccessOutputForStringOutputsJson() {
    String result = "somestring";
    ServerlessOutput serverlessOutput = (new BaseFunction()).createSuccessOutput(result);
    assertEquals(serverlessOutput.getBody(), new Gson().toJson(result));
  }

  @Test
  public void createSuccessOutputForHashMapOutputsJson() {
    HashMap<String, String> result = new HashMap<String, String>();
    result.put("some_key", "some_value");
    ServerlessOutput serverlessOutput = (new BaseFunction()).createSuccessOutput(result);
    assertEquals(serverlessOutput.getBody(), new Gson().toJson(result));
  }

  @Test
  public void getIntegerListReturnsIntegerListForCSV() {
    String result = "1!@#11!@#111";
    ArrayList<Integer> intList = (new BaseFunction()).getIntegerList(result);
    assertEquals(intList.size(), 3);
    assertEquals(intList.get(0), new Integer(1));
    assertEquals(intList.get(1), new Integer(11));
    assertEquals(intList.get(2), new Integer(111));
  }

  @Test
  public void getStringListReturnsStringListForCSV() {
    String result = "May!@#the!@#force!@#be!@#with!@#you";
    ArrayList<String> strList = (new BaseFunction()).getStringList(result);
    assertEquals(strList.size(), 6);
    assertEquals(strList.get(0), "May");
    assertEquals(strList.get(1), "the");
    assertEquals(strList.get(2), "force");
    assertEquals(strList.get(3), "be");
    assertEquals(strList.get(4), "with");
    assertEquals(strList.get(5), "you");
  }
}
