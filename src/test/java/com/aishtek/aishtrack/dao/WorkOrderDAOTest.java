package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.junit.Test;
import com.aishtek.aishtrack.beans.ServiceReport;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.test.BaseIntegrationTest;
import com.aishtek.aishtrack.utils.WorkStatus;

public class WorkOrderDAOTest extends BaseIntegrationTest {

  @Test
  public void createWorkOrderSavesTheWorkOrder() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      int personId = createTestPerson(connection);

      int workOrderId = createTestWorkOrder(connection, customerId, personId);
      WorkOrder workOrder = WorkOrderDAO.findById(connection, workOrderId);

      assertEquals(workOrder.getCustomerId() + "", "" + customerId);
      assertEquals(workOrder.getNotes(), "install sonething notes");
      assertEquals(workOrder.getType(), "installation");
      assertEquals(workOrder.getStatus(), WorkStatus.CREATED_STATUS);
      assertEquals(workOrder.getContactPersonId() + "", "" + personId);
      assertEquals(formatter.format(workOrder.getStatusDate()), formatter.format(today()));
      assertEquals(workOrder.getBrand(), "brand a");
      assertEquals(workOrder.getModel(), "model10");
      assertEquals(workOrder.getSerialNumber(), "serno1001");
      assertEquals(workOrder.getPartNumber(), "partno101");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void markAssignedUpdatesTheWorkOrder() throws SQLException {
    try (Connection connection = getConnection()) {
      int workOrderId = createTestWorkOrder(connection, 0, 0);
      WorkOrderDAO.markAsAssigned(connection, workOrderId);

      WorkOrder workOrder = WorkOrderDAO.findById(connection, workOrderId);
      assertEquals(workOrder.getStatus(), WorkStatus.ASSIGNED_STATUS);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }

  }

  @Test
  public void deleteWorkOrderTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int workOrderId = createTestWorkOrder(connection, 0, 0);
      WorkOrderDAO.delete(connection, workOrderId);

      WorkOrder workOrder = WorkOrderDAO.findById(connection, workOrderId);
      assertEquals(workOrder.getStatus(), WorkStatus.DELETED_STATUS);
      assertEquals(formatter.format(workOrder.getStatusDate()), formatter.format(today()));
      assertEquals(workOrder.getDeleted() + "", 1 + "");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void updateStatusFromServiceReportTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      int addressId = createTestAddress(connection);
      int personId = createTestPerson(connection);
      int categoryId = CategoryDAO.createCategory(connection, "categ");
      int equipmentId = CategoryDAO.createEquipment(connection, categoryId, "equip");

      ServiceReport serviceReport = new ServiceReport(0, "", customerId, addressId, personId,
          new Date(), "created", new Date(), "brand101", "model111", "serialno101", 5, "some notes",
          0, "installation");

      serviceReport.setCategoryId(categoryId);
      serviceReport.setEquipmentId(equipmentId);

      int workOrderId = createTestWorkOrder(connection, customerId, 0);

      int serviceReportId = ServiceReportDAO.create(connection, workOrderId, serviceReport);

      WorkOrderDAO.updateStatusFromServiceReport(connection, WorkStatus.ASSIGNED_STATUS,
          serviceReportId);
      WorkOrder workOrder = WorkOrderDAO.findById(connection, workOrderId);
      assertEquals(workOrder.getStatus(), WorkStatus.ASSIGNED_STATUS);
      assertEquals(formatter.format(workOrder.getStatusDate()), formatter.format(today()));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void updateTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      int personId = createTestPerson(connection);

      int workOrderId = createTestWorkOrder(connection, 0, 0);
      WorkOrder workOrder = WorkOrderDAO.findById(connection, workOrderId);
      workOrder.setCustomerId(customerId);
      workOrder.setNotes("some other notes");
      workOrder.setType("service");
      workOrder.setContactPersonId(personId);
      workOrder.setBrand("brand b");
      workOrder.setModel("model12");
      workOrder.setSerialNumber("serno1002");
      workOrder.setPartNumber("partno102");
      WorkOrderDAO.update(connection, workOrder);
      
      assertEquals(workOrder.getCustomerId() + "", "" + customerId);
      assertEquals(workOrder.getNotes(), "some other notes");
      assertEquals(workOrder.getType(), "service");
      assertEquals(workOrder.getContactPersonId() + "", "" + personId);
      assertEquals(workOrder.getBrand(), "brand b");
      assertEquals(workOrder.getModel(), "model12");
      assertEquals(workOrder.getSerialNumber(), "serno1002");
      assertEquals(workOrder.getPartNumber(), "partno102");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getTechniciansTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      int addressId = createTestAddress(connection);
      int personId = createTestPerson(connection);
      int categoryId = CategoryDAO.createCategory(connection, "categ");
      int equipmentId = CategoryDAO.createEquipment(connection, categoryId, "equip");

      ServiceReport serviceReport = new ServiceReport(0, "", customerId, addressId, personId,
          new Date(), "created", new Date(), "brand101", "model111", "serialno101", 5, "some notes",
          0, "installation");

      serviceReport.setCategoryId(categoryId);
      serviceReport.setEquipmentId(equipmentId);

      int workOrderId = createTestWorkOrder(connection, customerId, 0);
      int serviceReportId = ServiceReportDAO.create(connection, workOrderId, serviceReport);
      int technicianId = createTestTechnician(connection);
      ArrayList<Integer> technicianIds = new ArrayList<Integer>();
      technicianIds.add(technicianId);
      technicianIds.add(technicianId);

      ServiceReportDAO.assignTechniciansToServiceReport(connection, serviceReportId, technicianIds);

      String technicians = WorkOrderDAO.getTechnicians(connection, workOrderId);
      assertEquals(technicians, "Asterix Gaul, Asterix Gaul");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void findForViewTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      int addressId = createTestAddress(connection);
      int personId = createTestPerson(connection);
      int categoryId = CategoryDAO.createCategory(connection, "categ");
      int equipmentId = CategoryDAO.createEquipment(connection, categoryId, "equip");

      ServiceReport serviceReport = new ServiceReport(0, "", customerId, addressId, personId,
          new Date(), "created", new Date(), "brand101", "model111", "serialno101", 5, "some notes",
          0, "installation");

      serviceReport.setCategoryId(categoryId);
      serviceReport.setEquipmentId(equipmentId);

      int workOrderId = createTestWorkOrder(connection, customerId, 0);
      int serviceReportId = ServiceReportDAO.create(connection, workOrderId, serviceReport);
      int technicianId = createTestTechnician(connection);
      ArrayList<Integer> technicianIds = new ArrayList<Integer>();
      technicianIds.add(technicianId);
      technicianIds.add(technicianId);

      ServiceReportDAO.assignTechniciansToServiceReport(connection, serviceReportId, technicianIds);

      HashMap<String, String> findForView = WorkOrderDAO.findForView(connection, workOrderId);

      assertEquals(findForView.get("id"), "" + workOrderId);
      assertEquals(findForView.get("workOrderDate"), formatter.format(today()));
      assertEquals(findForView.get("type"), "installation");
      assertEquals(findForView.get("status"), WorkStatus.CREATED_STATUS);
      assertEquals(findForView.get("statusDate"), formatter.format(today()));
      assertEquals(findForView.get("notes"), "install sonething notes");
      assertEquals(findForView.get("category"), "canine");
      assertEquals(findForView.get("equipment"), "belt");
      assertEquals(findForView.get("brand"), "brand a");
      assertEquals(findForView.get("model"), "model10");
      assertEquals(findForView.get("serialNumber"), "serno1001");
      assertEquals(findForView.get("partNumber"), "partno101");

      assertEquals(findForView.get("customerName"), "Bajji Corner");
      assertEquals(findForView.get("contactFirstName"), "Asterix");
      assertEquals(findForView.get("contactLastName"), "Gaul");
      assertEquals(findForView.get("contactDesignation"), "Troubleshooter");
      assertEquals(findForView.get("contactEmail"), "asterix@aishtek.tst");
      assertEquals(findForView.get("contactPhone"), "9999999999");

      assertEquals(findForView.get("customerStreet"), "Food St.");
      assertEquals(findForView.get("customerArea"), "VV Puram");
      assertEquals(findForView.get("customerCity"), "Blore");
      assertEquals(findForView.get("customerState"), "KTKA");
      assertEquals(findForView.get("customerPincode"), "560004");
      assertEquals(findForView.get("technicians"), "Asterix Gaul, Asterix Gaul");


      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
