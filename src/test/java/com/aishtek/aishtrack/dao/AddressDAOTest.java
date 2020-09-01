package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Address;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class AddressDAOTest extends BaseIntegrationTest {

  private String street = "Street 27";
  private String area = "Area 54";
  private String city = "Fortune City";
  private String state = "Liquid";
  private String pincode = "560004";

  @Test
  public void testCreateAndFindById() throws SQLException {
    try (Connection connection = getConnection()) {
      Address address = new Address(street, area, city, state, pincode);
      int addressId = AddressDAO.create(connection, address);

      address = AddressDAO.findById(connection, addressId);

      assertEquals(address.getStreet(), street);
      assertEquals(address.getArea(), area);
      assertEquals(address.getCity(), city);
      assertEquals(address.getState(), state);
      assertEquals(address.getPincode(), pincode);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getAishwaryaAddressReturnsTheRightAddress() throws SQLException {
    try (Connection connection = getConnection()) {
      Address address = new Address(street, area, city, state, pincode);
      AddressDAO.createAishwaryaAddress(connection, address, yesterday(), tomorrow());

      Address aishwaryaAddress = AddressDAO.getAishwaryaAddress(connection);

      assertEquals(aishwaryaAddress.getStreet(), street);
      assertEquals(aishwaryaAddress.getArea(), area);
      assertEquals(aishwaryaAddress.getCity(), city);
      assertEquals(aishwaryaAddress.getState(), state);
      assertEquals(aishwaryaAddress.getPincode(), pincode);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getAishwaryaAddressDoesNotReturnOldAddresses() throws SQLException {
    Connection connection = getConnection();
    try {
      Address address = new Address(street, area, city, state, pincode);
      AddressDAO.createAishwaryaAddress(connection, address, yesterday(), yesterday());

      AddressDAO.getAishwaryaAddress(connection);
      assert (false);
    } catch (SQLException e) {
      assertEquals(e.getMessage(), "No current Aishwarya address in the database");
      assert (true);
    } finally {
      connection.rollback();
    }
  }
}
