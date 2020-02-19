package com.aishtek.aishtrack.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class ExpenseReport extends BaseBean {

  private int serviceReportId;
  private int customerId;
  private int technicianId;
  private BigDecimal advanceAmount;
  private BigDecimal carryForwardAmount;
  private String location;
  private int settled;
  private Date advanceAmountDate;

  private ArrayList<Expense> expenses;

  // for view
  private String customerName;
  private String technicianName;
  private String advanceAmountDateString;
  private BigDecimal totalExpenseAmount;
  private BigDecimal amountDue;

  public ExpenseReport(int id, int serviceReportId, int customerId, int technicianId,
      BigDecimal advanceAmount, int settled, BigDecimal carryForwardAmount, String location,
      Date advanceAmountDate) {
    this.id = id;
    this.serviceReportId = serviceReportId;
    this.customerId = customerId;
    this.technicianId = technicianId;
    this.advanceAmount = advanceAmount;
    this.settled = settled;
    this.carryForwardAmount = carryForwardAmount;
    this.location = location;
    this.advanceAmountDate = advanceAmountDate;
  }

  public BigDecimal getTotalExpenseAmount() {
    totalExpenseAmount = new BigDecimal(0);
    expenses.forEach(expense -> totalExpenseAmount.add(expense.getAmount()));
    return totalExpenseAmount;
  }

  public BigDecimal getAmountDue() {
    return getTotalExpenseAmount().subtract(carryForwardAmount.add(advanceAmount));
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

  public BigDecimal getCarryForwardAmount() {
    return carryForwardAmount;
  }

  public void setCarryForwardAmount(BigDecimal carryForwardAmount) {
    this.carryForwardAmount = carryForwardAmount;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Date getAdvanceAmountDate() {
    return advanceAmountDate;
  }

  public void setAdvanceAmountDate(Date advanceAmountDate) {
    this.advanceAmountDate = advanceAmountDate;
  }

  public String getAdvanceAmountDateString() {
    return advanceAmountDateString;
  }

  public void setAdvanceAmountDateString(String advanceAmountDateString) {
    this.advanceAmountDateString = advanceAmountDateString;
  }

}
