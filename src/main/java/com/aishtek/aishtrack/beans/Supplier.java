package com.aishtek.aishtrack.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class Supplier extends BaseBean {

  private String name;

  private ArrayList<HashMap<String, String>> addresses;
  private ArrayList<HashMap<String, String>> bankAccounts;

  public Supplier(int id, String name) {
    this.id = id;
    this.name = name;
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

  public ArrayList<HashMap<String, String>> getBankAccounts() {
    return bankAccounts;
  }

  public void setBankAccounts(ArrayList<HashMap<String, String>> bankAccounts) {
    this.bankAccounts = bankAccounts;
  }
}
