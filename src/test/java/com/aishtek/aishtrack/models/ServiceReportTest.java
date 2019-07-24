package com.aishtek.aishtrack.models;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.aishtek.aishtrack.test.BaseIntegrationTest;
import com.aishtek.aishtrack.utils.WorkStatus;

public class ServiceReportTest extends BaseIntegrationTest {

  private Customer customer;
  private String notes = "This is a note";
    

  @Before
  public void createClients() {
    customer = createTestCustomer();
  }

  @Test
  public void testCreateBlankServiceReport() {
    ServiceReport.createBlankServiceReport(customer, notes);
    ServiceReport serviceReport = (ServiceReport) ServiceReport.findAll().get(0);

    assertEquals(serviceReport.getCustomerId(), customer.getCustomerId());
    assertEquals(serviceReport.getAddressId(), customer.getAddressId());
    assertEquals(serviceReport.getContactPersonId(), customer.getContactPersonId());
    assertEquals(serviceReport.getNotes(), notes);
    assertEquals(serviceReport.getStatus(), WorkStatus.CREATED_STATUS);
    }
}
