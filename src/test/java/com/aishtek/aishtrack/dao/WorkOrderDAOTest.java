package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.test.BaseIntegrationTest;
import com.aishtek.aishtrack.utils.WorkStatus;

public class WorkOrderDAOTest extends BaseIntegrationTest {

  private int customerId;
  private String type = "service report";
  private String notes = "This is a note";

  @Test
  public void createWorkOrderSavesTheWorkOrder() throws SQLException {
    try (Connection connection = getConnection()) {
      customerId = createTestCustomer(connection);

      WorkOrder workOrder = new WorkOrder(customerId, type, notes);
      int workOrderId = WorkOrderDAO.create(connection, workOrder);
      workOrder = WorkOrderDAO.findById(connection, workOrderId);

      assertEquals(workOrder.getCustomerId(), customerId);
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
  public void markAssignedUpdatesTheWorkOrder() throws SQLException {
    try (Connection connection = getConnection()) {
      int workOrderId = createTestWorkOrder(connection);
      WorkOrderDAO.markAsAssigned(connection, workOrderId);

      WorkOrder workOrder = WorkOrderDAO.findById(connection, workOrderId);
      assertEquals(workOrder.getStatus(), WorkStatus.ASSIGNED_STATUS);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }

  }
}
