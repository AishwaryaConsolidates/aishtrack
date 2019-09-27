package com.aishtek.aishtrack.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import com.aishtek.aishtrack.beans.Address;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.dao.AddressDAO;
import com.aishtek.aishtrack.dao.CustomerDAO;
import com.aishtek.aishtrack.dao.PersonDAO;
import com.aishtek.aishtrack.dao.TechnicianDAO;

public class BaseIntegrationTest {

  public Connection getConnection() throws SQLException {
    Connection connection =
        DriverManager.getConnection("jdbc:postgresql://localhost/aishtek_test", "adarsh",
        "adarsh");
    connection.setAutoCommit(false);
    return connection;
  }

  public int createTestPerson(Connection connection) throws SQLException {
    return PersonDAO.create(connection,
        new Person("Asterix", "Gaul", "Troubleshooter", "asterix@aishtek.tst", "9999999999",
            "8888888888", "7777777777"));
  }

  public int createTestAddress(Connection connection) throws SQLException {
    return AddressDAO.create(connection,
        new Address("Food St.", "VV Puram", "Blore", "KTKA", "560004"));
  }

  public int createTestCustomer(Connection connection) throws SQLException {
    return CustomerDAO.create(connection,
        new Customer("Bajji Corner", "Bajji", createTestAddress(connection),
            createTestPerson(connection)));
  }

  // public int createTestWorkOrder(Connection connection) throws SQLException {
  // return WorkOrderDAO.create(connection,
  // new WorkOrder(createTestCustomer(connection), "Type 2", "Notify this"));
  // }

  public int createTestTechnician(Connection connection) throws SQLException {
    return TechnicianDAO.create(connection, createTestPerson(connection));
  }

  public static Timestamp currentTimestamp() {
    return new Timestamp(new Date().getTime());
  }
}
