package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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

  public static Address getAishwaryaAddress(Connection connection) throws SQLException {
    String sql =
        "SELECT a.id, street, area, city, state, pincode, deleted FROM addresses a, aishwarya_addresses aa "
            + "where a.id = aa.address_id and aa.start_date <= NOW() and aa.end_date >= NOW()";

    PreparedStatement statement = connection.prepareStatement(sql);
    ResultSet result = statement.executeQuery();

    if (result.next()) {
      Address address = new Address(result.getInt(1), result.getString(2), result.getString(3),
          result.getString(4), result.getString(5), result.getString(6), result.getInt(7));
      return address;
    } else {
      throw new SQLException("No current Aishwarya address in the database");
    }
  }

  public static int createAishwaryaAddress(Connection connection, Address address, Date startDate,
      Date endDate) throws SQLException {
    int addressId = create(connection, address);

    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into aishwarya_addresses (address_id, start_date, end_date) values (?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);
    preparedStatement.setInt(1, addressId);
    preparedStatement.setTimestamp(2, timestampFor(startDate));
    preparedStatement.setTimestamp(3, timestampFor(endDate));

    int affectedRows = preparedStatement.executeUpdate();
    if (affectedRows == 0) {
      throw new SQLException("Creating address failed, no rows affected.");
    }
    try (ResultSet result = preparedStatement.getGeneratedKeys()) {
      if (result.next()) {
        return result.getInt(1);
      } else {
        throw new SQLException("Aishwarya Address Id not generted");
      }
    } catch (SQLException sqle) {
      throw sqle;
    }
  }
}
