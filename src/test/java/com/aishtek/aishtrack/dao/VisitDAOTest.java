package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Visit;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class VisitDAOTest extends BaseIntegrationTest {

  @Test
  public void createFindByIdTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int visitId = createTestVisit(connection, 0);

      Visit visit = VisitDAO.findById(connection, visitId);

      assertEquals(visit.getId(), visitId);
      assertEquals(visit.getComplaint(), "complaint");
      assertEquals(visit.getFindings(), "findings");
      assertEquals(visit.getWorkDone(), "workDone");
      assertEquals(visit.getCustomerRemarks(), "customerRemarks");
      assertEquals(formatter.format(visit.getVisitDate()), formatter.format(new Date()));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void updateTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int visitId = createTestVisit(connection, 0);

      Visit visit = VisitDAO.findById(connection, visitId);
      visit.setComplaint("complaint2");
      visit.setFindings("findings2");
      visit.setWorkDone("workDone2");
      visit.setCustomerRemarks("customerRemarks2");
      visit.setVisitDate(tomorrow());
      VisitDAO.update(connection, visit);
      visit = VisitDAO.findById(connection, visitId);

      assertEquals(visit.getId(), visitId);
      assertEquals(visit.getComplaint(), "complaint2");
      assertEquals(visit.getFindings(), "findings2");
      assertEquals(visit.getWorkDone(), "workDone2");
      assertEquals(visit.getCustomerRemarks(), "customerRemarks2");
      assertEquals(formatter.format(visit.getVisitDate()), formatter.format(tomorrow()));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getVisitsTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int visitId = createTestVisit(connection, 0);
      Visit visit = VisitDAO.findById(connection, visitId);
      visit.setComplaint("complaint2");
      visit.setFindings("findings2");
      visit.setWorkDone("workDone2");
      visit.setCustomerRemarks("customerRemarks2");
      visit.setVisitDate(tomorrow());
      int visitId2 = VisitDAO.create(connection, visit);

      ArrayList<HashMap<String, String>> visits =
          VisitDAO.getVisits(connection, visit.getServiceReportId());

      assertEquals(visits.size(), 2);
      assertEquals(visits.get(0).get("id"), "" + visitId2);
      assertEquals(visits.get(0).get("visitDate"), formatter.format(tomorrow()));
      assertEquals(visits.get(1).get("id"), "" + visitId);
      assertEquals(visits.get(1).get("visitDate"), formatter.format(today()));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getVisitDetailsTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int visitId = createTestVisit(connection, 0);
      Visit visit = VisitDAO.findById(connection, visitId);

      ArrayList<Visit> visits = VisitDAO.getVisitsDetails(connection, visit.getServiceReportId());
      assertEquals(visits.size(), 1);

      assertEquals(visits.get(0).getId(), visitId);
      assertEquals(visits.get(0).getComplaint(), "complaint");
      assertEquals(visits.get(0).getFindings(), "findings");
      assertEquals(visits.get(0).getWorkDone(), "workDone");
      assertEquals(visits.get(0).getCustomerRemarks(), "customerRemarks");
      assertEquals(formatter.format(visits.get(0).getVisitDate()), formatter.format(new Date()));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getVisitFilesTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int visitId = createTestVisit(connection, 0);
      int visitFileId = createTestVisitFile(connection, "someFile", "someLocation", visitId);

      ArrayList<HashMap<String, String>> visitFiles = VisitDAO.getVisitFiles(connection, visitId);

      assertEquals(visitFiles.size(), 1);

      assertEquals(visitFiles.get(0).get("id"), "" + visitFileId);
      assertEquals(visitFiles.get(0).get("name"), "someFile");
      assertEquals(visitFiles.get(0).get("location"), "someLocation");


      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }


  @Test
  public void getScoutingReportFilesTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int visitId = createTestVisit(connection, 0);
      Visit visit = VisitDAO.findById(connection, visitId);
      int visitFileId = createTestVisitFile(connection, "someFile", "someLocation", visitId);

      ArrayList<HashMap<String, String>> visitFiles =
          VisitDAO.getScoutingReportFiles(connection, visit.getServiceReportId());

      assertEquals(visitFiles.size(), 1);

      assertEquals(visitFiles.get(0).get("id"), "" + visitFileId);
      assertEquals(visitFiles.get(0).get("name"), "someFile");
      assertEquals(visitFiles.get(0).get("location"), "someLocation");


      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void deleteForWorkOrderTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int visitId = createTestVisit(connection, 0);
      Visit visit = VisitDAO.findById(connection, visitId);
      String serviceReportCode =
          ServiceReportDAO.getCodeForId(connection, visit.getServiceReportId());
      HashMap<String, String> serviceReport =
          ServiceReportDAO.findByCode(connection, serviceReportCode);

      VisitDAO.deleteForWorkOrder(connection, Integer.parseInt(serviceReport.get("workOrderId")));

      visit = VisitDAO.findById(connection, visitId);
      assertEquals(visit.getDeleted(), 1);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
