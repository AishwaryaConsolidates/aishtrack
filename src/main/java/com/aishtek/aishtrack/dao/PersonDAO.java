package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.aishtek.aishtrack.beans.Person;

public class PersonDAO extends BaseDAO {
  public static int create(Connection connection, Person person) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into persons (first_name, last_name, designation, phone, email) values(?, ?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);
    preparedStatement.setString(1, person.getFirstName());
    preparedStatement.setString(2, person.getLastName());
    preparedStatement.setString(3, person.getDesignation());
    preparedStatement.setString(4, person.getPhone());
    preparedStatement.setString(5, person.getEmail());
    preparedStatement.executeUpdate();

    ResultSet result = preparedStatement.getGeneratedKeys();
    if (result.next()) {
      return result.getInt(1);
    } else {
      throw new SQLException("Person Id not generted");
    }
  }

  public static Person findById(Connection connection, int personId) throws SQLException {
    String sql =
        "SELECT id, first_name, last_name, designation, email, phone, deleted FROM persons where id = ?";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, personId);
    ResultSet result = statement.executeQuery();

    if (result.next()) {
      Person person = new Person(result.getInt(1), result.getString(2), result.getString(3),
          result.getString(4), result.getString(5), result.getString(6), result.getInt(7));
      return person;
    } else {
      throw new SQLException("No person for Id");
    }
  }
}
