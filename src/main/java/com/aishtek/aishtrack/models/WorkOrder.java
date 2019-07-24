package com.aishtek.aishtrack.models;

import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;
import com.aishtek.aishtrack.utils.WorkStatus;

@BelongsToParents({@BelongsTo(parent = Customer.class, foreignKeyName = "customer_id")})

@Table("work_orders")
public class WorkOrder extends ActiveJDBCBaseModel {

  public static WorkOrder createWorkOrder(int customerId, String type, String notes) {
    return WorkOrder.createIt(
        "customer_id", customerId,
        "type", type, "notes", notes, "status", WorkStatus.CREATED_STATUS,
        "status_date", currentTimestamp()
        );
  }

  public void markAsAssigned() {
    set("status", WorkStatus.ASSIGNED_STATUS, "status_date", currentTimestamp()).saveIt();
  }

  public int getWorkOrderId() {
    return getInteger("id");
  }

  public int getCustomerId() {
    return getInteger("customer_id");
  }

  public String getNotes() {
    return getString("notes");
  }

  public String getType() {
    return getString("type");
  }

  public String getStatus() {
    return getString("status");
  }

  public Customer getCustomer() {
    return parent(Customer.class);
  }
}
