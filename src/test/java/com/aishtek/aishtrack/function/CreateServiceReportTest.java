package com.aishtek.aishtrack.function;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.beans.ServiceReport;
import com.aishtek.aishtrack.beans.Technician;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.dao.CustomerDAO;
import com.aishtek.aishtrack.dao.ServiceReportDAO;
import com.aishtek.aishtrack.dao.WorkOrderDAO;
import com.aishtek.aishtrack.services.EmailSenderService;
import com.aishtek.aishtrack.test.BaseIntegrationTest;
import com.aishtek.aishtrack.utils.WorkStatus;
import com.amazonaws.services.lambda.runtime.Context;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class CreateServiceReportTest extends BaseIntegrationTest {

  private int workOrderId;
  private ArrayList<Integer> technicianIds;

  private String notes = "This is a note";
  @Mocked
  EmailSenderService emailSenderService;
  @Mocked
  Context context;

  @Before
  public void init() throws IOException {
    new Expectations() {
      {
        EmailSenderService.sendEmail((String[]) any, anyString, anyString, anyString);
      }
    };
  }

  @Test
  public void itCreatesABlankServiceReport() {
    try (Connection connection = getConnection()) {
      createArtifacts(connection);

      int serviceReportId = (new CreateServiceReport()).createServiceReport(connection, workOrderId,
          notes, technicianIds);

      ServiceReport serviceReport = ServiceReportDAO.findById(connection, serviceReportId);
      WorkOrder workOrder = WorkOrderDAO.findById(connection, workOrderId);
      Customer customer = CustomerDAO.findById(connection, workOrder.getCustomerId());

      assertEquals(serviceReport.getCustomerId(), workOrder.getCustomerId());
      assertEquals(serviceReport.getAddressId(), customer.getAddressId());
      assertEquals(serviceReport.getContactPersonId(), customer.getContactPersonId());
      assertEquals(serviceReport.getNotes(), notes);
      assertEquals(serviceReport.getStatus(), WorkStatus.CREATED_STATUS);

      connection.rollback();
    } catch (SQLException e) {
      e.printStackTrace();
      assert (false);
    }
  }

  @Test
  public void itAssignsTheTechniciansToTheServiceReport() {
    try (Connection connection = getConnection()) {
      createArtifacts(connection);

      int serviceReportId = (new CreateServiceReport()).createServiceReport(connection, workOrderId,
          notes, technicianIds);

      ArrayList<Technician> technicians =
          ServiceReportDAO.getTechniciansFor(connection, serviceReportId);

      assertEquals(technicians.size(), 2);
      assertEquals(technicians.get(0).getId(), technicians.get(0).getId());
      assertEquals(technicians.get(1).getId(), technicians.get(1).getId());

      connection.rollback();
    } catch (SQLException e) {
      e.printStackTrace();
      assert (false);
    }
  }

  @Test
  public void itUpdatesTheWorkOrderAsAssigned() {
    try (Connection connection = getConnection()) {
      createArtifacts(connection);

      (new CreateServiceReport()).createServiceReport(connection, workOrderId, notes,
          technicianIds);
      WorkOrder workOrder = WorkOrderDAO.findById(connection, workOrderId);

      assertEquals(workOrder.getStatus(), WorkStatus.ASSIGNED_STATUS);

      connection.rollback();
    } catch (SQLException e) {
      e.printStackTrace();
      assert (false);
    }
  }

  @Test
  public void itSendsEmails() throws IOException {
    try (Connection connection = getConnection()) {
      createArtifacts(connection);

      (new CreateServiceReport()).createServiceReport(connection, workOrderId, notes,
          technicianIds);

      new Verifications() {
        {
          EmailSenderService.sendEmail((String[]) any, anyString, anyString, anyString);
          times = 3;
        }
      };

      connection.rollback();
    } catch (SQLException e) {
      e.printStackTrace();
      assert (false);
    }
  }

  private void createArtifacts(Connection connection) throws SQLException {
    workOrderId = createTestWorkOrder(connection);
    technicianIds = new ArrayList<Integer>();
    technicianIds.add(createTestTechnician(connection));
    technicianIds.add(createTestTechnician(connection));
  }
}
