package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.utils.Util;

public class CustomerDAO extends BaseDAO {
  public static Customer findById(Connection connection, int customerId) throws SQLException {
      String sql =
        "SELECT id, name, nick_name, address_id, contact_person_id, deleted, gst_in FROM customers where id = ?";

      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, customerId);
      ResultSet result = statement.executeQuery();

      if (result.next()) {
        Customer customer = new Customer(result.getInt(1), result.getString(2), result.getString(3),
          result.getInt(4), result.getInt(5), result.getInt(6), result.getString(7));
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

  public static void update(Connection connection, Customer customer) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "update customers set name =?, nick_name =?, deleted = ? where id = ?");
    preparedStatement.setString(1, customer.getName());
    preparedStatement.setString(2, customer.getNickName());
    preparedStatement.setInt(3, customer.getDeleted());
    preparedStatement.setInt(4, customer.getId());
    preparedStatement.executeUpdate();
  }

  public static ArrayList<NameId> findCustomerPersons(Connection connection, int customerId)
      throws SQLException {
    String sql =
        "SELECT cp.person_id, p.first_name, p.last_name FROM customer_persons cp, persons p where cp.person_id = p.id and customer_id = ?";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, customerId);
    ResultSet result = statement.executeQuery();

    ArrayList<NameId> persons = new ArrayList<NameId>();
    while (result.next()) {
      persons.add(new NameId(result.getInt(1), (result.getString(2) + " " + result.getString(3))));
    }
    return persons;
  }

  public static ArrayList<Customer> searchFor(Connection connection, String name)
      throws SQLException {
    String sql = "SELECT id, name, nick_name FROM customers where deleted = 0 ";

    if (!Util.isNullOrEmpty(name)) {
      sql += " and (name ilike ? or nick_name ilike ?) ";
    }

    sql += " order by name asc ";
    PreparedStatement statement = connection.prepareStatement(sql);
    if (!Util.isNullOrEmpty(name)) {
      name = "%" + name + "%";
      statement.setString(1, name);
      statement.setString(2, name);
    }

    ResultSet result = statement.executeQuery();

    ArrayList<Customer> customers = new ArrayList<Customer>();
    while (result.next()) {
      customers.add(new Customer(result.getInt(1), result.getString(2), result.getString(3)));
    }
    return customers;
  }

  public static int createContactPerson(Connection connection, int customerId, Person person)
      throws SQLException {

    int personId = PersonDAO.create(connection, person);
    PreparedStatement preparedStatement = connection
        .prepareStatement("insert into customer_persons (customer_id, person_id) values(?, ?)");
    preparedStatement.setInt(1, customerId);
    preparedStatement.setInt(2, personId);
    preparedStatement.executeUpdate();

    return personId;
  }

  public static ArrayList<HashMap<String, String>> getContactPersons(Connection connection,
      int customerId) throws SQLException {
    String sql =
        "SELECT cp.id, p.first_name, p.last_name, p.designation, p.phone, p.email, p.mobile, p.alternate_phone "
        + " from customer_persons cp inner join persons p on cp.person_id = p.id "
        + " where cp.customer_id = ? " + " order by p.last_name, p.first_name";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, customerId);

    ResultSet result = statement.executeQuery();

    ArrayList<HashMap<String, String>> contactPersons = new ArrayList<HashMap<String, String>>();
    while (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("firstName", result.getString(2));
      hashMap.put("lastName", result.getString(3));
      hashMap.put("designation", result.getString(4));
      hashMap.put("phone", result.getString(5));
      hashMap.put("email", result.getString(6));
      hashMap.put("mobile", result.getString(7));
      hashMap.put("alternatePhone", result.getString(8));
      contactPersons.add(hashMap);
    }
    return contactPersons;
  }
}
