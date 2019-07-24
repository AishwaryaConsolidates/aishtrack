package com.aishtek.aishtrack.test;

import java.sql.Timestamp;
import java.util.Date;
import org.javalite.activejdbc.test.DBSpec;
import com.aishtek.aishtrack.models.Address;
import com.aishtek.aishtrack.models.Customer;
import com.aishtek.aishtrack.models.Person;
import com.aishtek.aishtrack.models.Technician;
import com.aishtek.aishtrack.models.WorkOrder;
import com.aishtek.aishtrack.utils.WorkStatus;

public class BaseIntegrationTest extends DBSpec {

  public Person createTestPerson() {
    return Person.createIt("first_name", "Asterix", "last_name", "Gaul", "designation",
        "Troubleshooter", "email", "asterix@aishtek.tst", "phone", "9999999999");
  }

  public Address createTestAddress() {
    return Address.createIt("street", "Gaul Street", "city", "Gaul", "pincode", "55555");
  }

  public Customer createTestCustomer() {
    return Customer.createIt("name", "The Cafe", "nick_name", "Cafe", "contact_person_id",
        createTestPerson().getPersonId(), "address_id", createTestAddress().getAddressId());
  }

  public WorkOrder createTestWorkOrder() {
    return WorkOrder.createIt("type", "service_report", "customer_id",
        createTestCustomer().getCustomerId(), "status_date", currentTimestamp(), "status",
        WorkStatus.CREATED_STATUS, "notes", "Some Note about WO");
  }

  public Technician createTestTechnician() {
    return Technician.createIt("person_id", createTestPerson().getPersonId());
  }
  public static Timestamp currentTimestamp() {
    return new Timestamp(new Date().getTime());
  }
}
