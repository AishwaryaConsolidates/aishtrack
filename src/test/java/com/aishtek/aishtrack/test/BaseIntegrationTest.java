package com.aishtek.aishtrack.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import com.aishtek.aishtrack.beans.Address;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.beans.ServiceReport;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.dao.AddressDAO;
import com.aishtek.aishtrack.dao.CustomerDAO;
import com.aishtek.aishtrack.dao.PersonDAO;
import com.aishtek.aishtrack.dao.ServiceReportDAO;
import com.aishtek.aishtrack.dao.TechnicianDAO;
import com.aishtek.aishtrack.dao.WorkOrderDAO;

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

  public int createTestWorkOrder(Connection connection, int customerId, int personId)
      throws SQLException {
    if (customerId == 0) {
      customerId = createTestCustomer(connection);
    }
    if (personId == 0) {
      personId = createTestPerson(connection);
    }
    WorkOrder workOrder = new WorkOrder(customerId, personId, "installation",
        "install sonething notes", 1, 1, "brand a",
        "model10", "serno1001", "partno101", createTestAddress(connection));

    return WorkOrderDAO.create(connection, workOrder);
  }

  public int createTestServiceReport(Connection connection, int customerId, int personId,
      int addressId) throws SQLException {
    if (customerId == 0) {
      customerId = createTestCustomer(connection);
    }
    if (personId == 0) {
      personId = createTestPerson(connection);
    }
    if (addressId == 0) {
      addressId = createTestAddress(connection);
    }
    ServiceReport serviceReport = new ServiceReport(0, "588bc7ff-8aeb-4cf8-94b1-04cb294a840c",
        customerId, addressId, personId, new Date(), "created", new Date(), "brand101", "model111",
        "serialno101", 5, "some notes", 0, "installation");
    return ServiceReportDAO.create(connection,
        createTestWorkOrder(connection, customerId, personId),
        serviceReport);
  }

  public int createTestTechnician(Connection connection) throws SQLException {
    return TechnicianDAO.create(connection, createTestPerson(connection));
  }

  public static Timestamp currentTimestamp() {
    return new Timestamp(new Date().getTime());
  }
}
