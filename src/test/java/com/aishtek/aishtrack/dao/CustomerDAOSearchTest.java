package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class CustomerDAOSearchTest extends BaseIntegrationTest {

  @Test
  public void searchSearchesByCustomerNameNickName() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);

      ArrayList<Customer> customers = CustomerDAO.searchFor(connection, "Bajji");

      assertEquals(customers.size(), 1);
      assertEquals(customers.get(0).getId(), customerId);
      assertEquals(customers.get(0).getName(), "Bajji Corner");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void searchDoesAPartialMatch() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);

      ArrayList<Customer> customers = CustomerDAO.searchFor(connection, "ajj");

      assertEquals(customers.size(), 1);
      assertEquals(customers.get(0).getId(), customerId);
      assertEquals(customers.get(0).getName(), "Bajji Corner");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void searchDoesNotReturnIfNameDoesntMatch() throws SQLException {
    try (Connection connection = getConnection()) {
      createTestCustomer(connection);

      ArrayList<Customer> customers = CustomerDAO.searchFor(connection, "Burgi");

      assertEquals(customers.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void searchDoesNotReturnDeletedCustomers() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      Customer customer = CustomerDAO.findById(connection, customerId);
      customer.setDeleted(1);
      CustomerDAO.update(connection, customer);

      ArrayList<Customer> customers = CustomerDAO.searchFor(connection, "Bajji");

      assertEquals(customers.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
