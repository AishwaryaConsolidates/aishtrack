package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Technician;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class TechnicianDAOSearchTest extends BaseIntegrationTest {

  private int technicianId1;
  private int technicianId2;

  @Test
  public void searchRetunsAllTechnicians() throws SQLException {
    try (Connection connection = getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("delete from technicians");
      preparedStatement.executeUpdate();

      technicianId1 = createTestTechnician(connection);
      technicianId2 = createTestTechnician(connection);

      ArrayList<Technician> technicians = TechnicianDAO.searchFor(connection);

      assertEquals(technicians.size(), 2);
      assertEquals(technicians.get(0).getId(), technicianId1);
      assertEquals(technicians.get(1).getId(), technicianId2);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void searchDoesNotRetunDeletedTechnicians() throws SQLException {
    try (Connection connection = getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("delete from technicians");
      preparedStatement.executeUpdate();

      technicianId1 = createTestTechnician(connection);
      technicianId2 = createTestTechnician(connection);

      TechnicianDAO.delete(connection, technicianId1);
      ArrayList<Technician> technicians = TechnicianDAO.searchFor(connection);

      assertEquals(technicians.size(), 1);
      assertEquals(technicians.get(0).getId(), technicianId2);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
