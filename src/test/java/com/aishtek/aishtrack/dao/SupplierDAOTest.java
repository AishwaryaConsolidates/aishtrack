package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Address;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.beans.Supplier;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class SupplierDAOTest extends BaseIntegrationTest {

  @Test
  public void searchForReturnsTheRightResults() throws SQLException {
    try (Connection connection = getConnection()) {
      int supplierId = createSupplier(connection, "domestic");

      ArrayList<NameId> results = SupplierDAO.searchFor(connection, null, null);
      assertEquals(results.size(), 1);

      results = SupplierDAO.searchFor(connection, null, "Supplier");
      assertEquals(results.size(), 1);

      results = SupplierDAO.searchFor(connection, "domestic", null);
      assertEquals(results.size(), 1);

      results = SupplierDAO.searchFor(connection, "domestica", null);
      assertEquals(results.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }

  }

  @Test
  public void findByTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int supplierId = createSupplier(connection, "domestic");

      Supplier supplier = SupplierDAO.findById(connection, supplierId);
      assertEquals(supplier.getId(), supplierId);
      assertEquals(supplier.getType(), "domestic");
      assertEquals(supplier.getName().substring(0, 8), "Supplier");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getAddressesForTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int supplierId = createSupplier(connection, "domestic");
      int addressId = createTestAddress(connection);
      Address address = AddressDAO.findById(connection, addressId);

      SupplierDAO.createSupplierAddress(connection, supplierId, addressId);

      ArrayList<NameId> addresses = SupplierDAO.getAddressesFor(connection, supplierId);
      assertEquals(addresses.size(), 1);
      assertEquals(addresses.get(0).getName(),
          (address.getArea() + " (" + address.getCity() + ")"));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getAddressesTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int supplierId = createSupplier(connection, "domestic");
      int addressId = createTestAddress(connection);
      Address address = AddressDAO.findById(connection, addressId);

      SupplierDAO.createSupplierAddress(connection, supplierId, addressId);

      ArrayList<HashMap<String, String>> addresses =
          SupplierDAO.getAddresses(connection, supplierId);
      assertEquals(addresses.size(), 1);
      assertEquals(addresses.get(0).get("id"), "" + address.getId());
      assertEquals(addresses.get(0).get("area"), address.getArea());
      assertEquals(addresses.get(0).get("city"), address.getCity());
      assertEquals(addresses.get(0).get("street"), address.getStreet());
      assertEquals(addresses.get(0).get("state"), address.getState());
      assertEquals(addresses.get(0).get("pincode"), address.getPincode());

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void createTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int supplierId = SupplierDAO.create(connection, "Supplier 1947", "domestic");

      Supplier supplier = SupplierDAO.findById(connection, supplierId);
      assertEquals(supplier.getId(), supplierId);
      assertEquals(supplier.getType(), "domestic");
      assertEquals(supplier.getName(), "Supplier 1947");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void updateTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int supplierId = SupplierDAO.create(connection, "Supplier 1947", "domestic");

      SupplierDAO.update(connection, "Supplier 2020", "local", supplierId);
      Supplier supplier = SupplierDAO.findById(connection, supplierId);

      assertEquals(supplier.getId(), supplierId);
      assertEquals(supplier.getType(), "local");
      assertEquals(supplier.getName(), "Supplier 2020");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void createSupplierBankAccountTest() throws Exception {
    try (Connection connection = getConnection()) {
      int supplierId = createSupplier(connection, "domestic");
      int bankAccountId = createBankAccount(connection);

      SupplierDAO.createSupplierBankAccount(connection, supplierId, bankAccountId);

      ArrayList<NameId> bankAccounts = BankAccountDAO.forSupplier(connection, supplierId);

      assertEquals(bankAccounts.size(), 1);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
