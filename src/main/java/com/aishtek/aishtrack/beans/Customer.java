package com.aishtek.aishtrack.beans;

import java.util.ArrayList;

public class Customer extends BaseBean {

  private String name;
  private String nickName;
  private int addressId;
  private int contactPersonId;

  // string fields for view
  private String contactPerson;
  private String address;
  private ArrayList<WorkOrder> workOrders;
  private ArrayList<ServiceReport> serviceReports;

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

  public ArrayList<WorkOrder> getWorkOrders() {
    return workOrders;
  }

  public void setWorkOrders(ArrayList<WorkOrder> workOrders) {
    this.workOrders = workOrders;
  }

  public ArrayList<ServiceReport> getServiceReports() {
    return serviceReports;
  }

  public void setServiceReports(ArrayList<ServiceReport> serviceReports) {
    this.serviceReports = serviceReports;
  }
}
