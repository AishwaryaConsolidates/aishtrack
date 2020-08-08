package com.aishtek.aishtrack.dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import com.aishtek.aishtrack.utils.Util;

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

  public static Timestamp endOfDayTimestamp(Date date) {
    if (date == null) {
      return null;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    c.set(Calendar.MILLISECOND, 999);
    return new Timestamp(c.getTimeInMillis());
  }

  public static Timestamp beginningOfDayTimestamp(Date date) {
    if (date == null) {
      return null;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 1);
    return new Timestamp(c.getTimeInMillis());
  }

  public static Date dateFor(Timestamp timestamp) {
    if (timestamp == null) {
      return null;
    }
    return new Date(timestamp.getTime());
  }

  public static String formatTimestamp(Timestamp timestamp) {
    return Util.formatDate(dateFor(timestamp));
  }
}
