package com.aishtek.aishtrack.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("persons")
public class Person extends Model {
  public int getPersonId() {
    return getInteger("id");
  }

  public String getEmailAddress() {
    return getString("email");
  }

  public String getFullName() {
    return getString("first_name") + " " + getString("last_name");
  }
}
