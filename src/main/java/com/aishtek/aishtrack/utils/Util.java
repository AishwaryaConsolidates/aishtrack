package com.aishtek.aishtrack.utils;

public class Util {

  public static boolean isNullOrEmpty(String str) {
    if (str != null && !str.isEmpty())
      return false;
    return true;
  }
}
