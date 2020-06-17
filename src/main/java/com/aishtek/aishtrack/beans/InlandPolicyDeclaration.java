package com.aishtek.aishtrack.beans;

import java.math.BigDecimal;
import java.util.Date;

public class InlandPolicyDeclaration extends BaseBean {

  private int inlandPolicyId;
  private int customerId;
  private BigDecimal amount;
  private String description;
  private int quantity;
  private String toLocation;
  private String fromLocation;
  private String invoiceNumber;
  private Date invoiceDate;
  private String receiptNumber;
  private Date receiptDate;

  // for view
  String invoiceDateString;
  String receiptDateString;

  public InlandPolicyDeclaration(int id, int inlandPolicyId, int customerId,
      BigDecimal amount, String description, int quantity, String toLocation,
      String fromLocation, String invoiceNumber, Date invoiceDate, String receiptNumber,
      Date receiptDate, int deleted) {

    this.id = id;
    this.inlandPolicyId = inlandPolicyId;
    this.customerId = customerId;
    this.amount = amount;
    this.description = description;
    this.quantity = quantity;
    this.toLocation = toLocation;
    this.fromLocation = fromLocation;
    this.invoiceNumber = invoiceNumber;
    this.invoiceDate = invoiceDate;
    this.receiptNumber = receiptNumber;
    this.receiptDate = receiptDate;
    this.deleted = deleted;
  }

  public int getInlandPolicyId() {
    return inlandPolicyId;
  }

  public void setInlandPolicyId(int inlandPolicyId) {
    this.inlandPolicyId = inlandPolicyId;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getToLocation() {
    return toLocation;
  }

  public void setToLocation(String toLocation) {
    this.toLocation = toLocation;
  }

  public String getFromLocation() {
    return fromLocation;
  }

  public void setFromLocation(String fromLocation) {
    this.fromLocation = fromLocation;
  }

  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public Date getInvoiceDate() {
    return invoiceDate;
  }

  public void setInvoiceDate(Date invoiceDate) {
    this.invoiceDate = invoiceDate;
  }

  public String getReceiptNumber() {
    return receiptNumber;
  }

  public void setReceiptNumber(String receiptNumber) {
    this.receiptNumber = receiptNumber;
  }

  public Date getReceiptDate() {
    return receiptDate;
  }

  public void setReceiptDate(Date receiptDate) {
    this.receiptDate = receiptDate;
  }

  public String getInvoiceDateString() {
    return invoiceDateString;
  }

  public void setInvoiceDateString(String invoiceDateString) {
    this.invoiceDateString = invoiceDateString;
  }

  public String getReceiptDateString() {
    return receiptDateString;
  }

  public void setReceiptDateString(String receiptDateString) {
    this.receiptDateString = receiptDateString;
  }


}
