package com.aishtek.aishtrack.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import com.aishtek.aishtrack.utils.Util;

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
  private String expenseReportDateString;

  public ExpenseReport(int id, int serviceReportId, int customerId, int technicianId,
      BigDecimal advanceAmount, int settled, BigDecimal carryForwardAmount, String location,
      Date advanceAmountDate, Date createdAt) {
    this.id = id;
    this.serviceReportId = serviceReportId;
    this.customerId = customerId;
    this.technicianId = technicianId;
    this.advanceAmount = advanceAmount;
    this.settled = settled;
    this.carryForwardAmount = carryForwardAmount;
    this.location = location;
    this.advanceAmountDate = advanceAmountDate;
    this.createdAt = createdAt;
  }

  public void calculateTotalExpenseAmount() {
    this.totalExpenseAmount = new BigDecimal(0);
    if (expenses != null) {
      expenses.forEach(
          expense -> this.totalExpenseAmount = this.totalExpenseAmount.add(expense.getAmount()));
    }
    this.amountDue = calculateAmountDue();
  }

  public BigDecimal calculateAmountDue() {
    return this.totalExpenseAmount.subtract(getCarryForwardAmount().add(getAdvanceAmount()));
  }

  public void prepareForPrint() {
    calculateTotalExpenseAmount();
    if (getAdvanceAmountDate() != null) {
      setAdvanceAmountDateString(Util.formatDate(getAdvanceAmountDate()));
    }
    if (getCreatedAt() != null) {
      setExpenseReportDateString(Util.formatDate(getCreatedAt()));
    }
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
    if (advanceAmount == null) {
      return new BigDecimal(0);
    }
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
    if (carryForwardAmount == null) {
      return new BigDecimal(0);
    }
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

  public void setTotalExpenseAmount(BigDecimal totalExpenseAmount) {
    this.totalExpenseAmount = totalExpenseAmount;
  }

  public void setAmountDue(BigDecimal amountDue) {
    this.amountDue = amountDue;
  }

  public String getExpenseReportDateString() {
    return expenseReportDateString;
  }

  public void setExpenseReportDateString(String expenseReportDateString) {
    this.expenseReportDateString = expenseReportDateString;
  }

  public BigDecimal getTotalExpenseAmount() {
    return totalExpenseAmount;
  }

  public BigDecimal getAmountDue() {
    return amountDue;
  }

}
