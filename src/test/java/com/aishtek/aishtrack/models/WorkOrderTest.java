package com.aishtek.aishtrack.models;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.aishtek.aishtrack.test.BaseIntegrationTest;
import com.aishtek.aishtrack.utils.WorkStatus;

public class WorkOrderTest extends BaseIntegrationTest {

  private int customerId;
  private String type = "service report";
  private String notes = "This is a note";
    

  @Before
  public void createClients() {
    customerId = createTestCustomer().getCustomerId();
  }

  @Test
  public void createWorkOrderSavesTheWorkOrder() {
    WorkOrder.createWorkOrder(customerId, type, notes);
    WorkOrder workOrder = (WorkOrder) WorkOrder.findAll().get(0);

    assertEquals(workOrder.getCustomerId(), customerId);
    assertEquals(workOrder.getNotes(), notes);
    assertEquals(workOrder.getType(), type);
    assertEquals(workOrder.getStatus(), WorkStatus.CREATED_STATUS);
    }

  @Test
  public void getCustomerTest() {
    WorkOrder workOrder = WorkOrder.createWorkOrder(customerId, type, notes);
    assertEquals(workOrder.getCustomer().getCustomerId(), customerId);
  }
}
