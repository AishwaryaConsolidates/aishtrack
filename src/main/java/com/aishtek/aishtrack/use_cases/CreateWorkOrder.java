package com.aishtek.aishtrack.use_cases;

import com.aishtek.aishtrack.models.WorkOrder;

public class CreateWorkOrder {

  public static WorkOrder process(int customerId, String type, String notes) {
    // create the work order
    return WorkOrder.createWorkOrder(customerId, type, notes);
  }
}
