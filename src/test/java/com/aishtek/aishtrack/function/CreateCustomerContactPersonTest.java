package com.aishtek.aishtrack.function;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.dao.CustomerDAO;
import com.aishtek.aishtrack.dao.PersonDAO;
import com.aishtek.aishtrack.test.BaseIntegrationTest;
import com.amazonaws.services.lambda.runtime.Context;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class CreateCustomerContactPersonTest extends BaseIntegrationTest {

  private int customerId;
  private String firstName = "xGetafix";
  private String lastName = "XDruid";
  private String email = "getafix@gaul.com";
  private String phone = "1112223333";
  private String designation = "The Druid";
  private String mobile = "2223334444";
  private String alternatePhone = "3334445555";

  @Mocked
  Context context;

  @Test
  public void itCreatesAPerson() {
    try (Connection connection = getConnection()) {
      createArtifacts(connection);
      int personId =
          (new CreateCustomerContactPerson()).createContactPerson(connection, customerId, firstName,
          lastName, email, phone, designation, mobile, alternatePhone);

      Person person = PersonDAO.findById(connection, personId);

      assertEquals(person.getFirstName(), firstName);
      assertEquals(person.getLastName(), lastName);
      assertEquals(person.getEmail(), email);
      assertEquals(person.getPhone(), phone);
      assertEquals(person.getDesignation(), designation);
      assertEquals(person.getMobile(), mobile);
      assertEquals(person.getAlternatePhone(), alternatePhone);

      connection.rollback();
    } catch (SQLException e) {
      e.printStackTrace();
      assert (false);
    }
  }

  @Test
  public void itAssignsThePersonToTheClient() {
    try (Connection connection = getConnection()) {
      createArtifacts(connection);
      (new CreateCustomerContactPerson()).createContactPerson(connection, customerId,
          firstName, lastName, email, phone, designation, mobile, alternatePhone);

      ArrayList<HashMap<String, String>> contactPersons =
          CustomerDAO.getContactPersons(connection, customerId);

      assertEquals(contactPersons.size(), 2);

      connection.rollback();
    } catch (SQLException e) {
      e.printStackTrace();
      assert (false);
    }
  }

  @Test
  // TODO
  public void itReturnsAListOfContactPersons() {
    try (Connection connection = getConnection()) {
      createArtifacts(connection);


      connection.rollback();
    } catch (SQLException e) {
      e.printStackTrace();
      assert (false);
    }
  }

  private void createArtifacts(Connection connection) throws SQLException {
    customerId = this.createTestCustomer(connection);
  }
}
