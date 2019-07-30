package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.aishtek.aishtrack.beans.Address;

public class AddressDAO extends BaseDAO {
  public static int create(Connection connection, Address address) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into addresses (street, area, city, state, pincode) values (?, ?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);
    preparedStatement.setString(1, address.getStreet());
    preparedStatement.setString(2, address.getArea());
    preparedStatement.setString(3, address.getCity());
    preparedStatement.setString(4, address.getState());
    preparedStatement.setString(5, address.getPincode());
    int affectedRows = preparedStatement.executeUpdate();
    if (affectedRows == 0) {
      throw new SQLException("Creating address failed, no rows affected.");
    }
    try (ResultSet result = preparedStatement.getGeneratedKeys()) {
      if (result.next()) {
        return result.getInt(1);
      } else {
        throw new SQLException("Address Id not generted");
      }
    } catch (SQLException sqle) {
      throw sqle;
    }
  }

  public static Address findById(Connection connection, int addressId) throws SQLException {
    String sql =
        "SELECT id, street, area, city, state, pincode, deleted FROM addresses where id = ?";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, addressId);
    ResultSet result = statement.executeQuery();

    if (result.next()) {
      Address address = new Address(result.getInt(1), result.getString(2), result.getString(3),
          result.getString(4), result.getString(5), result.getString(6), result.getInt(7));
      return address;
    } else {
      throw new SQLException("No address for Id");
    }
  }
}
