package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class PersonDAOTest extends BaseIntegrationTest {

  private String firstName = "Dosa";
  private String lastName = "Champ";
  private String designation = "Chef";
  private String email = "chef@aishtek.tst";
  private String phone = "9999999999";

  @Test
  public void createSavesThePerson() throws SQLException {
    try (Connection connection = getConnection()) {
      Person person = new Person(firstName, lastName, designation, email, phone);
      int personId = PersonDAO.create(connection, person);

      person = PersonDAO.findById(connection, personId);

      assertEquals(person.getFirstName(), firstName);
      assertEquals(person.getLastName(), lastName);
      assertEquals(person.getDesignation(), designation);
      assertEquals(person.getEmail(), email);
      assertEquals(person.getPhone(), phone);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }

  }
}
