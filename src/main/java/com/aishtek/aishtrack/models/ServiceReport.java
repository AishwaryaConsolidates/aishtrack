package com.aishtek.aishtrack.models;

import java.util.List;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;
import com.aishtek.aishtrack.utils.WorkStatus;

@BelongsToParents({@BelongsTo(parent = Customer.class, foreignKeyName = "customer_id"),
    @BelongsTo(parent = Person.class, foreignKeyName = "contact_person_id"),
    @BelongsTo(parent = Address.class, foreignKeyName = "address_id")})

@Table("service_reports")
public class ServiceReport extends ActiveJDBCBaseModel {

  public int getServiceRportId() {
    return getInteger("id");
  }

  public int getCustomerId() {
    return getInteger("customer_id");
  }

  public String getNotes() {
    return getString("notes");
  }

  public String getStatus() {
    return getString("status");
  }

  public int getAddressId() {
    return getInteger("address_id");
  }

  public int getContactPersonId() {
    return getInteger("contact_person_id");
  }

  public String getCode() {
    return getString("code");
  }

  public static ServiceReport createBlankServiceReport(Customer customer, String notes) {
    return createIt("customer_id", customer.getCustomerId(), "address_id", customer.getAddressId(),
        "contact_person_id", customer.getContactPersonId(), "report_date", currentTimestamp(),
        "status", WorkStatus.CREATED_STATUS, "status_date", currentTimestamp(), "notes", notes);
  }

  public List<Technician> getTechnicians() {
    return Technician.findBySQL(
        "SELECT t.* FROM service_report_technicians st INNER JOIN technicians t ON st.technician_id = t.id WHERE st.service_report_id = ? ORDER BY t.id ASC",
        getId());
  }

  public Customer getCustomer() {
    return parent(Customer.class);
  }
}