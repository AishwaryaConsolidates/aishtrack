package com.aishtek.aishtrack.function;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.dao.WorkOrderDAO;
import com.aishtek.aishtrack.test.BaseIntegrationTest;
import com.aishtek.aishtrack.utils.WorkStatus;

public class CreateWorkOrderTest extends BaseIntegrationTest {

  private int customerId;
  private String notes = "This is a note";
  private String type = "Service Report";

  @Test
  public void itCreatesAWorkOrder() {
    try (Connection connection = getConnection()) {
      customerId = createTestCustomer(connection);
      int workOrderId =
          (new CreateWorkOrder()).createWorkOrder(connection, customerId, type, notes);

      WorkOrder workOrder = WorkOrderDAO.findById(connection, workOrderId);

    assertEquals(workOrder.getCustomerId(), customerId);
    assertEquals(workOrder.getType(), type);
    assertEquals(workOrder.getNotes(), notes);
    assertEquals(workOrder.getStatus(), WorkStatus.CREATED_STATUS);
    } catch (SQLException e) {
      e.printStackTrace();
      assert (false);
    }
  }
}
