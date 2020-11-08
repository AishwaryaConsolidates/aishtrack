package com.aishtek.aishtrack.services;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.dao.CustomerDAO;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class CustomerServiceTest extends BaseIntegrationTest {

  String firstName = "Kapil";
  String lastName = "Dev";
  String email = "email@email.tst";
  String phone = "1231231234";
  String designation = "Bowler";
  String mobile = "2342342343";
  String alternatePhone = "4444444444";
  String name = "Customer A";
  String nickName = "CustA";
  String gstIN = "gstA";
  String street = "Street 1";
  String area = "Area 52";
  String city = "City of God";
  String state = "Stateless";
  String pincode = "54545";

  @Test
  public void itCreatesACustomer() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId =
          (new CustomerService()).createCustomer(connection, name, nickName, gstIN,
          street, area, city, state, pincode, designation, firstName, lastName, email, phone,
          mobile, alternatePhone);

      // creates customer
      Customer customer = CustomerDAO.findById(connection, customerId);
      assertEquals(customer.getName(), "Customer A");
      assertEquals(customer.getNickName(), "CustA");
      assertEquals(customer.getGstIN(), "gstA");

      // creates address
      Map<String, String> addressMap = getLastRecordFromTable(connection, "addresses");
      assertEquals(addressMap.get("id"), customer.getAddressId() + "");
      assertEquals(addressMap.get("area"), area);
      assertEquals(addressMap.get("city"), city);
      assertEquals(addressMap.get("state"), state);
      assertEquals(addressMap.get("pincode"), pincode);

      // creates contact person
      Map<String, String> contactPersonMap = getLastRecordFromTable(connection, "persons");
      ArrayList<HashMap<String, String>> contactPersons =
          CustomerDAO.getContactPersons(connection, customerId);
      
      assertEquals(contactPersons.get(0).get("firstName"), contactPersonMap.get("first_name"));
      assertEquals(contactPersons.get(0).get("lastName"), contactPersonMap.get("last_name"));
      assertEquals(contactPersons.get(0).get("email"), contactPersonMap.get("email"));
      assertEquals(contactPersons.get(0).get("phone"), contactPersonMap.get("phone"));
      assertEquals(contactPersons.get(0).get("designation"), contactPersonMap.get("designation"));

      connection.rollback();
    } catch (Exception e) {
      e.printStackTrace();
      assertEquals(true, false);
    }
  }

  @Test
  public void itcreatesAContactPerson() throws IOException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);

      (new CustomerService()).createContactPerson(connection, customerId, firstName, lastName,
          email, phone,
          designation, mobile, alternatePhone);

      ArrayList<HashMap<String, String>> contactPersons =
          CustomerDAO.getContactPersons(connection, customerId);

      assertEquals(contactPersons.size(), 2);

      assertEquals(contactPersons.get(0).get("firstName"), "Kapil");
      assertEquals(contactPersons.get(0).get("lastName"), "Dev");
      assertEquals(contactPersons.get(0).get("designation"), "Bowler");
      assertEquals(contactPersons.get(0).get("phone"), "1231231234");
      assertEquals(contactPersons.get(0).get("email"), "email@email.tst");
      assertEquals(contactPersons.get(0).get("mobile"), "2342342343");
      assertEquals(contactPersons.get(0).get("alternatePhone"), "4444444444");
    } catch (Exception e) {
      e.printStackTrace();
      assertEquals(true, false);
    }
  }

  @Test
  public void getContactPersonsTest() throws IOException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);

      (new CustomerService()).createContactPerson(connection, customerId, firstName, lastName,
          email, phone, designation, mobile, alternatePhone);

      ArrayList<HashMap<String, String>> contactPersons =
          (new CustomerService()).getContactPersons(connection, customerId);

      assertEquals(contactPersons.size(), 2);

      assertEquals(contactPersons.get(0).get("firstName"), "Kapil");
      assertEquals(contactPersons.get(0).get("lastName"), "Dev");
      assertEquals(contactPersons.get(0).get("designation"), "Bowler");
      assertEquals(contactPersons.get(0).get("phone"), "1231231234");
      assertEquals(contactPersons.get(0).get("email"), "email@email.tst");
      assertEquals(contactPersons.get(0).get("mobile"), "2342342343");
      assertEquals(contactPersons.get(0).get("alternatePhone"), "4444444444");

      assertEquals(contactPersons.get(1).get("firstName"), "Asterix");
      assertEquals(contactPersons.get(1).get("lastName"), "Gaul");
      assertEquals(contactPersons.get(1).get("designation"), "Troubleshooter");
      assertEquals(contactPersons.get(1).get("phone"), "9999999999");
      assertEquals(contactPersons.get(1).get("email"), "asterix@aishtek.tst");
      assertEquals(contactPersons.get(1).get("mobile"), "8888888888");
      assertEquals(contactPersons.get(1).get("alternatePhone"), "7777777777");
    } catch (Exception e) {
      e.printStackTrace();
      assertEquals(true, false);
    }
  }
}
