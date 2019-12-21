package com.aishtek.aishtrack.services;

import java.sql.Connection;
import java.sql.SQLException;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.dao.CustomerDAO;

public class CustomerService {

  public int createContactPerson(Connection connection, int customerId, String firstName,
      String lastName, String email, String phone, String designation, String mobile,
      String alternatePhone)
      throws SQLException {
    Person person =
        new Person(firstName, lastName, designation, phone, email, mobile, alternatePhone);
    return CustomerDAO.createContactPerson(connection, customerId, person);
  }
}