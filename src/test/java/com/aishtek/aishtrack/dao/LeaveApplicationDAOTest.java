package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import com.aishtek.aishtrack.beans.LeaveApplication;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class LeaveApplicationDAOTest extends BaseIntegrationTest {

  @Test
  public void saveAndCreateTest() throws Exception {
    try (Connection connection = getConnection()) {
      int technicianId = createTestTechnician(connection);
      
      LeaveApplication leaveApplication = new LeaveApplication(0, technicianId, yesterday(), tomorrow(), "COVID 19", "", "", null, 0);
      
      int leaveApplicationId = LeaveApplicationDAO.save(connection, leaveApplication);
      LeaveApplication createdLeaveApplication =
          LeaveApplicationDAO.findById(connection, leaveApplicationId);

      assertEquals(createdLeaveApplication.getId(), leaveApplicationId);
      assertEquals(formatter.format(createdLeaveApplication.getFromDate()),
          formatter.format(yesterday()));
      assertEquals(formatter.format(createdLeaveApplication.getToDate()),
          formatter.format(tomorrow()));
      assertEquals(createdLeaveApplication.getReason(), "COVID 19");
      assertEquals(createdLeaveApplication.getStatus(), "processing");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void saveAndUpdateTest() throws Exception {
    try (Connection connection = getConnection()) {
      int technicianId = createTestTechnician(connection);

      LeaveApplication leaveApplication = new LeaveApplication(0, technicianId, yesterday(),
          tomorrow(), "COVID 19", "", "", null, 0);

      int leaveApplicationId = LeaveApplicationDAO.save(connection, leaveApplication);
      LeaveApplication createdLeaveApplication =
          LeaveApplicationDAO.findById(connection, leaveApplicationId);

      createdLeaveApplication.setId(leaveApplicationId);
      createdLeaveApplication.setFromDate(today());
      createdLeaveApplication.setToDate(today());
      createdLeaveApplication.setReason("Rainy Day");
      createdLeaveApplication.setSignature("Signature");
      createdLeaveApplication.setSignatureDate(today());
      createdLeaveApplication.setStatus("approved");

      leaveApplicationId = LeaveApplicationDAO.save(connection, createdLeaveApplication);
      LeaveApplication updatedLeaveApplication =
          LeaveApplicationDAO.findById(connection, leaveApplicationId);

      assertEquals(updatedLeaveApplication.getId(), leaveApplicationId);
      assertEquals(formatter.format(updatedLeaveApplication.getFromDate()),
          formatter.format(today()));
      assertEquals(formatter.format(updatedLeaveApplication.getToDate()),
          formatter.format(today()));
      assertEquals(updatedLeaveApplication.getReason(), "Rainy Day");
      assertEquals(updatedLeaveApplication.getSignature(), "Signature");
      assertEquals(updatedLeaveApplication.getStatus(), "approved");
      assertEquals(formatter.format(updatedLeaveApplication.getSignatureDate()),
          formatter.format(today()));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void searchForTest() throws Exception {
    try (Connection connection = getConnection()) {
      int technicianId = createTestTechnician(connection);

      LeaveApplication leaveApplication =
          new LeaveApplication(0, technicianId, today(),
          tomorrow(), "COVID 19", "", "", null, 0);

      int leaveApplicationId = LeaveApplicationDAO.save(connection, leaveApplication);
      ArrayList<HashMap<String, String>> results =
          LeaveApplicationDAO.searchFor(connection, technicianId, yesterday(), tomorrow(),
              "processing");
      assertEquals(results.size(), 1);
      assertEquals(results.get(0).get("id"), "" + leaveApplicationId);

      results = LeaveApplicationDAO.searchFor(connection, technicianId + 1, yesterday(), tomorrow(),
          "processing");
      assertEquals(results.size(), 0);

      results =
          LeaveApplicationDAO.searchFor(connection, technicianId, tomorrow(), tomorrow(),
              "processing");
      assertEquals(results.size(), 0);

      results =
          LeaveApplicationDAO.searchFor(connection, technicianId, yesterday(), yesterday(),
              "processing");
      assertEquals(results.size(), 0);

      results = LeaveApplicationDAO.searchFor(connection, technicianId, yesterday(), tomorrow(),
          "some status");
      assertEquals(results.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
