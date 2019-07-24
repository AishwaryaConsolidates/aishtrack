package com.aishtek.aishtrack.function;

import java.util.Map;
import com.google.gson.Gson;

public class BaseFunction {

  @SuppressWarnings("unchecked")
  public Map<String, String> getParams(String jsonString) {
    Gson gson = new Gson();
    return gson.fromJson(jsonString, Map.class);
  }
}