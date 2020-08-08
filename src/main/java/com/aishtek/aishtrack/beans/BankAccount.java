package com.aishtek.aishtrack.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class BankAccount extends BaseBean {

  private String name;
  private String branch;
  private String swiftCode;
  private String accountNumber;
  private String encryptedAccountNumber;
  private String iban;
  private String otherDetails;

  private ArrayList<HashMap<String, String>> addresses;

  public BankAccount(int id, String name, String branch, String swiftCode, String accountNumber,
      String iban, String otherDetails, String encryptedAccountNumber) {
    this.id = id;
    this.name = name;
    this.branch = branch;
    this.swiftCode = swiftCode;
    this.accountNumber = accountNumber;
    this.iban = iban;
    this.otherDetails = otherDetails;
    this.encryptedAccountNumber = encryptedAccountNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArrayList<HashMap<String, String>> getAddresses() {
    return addresses;
  }

  public void setAddresses(ArrayList<HashMap<String, String>> addresses) {
    this.addresses = addresses;
  }

  public String getBranch() {
    return branch;
  }

  public void setBranch(String branch) {
    this.branch = branch;
  }

  public String getSwiftCode() {
    return swiftCode;
  }

  public void setSwiftCode(String swiftCode) {
    this.swiftCode = swiftCode;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public String getOtherDetails() {
    return otherDetails;
  }

  public void setOtherDetails(String otherDetails) {
    this.otherDetails = otherDetails;
  }

  public String getEncryptedAccountNumber() {
    return encryptedAccountNumber;
  }

  public void setEncryptedAccountNumber(String encryptedAccountNumber) {
    this.encryptedAccountNumber = encryptedAccountNumber;
  }
}
