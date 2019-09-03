package com.aishtek.aishtrack.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer extends BaseBean {

  private String name;
  private String nickName;
  private int addressId;
  private int contactPersonId;

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

  public Customer(int id, String name, String nickName, int addressId, int contactPersonId,
      int deleted) {
    this.id = id;
    this.name = name;
    this.nickName = nickName;
    this.addressId = addressId;
    this.contactPersonId = contactPersonId;
    this.deleted = deleted;
  }

  public Customer(String name, String nickName, int addressId, int contactPersonId) {
    this.name = name;
    this.nickName = nickName;
    this.addressId = addressId;
    this.contactPersonId = contactPersonId;
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

  public int getContactPersonId() {
    return contactPersonId;
  }

  public void setContactPersonId(int contactPersonId) {
    this.contactPersonId = contactPersonId;
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
}
