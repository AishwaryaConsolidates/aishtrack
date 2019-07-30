package com.aishtek.aishtrack.beans;

public class CustomerPerson extends BaseBean {

  private int customerId;
  private int personId;

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public int getPersonId() {
    return personId;
  }

  public void setPersonId(int personId) {
    this.personId = personId;
  }
}
