package com.aishtek.aishtrack.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({@BelongsTo(parent = Person.class, foreignKeyName = "person_id")})

@Table("technicians")
public class Technician extends Model {

  public int getTechnicianId() {
    return getInteger("id");
  }

  public String getEmailAddress() {
    return parent(Person.class).getEmailAddress();
  }

  public String getFullName() {
    return parent(Person.class).getFullName();
  }
}
