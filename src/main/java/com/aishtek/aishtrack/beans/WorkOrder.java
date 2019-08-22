package com.aishtek.aishtrack.beans;

import java.util.Date;
import com.aishtek.aishtrack.utils.WorkStatus;

public class WorkOrder extends BaseBean {

  private int customerId;
  private String type;
  private String status;
  private Date statusDate;
  private String notes;

  // search fields
  private String customerName;

  public WorkOrder(int customerId, String type, String notes) {
    this.customerId = customerId;
    this.type = type;
    this.notes = notes;
    setStatus(WorkStatus.CREATED_STATUS);
  }

  public WorkOrder(int id, int customerId, String type, String status, Date statusDate,
      String notes, int deleted) {
    this.id = id;
    this.customerId = customerId;
    this.type = type;
    this.notes = notes;
    this.status = status;
    this.statusDate = statusDate;
    this.deleted = deleted;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
    this.statusDate = new Date();
  }

  public Date getStatusDate() {
    return statusDate;
  }

  public void setStatusDate(Date statusDate) {
    this.statusDate = statusDate;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }
}
