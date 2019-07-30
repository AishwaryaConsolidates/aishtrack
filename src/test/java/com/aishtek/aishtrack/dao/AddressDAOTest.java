package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Address;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class AddressDAOTest extends BaseIntegrationTest {

  private String street;
  private String area = "Area 54";
  private String city = "Fortune City";
  private String state = "Liquid";
  private String pincode = "560004";

  @Test
  public void createSavesTheAddress() throws SQLException {
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
}
