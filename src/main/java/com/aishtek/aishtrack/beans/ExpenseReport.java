package com.aishtek.aishtrack.beans;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ExpenseReport extends BaseBean {

  private int serviceReportId;
  private int customerId;
  private int technicianId;
  private BigDecimal advanceAmount;
  private int settled;

  private ArrayList<Expense> expenses;

  // for view
  private String customerName;
  private String technicianName;

  public ExpenseReport(int id, int serviceReportId, int customerId, int technicianId,
      BigDecimal advanceAmount, int settled) {
    this.id = id;
    this.serviceReportId = serviceReportId;
    this.customerId = customerId;
    this.technicianId = technicianId;
    this.advanceAmount = advanceAmount;
    this.settled = settled;
  }

  public int getServiceReportId() {
    return serviceReportId;
  }

  public void setServiceReportId(int serviceReportId) {
    this.serviceReportId = serviceReportId;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public int getTechnicianId() {
    return technicianId;
  }

  public void setTechnicianId(int technicianId) {
    this.technicianId = technicianId;
  }

  public ArrayList<Expense> getExpenses() {
    return expenses;
  }

  public void setExpenses(ArrayList<Expense> expenses) {
    this.expenses = expenses;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getTechnicianName() {
    return technicianName;
  }

  public void setTechnicianName(String technicianName) {
    this.technicianName = technicianName;
  }

  public BigDecimal getAdvanceAmount() {
    return advanceAmount;
  }

  public void setAdvanceAmount(BigDecimal advanceAmount) {
    this.advanceAmount = advanceAmount;
  }

  public int getSettled() {
    return settled;
  }

  public void setSettled(int settled) {
    this.settled = settled;
  }

}
