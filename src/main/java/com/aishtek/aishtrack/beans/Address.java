package com.aishtek.aishtrack.beans;

public class Address extends BaseBean {

  private String street;
  private String area;
  private String city;
  private String state;
  private String pincode;

  public Address(String street, String area, String city, String state, String pincode) {
    this.street = street;
    this.area = area;
    this.city = city;
    this.state = state;
    this.pincode = pincode;
  }

  public Address(int id, String street, String area, String city, String state, String pincode,
      int deleted) {
    this.id = id;
    this.street = street;
    this.area = area;
    this.city = city;
    this.state = state;
    this.pincode = pincode;
    this.deleted = deleted;
  }

  public String getFullAddress() {
    return street + ", " + area + ", " + city + ", " + state + ", " + pincode;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getPincode() {
    return pincode;
  }

  public void setPincode(String pincode) {
    this.pincode = pincode;
  }

}
