package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseFunction {

  protected Connection getConnection() throws SQLException {
    Connection connection = DriverManager.getConnection(
        "jdbc:postgresql://aishtek.cufbjsmbrpfk.ap-south-1.rds.amazonaws.com/aishtek", "aishtek",
        "a1shwarya");
    connection.setAutoCommit(false);
    return connection;
  }
}
