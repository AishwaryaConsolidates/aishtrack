package com.aishtek.aishtrack.beans;

public class Technician extends BaseBean {

  private int personId;

  // display fields
  private Person person;

  public Technician(int id, int personId, int deleted) {
    this.id = id;
    this.personId = personId;
    this.deleted = deleted;
  }

  public Technician(int personId) {
    this.personId = personId;
  }

  public int getPersonId() {
    return personId;
  }

  public void setPersonId(int personId) {
    this.personId = personId;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }
}
