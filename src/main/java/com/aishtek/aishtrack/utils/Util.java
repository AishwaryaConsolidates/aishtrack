package com.aishtek.aishtrack.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

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

  public static String formatDate(Date date) {
    if (date != null) {
      return (new SimpleDateFormat("dd/MM/yyyy")).format(date);
    } else {
      return "";
    }
  }
}
