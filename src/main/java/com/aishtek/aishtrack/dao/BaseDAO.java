package com.aishtek.aishtrack.dao;

import java.sql.Timestamp;
import java.util.Date;

public class BaseDAO {

  public static Timestamp currentTimestamp() {
    return new Timestamp(new Date().getTime());
  }

  public static Timestamp timestampFor(Date date) {
    if (date == null) {
      return null;
    }
    return new Timestamp(date.getTime());
  }

  public static Date dateFor(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }
    return new Date(timestamp.getTime());
  }
}
