package com.aishtek.aishtrack.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class BaseBean {

  protected int id;
  protected Date createdAt;
  protected Date updatedAt;
  protected int deleted = 0;

  protected static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(
        "jdbc:postgresql://aishtek.cufbjsmbrpfk.ap-south-1.rds.amazonaws.com/aishtek", "aishtek",
        "a1shwarya");
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public static Timestamp currentTimestamp() {
    return new Timestamp(new Date().getTime());
  }

  public static Timestamp timestampFor(Date date) {
    return new Timestamp(date.getTime());
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public int getDeleted() {
    return deleted;
  }

  public void setDeleted(int deleted) {
    this.deleted = deleted;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }
}
