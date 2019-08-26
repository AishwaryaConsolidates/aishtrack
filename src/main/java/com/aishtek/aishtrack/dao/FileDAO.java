package com.aishtek.aishtrack.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.aishtek.aishtrack.beans.File;

public class FileDAO extends BaseDAO {
  public static int create(Connection connection, File file) throws SQLException {
      PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into files (name, location) values(?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
      
    preparedStatement.setString(1, file.getName());
    preparedStatement.setString(2, file.getLocation());

    preparedStatement.executeUpdate();

      ResultSet result = preparedStatement.getGeneratedKeys();
      if (result.next()) {
      return result.getInt(1);
      } else {
      throw new SQLException("File ID not generted");
      }
  }
}
