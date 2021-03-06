package com.aishtek.aishtrack.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends BaseBean {

  private String name;
  private String nickName;
  private String gstIN;
  private int addressId;

  // string fields for view
  private String contactPerson;
  private String address;
  private ArrayList<HashMap<String, String>> workOrders;
  private ArrayList<HashMap<String, String>> contactPersons;

  public Customer(int id, String name, String nickName) {
    this.id = id;
    this.name = name;
    this.nickName = nickName;
  }

  public Customer(int id, String name, String nickName, int addressId, int deleted, String gstIN) {
    this.id = id;
    this.name = name;
    this.nickName = nickName;
    this.addressId = addressId;
    this.deleted = deleted;
    this.gstIN = gstIN;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public int getAddressId() {
    return addressId;
  }

  public void setAddressId(int addressId) {
    this.addressId = addressId;
  }

  public String getContactPerson() {
    return contactPerson;
  }

  public void setContactPerson(Person contactPerson) {
    this.contactPerson = contactPerson.getFullPerson();
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address.getFullAddress();
  }

  public ArrayList<HashMap<String, String>> getWorkOrders() {
    return workOrders;
  }

  public void setWorkOrders(ArrayList<HashMap<String, String>> workOrders) {
    this.workOrders = workOrders;
  }

  public ArrayList<HashMap<String, String>> getContactPersons() {
    return contactPersons;
  }

  public void setContactPersons(ArrayList<HashMap<String, String>> contactPersons) {
    this.contactPersons = contactPersons;
  }

  public String getGstIN() {
    return gstIN;
  }

  public void setGstIN(String gstIN) {
    this.gstIN = gstIN;
  }
}
