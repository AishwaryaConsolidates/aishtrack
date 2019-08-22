package com.aishtek.aishtrack.beans;

import java.util.ArrayList;

// Simple name id bean to be used as a common class for drop downs
public class NameId {
    
    private String name;
    private int id;
    
    public NameId(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public NameId() {
    }

  public static ArrayList<NameId> convertCustomersToNameId(ArrayList<Customer> customers) {
    ArrayList<NameId> nameIds = new ArrayList<NameId>();
    for (Customer customer : customers) {
      nameIds.add(new NameId(customer.getId(), customer.getName()));
    }
    return nameIds;
  }

  public static ArrayList<NameId> convertTecniciansToNameId(ArrayList<Technician> technicians) {
    ArrayList<NameId> nameIds = new ArrayList<NameId>();
    for (Technician technician : technicians) {
      nameIds.add(new NameId(technician.getId(), technician.getPerson().getFullName()));
    }
    return nameIds;
  }

    public void setName (String name) {
        this.name = name;
    }

    public String getName () {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
