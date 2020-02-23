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
  private int fileId;

  // for view
  private String expenseDateString;
  private String expenseFileLink;

  public Expense(int id, int expenseReportId, Date expenseDate, String expenseType, String notes,
      BigDecimal amount, String expenseFileLink) {
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
    this.expenseFileLink = expenseFileLink;
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
    if (amount == null) {
      return new BigDecimal(0);
    }
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

  public int getFileId() {
    return fileId;
  }

  public void setFileId(int fileId) {
    this.fileId = fileId;
  }

  public String getExpenseFileLink() {
    return expenseFileLink;
  }

  public void setExpenseFileLink(String expenseFileLink) {
    this.expenseFileLink = expenseFileLink;
  }
}
