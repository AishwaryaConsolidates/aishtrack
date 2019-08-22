package com.aishtek.aishtrack.utils;

public class Util {

  public static boolean isNullOrEmpty(String str) {
    if (str != null && !str.isEmpty())
      return false;
    return true;
  }

  public static boolean isNullOrEmpty(String[] str) {
    if (str != null && str.length > 0)
      return false;
    return true;
  }

  public static int getInt(String str) {
    try {
      return Integer.parseInt(str);
    } catch (NumberFormatException e) {
      return 0;
    }
  }
}
