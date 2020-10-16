package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.beans.Technician;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class TechnicianDAOTest extends BaseIntegrationTest {

  @Test
  public void createAndFindByTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int personId = createTestPerson(connection);

      int technicianId = TechnicianDAO.create(connection, personId);
      Technician technician = TechnicianDAO.findById(connection, technicianId);

      assertEquals(technician.getPersonId(), personId);
      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getTechnicianIdForTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int personId = createTestPerson(connection);
      int technicianId = TechnicianDAO.create(connection, personId);
      int returnedTechnician = TechnicianDAO.getTechnicianIdFor(connection, "asterix@aishtek.tst");

      assertEquals(technicianId, returnedTechnician);
      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void deleteTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int personId = createTestPerson(connection);
      int technicianId = TechnicianDAO.create(connection, personId);
      TechnicianDAO.delete(connection, technicianId);

      Technician technician = TechnicianDAO.findById(connection, technicianId);
      Person person = PersonDAO.findById(connection, personId);

      assertEquals(technician.getDeleted(), 1);
      assertEquals(person.getDeleted(), 1);
      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getTechniciansForTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      int technicianId = createTestTechnician(connection);

      int serviceReportId = createTestServiceReport(connection, customerId, 0, 0);
      String serviceReportCode = ServiceReportDAO.getCodeForId(connection, serviceReportId);
      HashMap<String, String> serviceReport =
          ServiceReportDAO.findByCode(connection, serviceReportCode);
      int workOrderId = Integer.parseInt(serviceReport.get("workOrderId"));


      ArrayList<Integer> technicianIds = new ArrayList<Integer>();
      technicianIds.add(technicianId);

      ServiceReportDAO.assignTechniciansToServiceReport(connection, serviceReportId, technicianIds);
      WorkOrderDAO.markAsAssigned(connection, workOrderId);
      ArrayList<String> tecnicians = TechnicianDAO.getTechniciansFor(connection, 0, 0);
      assertEquals(tecnicians.get(0), "Asterix Gaul");



      ServiceReportDAO.assignTechniciansToServiceReport(connection, serviceReportId, technicianIds);
      tecnicians = TechnicianDAO.getTechniciansFor(connection, 0, 0);
      assertEquals(tecnicians.get(0), "Asterix Gaul");

      tecnicians = TechnicianDAO.getTechniciansFor(connection, workOrderId + 1, 0);
      assertEquals(tecnicians.size(), 0);

      tecnicians = TechnicianDAO.getTechniciansFor(connection, 0, serviceReportId + 1);
      assertEquals(tecnicians.size(), 0);

      tecnicians = TechnicianDAO.getTechniciansFor(connection, 0, serviceReportId);
      assertEquals(tecnicians.size(), 1);

      tecnicians = TechnicianDAO.getTechniciansFor(connection, workOrderId, 0);
      assertEquals(tecnicians.size(), 1);

      tecnicians = TechnicianDAO.getTechniciansFor(connection, workOrderId, serviceReportId);
      assertEquals(tecnicians.size(), 1);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getTechnicianIdsForTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      int technicianId = createTestTechnician(connection);
      int serviceReportId = createTestServiceReport(connection, customerId, 0, 0);

      ArrayList<Integer> technicianIds = new ArrayList<Integer>();
      technicianIds.add(technicianId);

      ServiceReportDAO.assignTechniciansToServiceReport(connection, serviceReportId, technicianIds);

      ArrayList<Integer> tecnicians =
          TechnicianDAO.getTechnicianIdsFor(connection, serviceReportId);
      assertEquals(tecnicians.get(0) + "", "" + technicianId);


      tecnicians = TechnicianDAO.getTechnicianIdsFor(connection, serviceReportId + 1);
      assertEquals(tecnicians.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
