package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.Test;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.test.BaseIntegrationTest;
import com.aishtek.aishtrack.utils.WorkStatus;

public class WorkOrderDAOSearchTest extends BaseIntegrationTest {

  private int customerId;
  private String type = "service report";
  private String notes = "This is a note";

  @Test
  public void searchSearchesByCustomerNameNickName() throws SQLException {
    try (Connection connection = getConnection()) {
      customerId = createTestCustomer(connection);
      WorkOrder workOrder = new WorkOrder(customerId, type, notes);
      WorkOrderDAO.create(connection, workOrder);

      ArrayList<WorkOrder> workOrders = WorkOrderDAO.searchFor(connection, "Bajji", null);

      assertEquals(workOrders.size(), 1);
      assertEquals(workOrders.get(0).getCustomerId(), customerId);
      assertEquals(workOrders.get(0).getCustomerName(), "Bajji Corner");
      assertEquals(workOrder.getNotes(), notes);
      assertEquals(workOrder.getType(), type);
      assertEquals(workOrder.getStatus(), WorkStatus.CREATED_STATUS);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void searchDoesAPartialMatch() throws SQLException {
    try (Connection connection = getConnection()) {
      customerId = createTestCustomer(connection);
      WorkOrder workOrder = new WorkOrder(customerId, type, notes);
      WorkOrderDAO.create(connection, workOrder);

      ArrayList<WorkOrder> workOrders = WorkOrderDAO.searchFor(connection, "ajj", null);

      assertEquals(workOrders.size(), 1);
      assertEquals(workOrders.get(0).getCustomerId(), customerId);
      assertEquals(workOrders.get(0).getCustomerName(), "Bajji Corner");
      assertEquals(workOrder.getNotes(), notes);
      assertEquals(workOrder.getType(), type);
      assertEquals(workOrder.getStatus(), WorkStatus.CREATED_STATUS);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void searchSearchesByStatus() throws SQLException {
    try (Connection connection = getConnection()) {
      customerId = createTestCustomer(connection);
      WorkOrder workOrder = new WorkOrder(customerId, type, notes);
      WorkOrderDAO.create(connection, workOrder);

      ArrayList<WorkOrder> workOrders =
          WorkOrderDAO.searchFor(connection, "ajj", WorkStatus.CREATED_STATUS);

      assertEquals(workOrders.size(), 1);
      assertEquals(workOrder.getStatus(), WorkStatus.CREATED_STATUS);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void searchDoesNotReturnIfDifferentStatus() throws SQLException {
    try (Connection connection = getConnection()) {
      customerId = createTestCustomer(connection);
      WorkOrder workOrder = new WorkOrder(customerId, type, notes);
      WorkOrderDAO.create(connection, workOrder);

      ArrayList<WorkOrder> workOrders =
          WorkOrderDAO.searchFor(connection, "ajj", WorkStatus.ASSIGNED_STATUS);

      assertEquals(workOrders.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void searchDoesNotReturnIfNameDoesntMatch() throws SQLException {
    try (Connection connection = getConnection()) {
      customerId = createTestCustomer(connection);
      WorkOrder workOrder = new WorkOrder(customerId, type, notes);
      WorkOrderDAO.create(connection, workOrder);

      ArrayList<WorkOrder> workOrders = WorkOrderDAO.searchFor(connection, "Burgi", null);

      assertEquals(workOrders.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
