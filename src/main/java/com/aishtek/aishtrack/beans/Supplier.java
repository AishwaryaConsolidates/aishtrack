package com.aishtek.aishtrack.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class Supplier extends BaseBean {

  private String name;
  private String type;

  private ArrayList<HashMap<String, String>> addresses;
  private ArrayList<NameId> bankAccounts;

  public Supplier(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public Supplier(int id, String name, String type) {
    this.id = id;
    this.name = name;
    this.type = type;
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ArrayList<NameId> getBankAccounts() {
    return bankAccounts;
  }

  public void setBankAccounts(ArrayList<NameId> bankAccounts) {
    this.bankAccounts = bankAccounts;
  }
}
