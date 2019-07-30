package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class CustomerDAOTest extends BaseIntegrationTest {

  private String name = "Cafe Au Lait";
  private String nickName = "Cafe";

  @Test
  public void createSavesTheCustomer() throws SQLException {
    try (Connection connection = getConnection()) {
      int personId = createTestPerson(connection);
      int addressId = createTestAddress(connection);
      Customer customer =
          new Customer(name, nickName, addressId, personId);
      int customerId = CustomerDAO.create(connection, customer);

      customer = CustomerDAO.findById(connection, customerId);

      assertEquals(customer.getName(), name);
      assertEquals(customer.getNickName(), nickName);
      assertEquals(customer.getContactPersonId(), personId);
      assertEquals(customer.getAddressId(), addressId);

      ArrayList<Integer> personIds = CustomerDAO.findCustomerPersons(connection, customerId);
      assertEquals(personIds.size(), 1);
      assertEquals((Integer) personIds.get(0), new Integer(personId));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }

  }
}
