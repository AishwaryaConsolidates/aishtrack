package com.aishtek.aishtrack.beans;

import java.math.BigDecimal;
import java.util.Date;

public class DomesticRemittance extends BaseBean {

  int fromBankAccountId;
  int fromBankAddressId;
  int supplierId;
  int supplierBankAccountId;
  BigDecimal amount;
  String purpose;
  Date signatureDate;
  String chequeNumber;
  Date chequeDate;
  // for view
  String signatureDateString;
  String chequeDateString;

  public DomesticRemittance(int id, int fromBankAccountId, int fromBankAddressId, int supplierId,
      int supplierBankAccountId, BigDecimal amount, String purpose, Date signatureDate,
      int deleted, String chequeNumber, Date chequeDate) {
    this.id = id;
    this.fromBankAccountId = fromBankAccountId;
    this.fromBankAddressId = fromBankAddressId;
    this.supplierId = supplierId;
    this.supplierBankAccountId = supplierBankAccountId;
    this.amount = amount;
    this.purpose = purpose;
    this.signatureDate = signatureDate;
    this.deleted = deleted;
    this.chequeNumber = chequeNumber;
    this.chequeDate = chequeDate;
  }

  public int getFromBankAccountId() {
    return fromBankAccountId;
  }

  public void setFromBankAccountId(int fromBankAccountId) {
    this.fromBankAccountId = fromBankAccountId;
  }

  public int getFromBankAddressId() {
    return fromBankAddressId;
  }

  public void setFromBankAddressId(int fromBankAddressId) {
    this.fromBankAddressId = fromBankAddressId;
  }

  public int getSupplierId() {
    return supplierId;
  }

  public void setSupplierId(int supplierId) {
    this.supplierId = supplierId;
  }

  public int getSupplierBankAccountId() {
    return supplierBankAccountId;
  }

  public void setSupplierBankAccountId(int supplierBankAccountId) {
    this.supplierBankAccountId = supplierBankAccountId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public Date getSignatureDate() {
    return signatureDate;
  }

  public void setSignatureDate(Date signatureDate) {
    this.signatureDate = signatureDate;
  }

  public String getSignatureDateString() {
    return signatureDateString;
  }

  public void setSignatureDateString(String signatureDateString) {
    this.signatureDateString = signatureDateString;
  }

  public String getChequeNumber() {
    return chequeNumber;
  }

  public void setChequeNumber(String chequeNumber) {
    this.chequeNumber = chequeNumber;
  }

  public Date getChequeDate() {
    return chequeDate;
  }

  public void setChequeDate(Date chequeDate) {
    this.chequeDate = chequeDate;
  }

  public String getChequeDateString() {
    return chequeDateString;
  }

  public void setChequeDateString(String chequeDateString) {
    this.chequeDateString = chequeDateString;
  }

}
