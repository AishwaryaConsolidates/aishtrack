package com.aishtek.aishtrack.beans;

public class Person extends BaseBean {

  private String firstName;
  private String lastName;
  private String designation;
  private String phone;
  private String email;
  private String mobile;
  private String alternatePhone;

  public Person(String firstName, String lastName, String designation, String phone, String email,
      String mobile, String alternatePhone) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.designation = designation;
    this.phone = phone;
    this.email = email;
    this.mobile = mobile;
    this.alternatePhone = alternatePhone;
  }

  public Person(int id, String firstName, String lastName, String designation, String email,
      String phone, int deleted, String mobile, String alternatePhone) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.designation = designation;
    this.phone = phone;
    this.email = email;
    this.deleted = deleted;
    this.mobile = mobile;
    this.alternatePhone = alternatePhone;
  }

  public String getFullPerson() {
    return getFullName() + " (" + designation + "), " + email + ", " + phone + ", " + mobile + ", "
        + alternatePhone;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getAlternatePhone() {
    return alternatePhone;
  }

  public void setAlternatePhone(String alternatePhone) {
    this.alternatePhone = alternatePhone;
  }

}
