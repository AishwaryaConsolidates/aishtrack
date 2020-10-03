package com.aishtek.aishtrack.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class DeclarationsReport {

  int policyId;
  String provider;
  String policyStreet;
  String policyCity;
  String policyArea;
  String policyPin;

  BigDecimal amountInsured;
  BigDecimal amountUsed;
  BigDecimal openingBalance;
  BigDecimal amountDeclared;
  BigDecimal amountBalance;

  ArrayList<HashMap<String, String>> declarations;

  public void calculateAmounts() {
    calculateOpeningBalance();
    calculateAmountDeclared();
    calculateAmountBalance();
  }

  public void calculateAmountBalance() {
    setAmountBalance(openingBalance.subtract(amountDeclared));
  }

  public void calculateOpeningBalance() {
    setOpeningBalance(amountInsured.subtract(amountUsed));
  }

  public void calculateAmountDeclared() {
    this.amountDeclared = new BigDecimal(0);
    for (HashMap<String, String> declaration : declarations) {
      BigDecimal totalAmount = declaration.get("amountDeclared") == null ? new BigDecimal(0)
          : new BigDecimal(declaration.get("amountDeclared"));
      BigDecimal dutyAmount = declaration.get("dutyAmount") == null ? new BigDecimal(0)
          : new BigDecimal(declaration.get("dutyAmount"));
      this.amountDeclared = this.amountDeclared.add(totalAmount).add(dutyAmount);
    }
  }

  public int getPolicyId() {
    return policyId;
  }

  public void setPolicyId(int policyId) {
    this.policyId = policyId;
  }

  public BigDecimal getAmountInsured() {
    return amountInsured;
  }

  public void setAmountInsured(BigDecimal amount) {
    this.amountInsured = (amount == null ? new BigDecimal(0) : amount);
  }

  public BigDecimal getAmountUsed() {
    return amountUsed;
  }

  public void setAmountUsed(BigDecimal amountUsed) {
    this.amountUsed = (amountUsed == null ? new BigDecimal(0) : amountUsed);
  }

  public ArrayList<HashMap<String, String>> getDeclarations() {
    return declarations;
  }

  public void setDeclarations(ArrayList<HashMap<String, String>> declarations) {
    this.declarations = declarations;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getPolicyStreet() {
    return policyStreet;
  }

  public void setPolicyStreet(String policyStreet) {
    this.policyStreet = policyStreet;
  }

  public String getPolicyCity() {
    return policyCity;
  }

  public void setPolicyCity(String policyCity) {
    this.policyCity = policyCity;
  }

  public String getPolicyArea() {
    return policyArea;
  }

  public void setPolicyArea(String policyArea) {
    this.policyArea = policyArea;
  }

  public String getPolicyPin() {
    return policyPin;
  }

  public void setPolicyPin(String policyPin) {
    this.policyPin = policyPin;
  }

  public void setAmountBalance(BigDecimal amountBalance) {
    this.amountBalance = amountBalance;
  }

  public void setOpeningBalance(BigDecimal openingBalance) {
    this.openingBalance = openingBalance;
  }

  public void setAmountDeclared(BigDecimal amountDeclared) {
    this.amountDeclared = amountDeclared;
  }

  public BigDecimal getOpeningBalance() {
    return openingBalance;
  }

  public BigDecimal getAmountDeclared() {
    return amountDeclared;
  }

  public BigDecimal getAmountBalance() {
    return amountBalance;
  }

}
