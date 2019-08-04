package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

  public static ArrayList<Technician> searchFor(Connection connection) throws SQLException {
    String sql =
        "SELECT t.id, p.first_name, p.last_name, p.designation FROM technicians t, persons p where t.person_id = p.id and t.deleted = 0 ";

    PreparedStatement statement = connection.prepareStatement(sql);

    ResultSet result = statement.executeQuery();

    ArrayList<Technician> technicians = new ArrayList<Technician>();
    while (result.next()) {
      technicians.add(new Technician(result.getInt(1), result.getString(2), result.getString(3),
          result.getString(4)));
    }
    return technicians;
  }

  public static void delete(Connection connection, int technicianId) throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement("update technicians set deleted = 1 where id = ?");
    preparedStatement.setInt(1, technicianId);
    preparedStatement.executeUpdate();

    preparedStatement = connection.prepareStatement(
        "update persons set deleted = 1 from persons p, technicians t where t.person_id = p.id and t.id = ?");
    preparedStatement.setInt(1, technicianId);
    preparedStatement.executeUpdate();
  }
}
