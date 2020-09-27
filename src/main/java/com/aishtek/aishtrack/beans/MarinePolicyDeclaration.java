package com.aishtek.aishtrack.beans;

import java.math.BigDecimal;
import java.util.Date;

public class MarinePolicyDeclaration extends BaseBean {

  private int marinePolicyId;
  private int supplierId;
  private int supplierAddressId;
  private BigDecimal amount = new BigDecimal(0);
  private BigDecimal exchangeRate = new BigDecimal(0);
  private BigDecimal dutyAmount = new BigDecimal(0);
  private String currency;
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
  // calculated
  private BigDecimal amountDeclared = new BigDecimal(0);
  private BigDecimal totalAmount = new BigDecimal(0);

  public MarinePolicyDeclaration(int id, int marinePolicyId, int supplierId, int supplierAddressId,
      BigDecimal amount, String currency, String description, int quantity, String toLocation,
      String fromLocation, String invoiceNumber, Date invoiceDate, String receiptNumber,
      Date receiptDate, int deleted, BigDecimal exchangeRate, BigDecimal dutyAmount) {

    this.id = id;
    this.marinePolicyId = marinePolicyId;
    this.supplierId = supplierId;
    this.supplierAddressId = supplierAddressId;
    this.amount = amount;
    this.currency = currency;
    this.description = description;
    this.quantity = quantity;
    this.toLocation = toLocation;
    this.fromLocation = fromLocation;
    this.invoiceNumber = invoiceNumber;
    this.invoiceDate = invoiceDate;
    this.receiptNumber = receiptNumber;
    this.receiptDate = receiptDate;
    this.deleted = deleted;
    this.dutyAmount = dutyAmount;
    this.exchangeRate = exchangeRate;

    setAmountDeclared();
  }

  public int getMarinePolicyId() {
    return marinePolicyId;
  }

  public void setMarinePolicyId(int marinePolicyId) {
    this.marinePolicyId = marinePolicyId;
  }

  public int getSupplierId() {
    return supplierId;
  }

  public void setSupplierId(int supplierId) {
    this.supplierId = supplierId;
  }

  public int getSupplierAddressId() {
    return supplierAddressId;
  }

  public void setSupplierAddressId(int supplierAddressId) {
    this.supplierAddressId = supplierAddressId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
    setAmountDeclared();
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
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

  public BigDecimal getExchangeRate() {
    return exchangeRate;
  }

  public void setExchangeRate(BigDecimal exchangeRate) {
    this.exchangeRate = exchangeRate;
  }

  public BigDecimal getDutyAmount() {
    return dutyAmount;
  }

  public void setDutyAmount(BigDecimal dutyAmount) {
    this.dutyAmount = dutyAmount;
    setTotalAmount();
  }

  public BigDecimal getAmountDeclared() {
    return amountDeclared;
  }

  public void setAmountDeclared() {
    this.amountDeclared = amount.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    setTotalAmount();
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount() {
    this.totalAmount =
        getAmountDeclared().add(getDutyAmount()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
  }


}
