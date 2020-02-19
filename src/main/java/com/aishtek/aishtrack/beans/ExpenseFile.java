package com.aishtek.aishtrack.beans;

public class ExpenseFile extends BaseBean {

  private int id;
  private int expenseId;
  private int fileId;

  public ExpenseFile(int expenseId, int fileId) {
    this.expenseId = expenseId;
    this.fileId = fileId;
  }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }

  public int getFileId() {
    return fileId;
  }

  public void setFileId(int fileId) {
    this.fileId = fileId;
  }

  public int getExpenseId() {
    return expenseId;
  }

  public void setExpenseId(int expenseId) {
    this.expenseId = expenseId;
  }
}
