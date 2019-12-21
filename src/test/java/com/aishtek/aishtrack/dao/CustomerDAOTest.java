package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class CustomerDAOTest extends BaseIntegrationTest {

  private String name = "Cafe Au Lait";
  private String nickName = "Cafe";

  @Test
  public void createSavesTheCustomer() throws SQLException {
    try (Connection connection = getConnection()) {
      int addressId = createTestAddress(connection);
      Customer customer = new Customer(0, name, nickName, addressId, 0, "GSTIN");
      int customerId = CustomerDAO.create(connection, customer);
      CustomerDAO.createContactPerson(connection, customerId, new Person("Asterix", "Gaul",
          "Troubleshooter", "asterix@aishtek.tst", "9999999999", "8888888888", "7777777777"));
      customer = CustomerDAO.findById(connection, customerId);

      assertEquals(customer.getName(), name);
      assertEquals(customer.getNickName(), nickName);
      assertEquals(customer.getAddressId(), addressId);

      // ArrayList<NameId> personIds = CustomerDAO.findCustomerPersons(connection, customerId);
      // assertEquals(personIds.size(), 1);
      // assertEquals((Integer) personIds.get(0), new Integer(personId));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }

  }
}
