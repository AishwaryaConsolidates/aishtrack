package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class CustomerDAOTest extends BaseIntegrationTest {

  @Test
  public void createSavesTheCustomer() throws SQLException {
    try (Connection connection = getConnection()) {
      int addressId = createTestAddress(connection);
      Customer customer = new Customer(0, "Cafe Au Lait", "Cafe", addressId, 0, "GSTIN");
      int customerId = CustomerDAO.create(connection, customer);
      customer = CustomerDAO.findById(connection, customerId);

      assertEquals(customer.getName(), "Cafe Au Lait");
      assertEquals(customer.getNickName(), "Cafe");
      assertEquals(customer.getAddressId(), addressId);
      assertEquals(customer.getGstIN(), "GSTIN");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void testCreateContactPerson() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = CustomerDAO.create(connection,
          new Customer(0, "Bajji Corner", "Bajji", createTestAddress(connection), 0, "GSTIN"));
      CustomerDAO.createContactPerson(connection, customerId, new Person("Asterix", "Gaul",
          "Troubleshooter", "asterix@aishtek.tst", "9999999999", "8888888888", "7777777777"));

      ArrayList<HashMap<String, String>> persons =
          CustomerDAO.getContactPersons(connection, customerId);
      
      assertEquals(persons.size(), 1);
      assertEquals(persons.get(0).get("firstName"), "Asterix");
      assertEquals(persons.get(0).get("lastName"), "Gaul");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void testFindByIdFindsTheCustomer() throws SQLException {
    try (Connection connection = getConnection()) {
      int addressId = createTestAddress(connection);
      Customer customer = new Customer(0, "Cafe Au Lait", "Cafe", addressId, 0, "GSTIN");
      int customerId = CustomerDAO.create(connection, customer);
      customer = CustomerDAO.findById(connection, customerId);

      assertEquals(customer.getName(), "Cafe Au Lait");
      assertEquals(customer.getNickName(), "Cafe");
      assertEquals(customer.getAddressId(), addressId);
      assertEquals(customer.getGstIN(), "GSTIN");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void updateSavesTheCustomer() throws SQLException {
    try (Connection connection = getConnection()) {
      int addressId = createTestAddress(connection);
      Customer customer = new Customer(0, "Cafe Au Lait", "Cafe", addressId, 0, "GSTIN");
      int customerId = CustomerDAO.create(connection, customer);
      customer = CustomerDAO.findById(connection, customerId);

      customer.setName("Cafe Au Lait 21");
      customer.setNickName("Cafe 42");
      customer.setGstIN("GSTIN 2");

      CustomerDAO.update(connection, customer);
      customer = CustomerDAO.findById(connection, customerId);

      assertEquals(customer.getName(), "Cafe Au Lait 21");
      assertEquals(customer.getNickName(), "Cafe 42");
      assertEquals(customer.getGstIN(), "GSTIN 2");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void testFindCustomerPersons() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);

      int personId1 =
          CustomerDAO.createContactPerson(connection, customerId, new Person("Obelix", "Gaul",
              "Troubleshooter", "obelix@aishtek.tst", "9999999999", "8888888888", "7777777777"));

      ArrayList<NameId> persons = CustomerDAO.findCustomerPersons(connection, customerId);

      assertEquals(persons.size(), 2);
      assertEquals(persons.get(0).getName(), "Asterix Gaul");
      assertEquals(persons.get(1).getName(), "Obelix Gaul");
      assertEquals(persons.get(1).getId(), personId1);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void testCreateGetContactPersons() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);

      CustomerDAO.createContactPerson(connection, customerId, new Person("Obelix", "Gaul",
          "Menhir Maker", "1010101010", "obelix@aishtek.tst", "20202020202", "303030303030"));

      ArrayList<HashMap<String, String>> contactPersons =
          CustomerDAO.getContactPersons(connection, customerId);
      assertEquals(contactPersons.size(), 2);

      assertEquals(contactPersons.get(1).get("firstName"), "Obelix");
      assertEquals(contactPersons.get(1).get("lastName"), "Gaul");
      assertEquals(contactPersons.get(1).get("designation"), "Menhir Maker");
      assertEquals(contactPersons.get(1).get("phone"), "1010101010");
      assertEquals(contactPersons.get(1).get("email"), "obelix@aishtek.tst");
      assertEquals(contactPersons.get(1).get("mobile"), "20202020202");
      assertEquals(contactPersons.get(1).get("alternatePhone"), "303030303030");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
