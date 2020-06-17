package com.aishtek.aishtrack.beans;

import java.math.BigDecimal;
import java.util.Date;

public class OutwardRemittance extends BaseBean {

  int fromBankAccountId;
  int fromBankAddressId;
  int fromAddressId;
  int supplierId;
  int supplierAddressId;
  int supplierBankAccountId;
  int supplierBankAddressId;
  BigDecimal amount;
  String goods;
  String goodsClassificationNo;
  String countryOfOrigin;
  String currency;
  String purpose;
  String otherInfo;
  String signaturePlace;
  Date signatureDate;

  // for view
  String signatureDateString;

  public OutwardRemittance(int id, int fromBankAccountId, int fromBankAddressId, int fromAddressId,
      int supplierId, int supplierAddressId, int supplierBankAccountId, int supplierBankAddressId,
      BigDecimal amount, String goods, String goodsClassificationNo, String countryOfOrigin,
      String currency, String purpose, String otherInfo, String signaturePlace,
      Date signatureDate, int deleted) {
    this.id = id;
    this.fromBankAccountId = fromBankAccountId;
    this.fromBankAddressId = fromBankAddressId;
    this.fromAddressId = fromAddressId;
    this.supplierId = supplierId;
    this.supplierAddressId = supplierAddressId;
    this.supplierBankAccountId = supplierBankAccountId;
    this.supplierBankAddressId = supplierBankAddressId;
    this.amount = amount;
    this.goods = goods;
    this.goodsClassificationNo = goodsClassificationNo;
    this.countryOfOrigin = countryOfOrigin;
    this.currency = currency;
    this.purpose = purpose;
    this.otherInfo = otherInfo;
    this.signaturePlace = signaturePlace;
    this.signatureDate = signatureDate;
    this.deleted = deleted;
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

  public int getFromAddressId() {
    return fromAddressId;
  }

  public void setFromAddressId(int fromAddressId) {
    this.fromAddressId = fromAddressId;
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

  public int getSupplierBankAccountId() {
    return supplierBankAccountId;
  }

  public void setSupplierBankAccountId(int supplierBankAccountId) {
    this.supplierBankAccountId = supplierBankAccountId;
  }

  public int getSupplierBankAddressId() {
    return supplierBankAddressId;
  }

  public void setSupplierBankAddressId(int supplierBankAddressId) {
    this.supplierBankAddressId = supplierBankAddressId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getGoods() {
    return goods;
  }

  public void setGoods(String goods) {
    this.goods = goods;
  }

  public String getGoodsClassificationNo() {
    return goodsClassificationNo;
  }

  public void setGoodsClassificationNo(String goodsClassificationNo) {
    this.goodsClassificationNo = goodsClassificationNo;
  }

  public String getCountryOfOrigin() {
    return countryOfOrigin;
  }

  public void setCountryOfOrigin(String countryOfOrigin) {
    this.countryOfOrigin = countryOfOrigin;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getPurpose() {
    return purpose;
  }

  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  public String getOtherInfo() {
    return otherInfo;
  }

  public void setOtherInfo(String otherInfo) {
    this.otherInfo = otherInfo;
  }

  public String getSignaturePlace() {
    return signaturePlace;
  }

  public void setSignaturePlace(String signaturePlace) {
    this.signaturePlace = signaturePlace;
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

}
