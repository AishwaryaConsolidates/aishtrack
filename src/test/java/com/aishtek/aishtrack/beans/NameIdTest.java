package com.aishtek.aishtrack.beans;

import static org.junit.Assert.assertEquals;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.Test;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class NameIdTest extends BaseIntegrationTest {

  @Test
  public void convertCustomersToNameIdTest() throws SQLException {
    ArrayList<Customer> customers = new ArrayList<Customer>();
    Customer customer1 = new Customer(1, "Customer Abc", "Abc");
    Customer customer2 = new Customer(1, "Customer Xyz", "Xyz");
    Customer customer3 = new Customer(1, "Customer Qwe", "Qwe");
    customers.add(customer1);
    customers.add(customer2);
    customers.add(customer3);

    ArrayList<NameId> nameIds = NameId.convertCustomersToNameId(customers);
    assertEquals(nameIds.size(), 3);
    assertEquals(nameIds.get(0).getName(), "Customer Abc");
    assertEquals(nameIds.get(1).getName(), "Customer Xyz");
    assertEquals(nameIds.get(2).getName(), "Customer Qwe");
  }

  @Test
  public void convertTecniciansToNameIdTest() throws SQLException {
    ArrayList<Technician> technicians = new ArrayList<Technician>();
    Technician technician1 = new Technician(1, "Customer", "Abc", "Bowler");
    Technician technician2 = new Technician(1, "Customer", "Xyz", "Batsman");
    Technician technician3 = new Technician(1, "Customer", "Qwe", "Wicket Keeper");

    technicians.add(technician1);
    technicians.add(technician2);
    technicians.add(technician3);

    ArrayList<NameId> nameIds = NameId.convertTecniciansToNameId(technicians);
    assertEquals(nameIds.size(), 3);
    assertEquals(nameIds.get(0).getName(), "Customer Abc");
    assertEquals(nameIds.get(1).getName(), "Customer Xyz");
    assertEquals(nameIds.get(2).getName(), "Customer Qwe");
  }
}
