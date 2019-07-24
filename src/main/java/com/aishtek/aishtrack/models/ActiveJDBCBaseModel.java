package com.aishtek.aishtrack.models;

import java.sql.Timestamp;
import java.util.Date;
import org.javalite.activejdbc.Model;


public class ActiveJDBCBaseModel extends Model {

  public static Timestamp currentTimestamp() {
    return new Timestamp(new Date().getTime());
  }
}
