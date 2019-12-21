package com.aishtek.aishtrack.beans;

import java.math.BigDecimal;
import java.util.Date;
import com.aishtek.aishtrack.utils.Util;

public class Expense extends BaseBean {

  private int expenseReportId;
  private Date expenseDate;
  private String expenseType;
  private String notes;
  private BigDecimal amount;

  // for view
  private String expenseDateString;

  public Expense(int id, int expenseReportId, Date expenseDate, String expenseType, String notes,
      BigDecimal amount) {
    this.id = id;
    this.expenseReportId = expenseReportId;
    this.expenseDate = expenseDate;
    this.expenseType = expenseType;
    this.notes = notes;
    this.amount = amount;
    if (expenseDate != null) {
      this.expenseDateString = Util.formatDate(expenseDate);
    } else {
      this.expenseDateString = "";
    }
  }

  public int getExpenseReportId() {
    return expenseReportId;
  }

  public void setExpenseReportId(int expenseReportId) {
    this.expenseReportId = expenseReportId;
  }

  public Date getExpenseDate() {
    return expenseDate;
  }

  public void setExpenseDate(Date expenseDate) {
    this.expenseDate = expenseDate;
  }

  public String getExpenseType() {
    return expenseType;
  }

  public void setExpenseType(String expenseType) {
    this.expenseType = expenseType;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getExpenseDateString() {
    return expenseDateString;
  }

  public void setExpenseDateString(String expenseDateString) {
    this.expenseDateString = expenseDateString;
  }
}
