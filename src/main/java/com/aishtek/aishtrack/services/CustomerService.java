package com.aishtek.aishtrack.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.dao.CustomerDAO;

public class CustomerService extends BaseService {

  public int createCustomer(Connection connection, String name, String nickName,
      String gstIN, String street, String area, String city, String state, String pincode,
      String designation, String firstName, String lastName, String email, String phone,
      String mobile, String alternatePhone) throws SQLException {

      try {
        int addressId =
          (new AddressService()).createAddress(connection, street, area, city, state, pincode);

        Customer customer = new Customer(0, name, nickName, addressId, 0, gstIN);
        int customerId = CustomerDAO.create(connection, customer);

      createContactPerson(connection, customerId, firstName, lastName, email, phone, designation,
          mobile, alternatePhone);

        return customerId;
      } catch (Exception e) {
        connection.rollback();
        throw e;
      }
  }

  public int createContactPerson(Connection connection, int customerId, String firstName,
      String lastName, String email, String phone, String designation, String mobile,
      String alternatePhone)
      throws SQLException {
    Person person =
        new Person(firstName, lastName, designation, phone, email, mobile, alternatePhone);
    return CustomerDAO.createContactPerson(connection, customerId, person);
  }

  public ArrayList<HashMap<String, String>> getContactPersons(Connection connection,
      int customerId) throws SQLException {
    return CustomerDAO.getContactPersons(connection, customerId);
  }
}