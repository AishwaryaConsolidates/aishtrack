package com.aishtek.aishtrack.use_cases;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.aishtek.aishtrack.models.Customer;
import com.aishtek.aishtrack.models.ServiceReport;
import com.aishtek.aishtrack.models.Technician;
import com.aishtek.aishtrack.models.WorkOrder;
import com.aishtek.aishtrack.services.EmailSenderService;
import com.aishtek.aishtrack.test.BaseIntegrationTest;
import com.aishtek.aishtrack.utils.WorkStatus;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class CreateServiceReportTest extends BaseIntegrationTest {

  private WorkOrder workOrder;
  private Technician technician1;
  private Technician technician2;
  private String notes = "This is a note";
  @Mocked
  EmailSenderService emailSenderService;
  @Before
  public void init() throws IOException {
    workOrder = createTestWorkOrder();
    technician1 = createTestTechnician();
    technician2 = createTestTechnician();
    new Expectations() {
      {
        EmailSenderService.sendEmail((String[]) any, anyString, anyString, anyString);
      }
    };
  }

  @Test
  public void itCreatesABlankServiceReport() {
    int [] technicianIds = {technician1.getTechnicianId(), technician2.getTechnicianId()};
    ServiceReport serviceReport =
        CreateServiceReport.process(workOrder.getWorkOrderId(), technicianIds, notes);

    assertEquals(serviceReport.getCustomerId(), workOrder.getCustomerId());
    assertEquals(serviceReport.getAddressId(), workOrder.parent(Customer.class).getAddressId());
    assertEquals(serviceReport.getContactPersonId(),
        workOrder.parent(Customer.class).getContactPersonId());
    assertEquals(serviceReport.getNotes(), notes);
    assertEquals(serviceReport.getStatus(), WorkStatus.CREATED_STATUS);
  }

  @Test
  public void itAssignsTheTechniciansToTheServiceReport() {
    ServiceReport serviceReport =
        CreateServiceReport.process(workOrder.getWorkOrderId(), technicianIds(), notes);

    List<Technician> technicians = serviceReport.getTechnicians();

    assertEquals(technicians.size(), 2);
    assertEquals(technicians.get(0).getTechnicianId(), technician1.getTechnicianId());
    assertEquals(technicians.get(1).getTechnicianId(), technician2.getTechnicianId());
  }

  @Test
  public void itUpdatesTheWorkOrderAsAssigned() {
    CreateServiceReport.process(workOrder.getWorkOrderId(), technicianIds(), notes);

    workOrder.refresh();

    assertEquals(workOrder.getStatus(), WorkStatus.ASSIGNED_STATUS);
  }

  @Test
  public void itSendsEmails() throws IOException {
    CreateServiceReport.process(workOrder.getWorkOrderId(), technicianIds(), notes);


    new Verifications() {
      {
        EmailSenderService.sendEmail((String[]) any, anyString, anyString, anyString);
        times = 3;
      }
    };
  }

  private int[] technicianIds() {
    int[] technicianIds = {technician1.getTechnicianId(), technician2.getTechnicianId()};
    return technicianIds;
  }
}
