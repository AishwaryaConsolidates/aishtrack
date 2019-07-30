package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.aishtek.aishtrack.beans.Technician;

public class TechnicianDAO extends BaseDAO {
  public static Technician findById(Connection connection, int technicianId) throws SQLException {
      String sql =
        "SELECT id, person_id, deleted FROM technicians where id = ?";

      PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, technicianId);
      ResultSet result = statement.executeQuery();

      if (result.next()) {
      Technician technician = new Technician(result.getInt(1), result.getInt(2), result.getInt(3));
      return technician;
    } else {
      throw new SQLException("No technician for Id");
      }
  }

  public static int create(Connection connection, int personId) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into technicians (person_id) values(?)",
        PreparedStatement.RETURN_GENERATED_KEYS);
    preparedStatement.setInt(1, personId);
    preparedStatement.executeUpdate();

    ResultSet result = preparedStatement.getGeneratedKeys();
    if (result.next()) {
      return result.getInt(1);
    } else {
      throw new SQLException("Technician ID not generted");
    }
  }
}
