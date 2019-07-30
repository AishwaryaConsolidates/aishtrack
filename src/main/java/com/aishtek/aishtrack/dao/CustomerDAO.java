package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.aishtek.aishtrack.beans.Customer;

public class CustomerDAO extends BaseDAO {
  public static Customer findById(Connection connection, int customerId) throws SQLException {
      String sql =
          "SELECT id, name, nick_name, address_id, contact_person_id, deleted FROM customers where id = ?";

      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, customerId);
      ResultSet result = statement.executeQuery();

      if (result.next()) {
        Customer customer = new Customer(result.getInt(1), result.getString(2), result.getString(3),
            result.getInt(4), result.getInt(5), result.getInt(6));
        return customer;
    } else {
      throw new SQLException("No customer for Id");
      }
  }

  public static int create(Connection connection, Customer customer) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into customers (name, nick_name, address_id, contact_person_id) values(?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);
    preparedStatement.setString(1, customer.getName());
    preparedStatement.setString(2, customer.getNickName());
    preparedStatement.setInt(3, customer.getAddressId());
    preparedStatement.setInt(4, customer.getContactPersonId());
    preparedStatement.executeUpdate();

    ResultSet result = preparedStatement.getGeneratedKeys();
    if (result.next()) {
      int customerId = result.getInt(1);

      preparedStatement = connection.prepareStatement(
          "insert into customer_persons (customer_id, person_id) values(?, ?)",
          PreparedStatement.RETURN_GENERATED_KEYS);
      preparedStatement.setInt(1, customerId);
      preparedStatement.setInt(2, customer.getContactPersonId());
      preparedStatement.executeUpdate();

      return customerId;
    } else {
      throw new SQLException("Service Report ID not generted");
    }
  }

  public static ArrayList<Integer> findCustomerPersons(Connection connection, int customerId)
      throws SQLException {
    String sql = "SELECT person_id FROM customer_persons where customer_id = ?";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, customerId);
    ResultSet result = statement.executeQuery();

    ArrayList<Integer> personIds = new ArrayList<Integer>();
    while (result.next()) {
      personIds.add(result.getInt(1));
    }
    return personIds;
  }
}
