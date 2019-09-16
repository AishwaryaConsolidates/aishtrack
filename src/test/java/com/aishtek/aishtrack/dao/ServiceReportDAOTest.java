package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class ServiceReportDAOTest extends BaseIntegrationTest {

  private int workOrderId;
  private String brand = "noBrand";
  private String model = "modeless";
  private String serialNumber = "serial";
  private String notes = "This is a note";
  private Date reportDate = new Date();

  @Test
  public void createServiceReportSavesTheReport() throws SQLException {
    try (Connection connection = getConnection()) {
      // workOrderId = createTestWorkOrder(connection);

      WorkOrder workOrder = WorkOrderDAO.findById(connection, workOrderId);
      Customer customer = CustomerDAO.findById(connection, workOrder.getCustomerId());

      // int serviceReportId =
      // ServiceReportDAO.create(connection, workOrderId,
      // new ServiceReport(customer, notes, brand, model, serialNumber, reportDate));
      // ServiceReport serviceReport = ServiceReportDAO.findById(connection, serviceReportId);
      //
      //
      // assertEquals(serviceReport.getCustomerId(), workOrder.getCustomerId());
      // assertEquals(serviceReport.getAddressId(), customer.getAddressId());
      // assertEquals(serviceReport.getContactPersonId(), customer.getContactPersonId());
      // assertEquals(serviceReport.getStatus(), WorkStatus.CREATED_STATUS);
      // assertEquals(serviceReport.getNotes(), notes);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }

  }

  @Test
  public void markAssignedUpdatesTheWorkOrder() throws SQLException {
    // try (Connection connection = getConnection()) {
    // int workOrderId = createTestWorkOrder(connection);
    // WorkOrderDAO.markAsAssigned(connection, workOrderId);
    //
    // WorkOrder workOrder = WorkOrderDAO.findById(connection, workOrderId);
    // assertEquals(workOrder.getStatus(), WorkStatus.ASSIGNED_STATUS);
    //
    // connection.rollback();
    // } catch (SQLException e) {
    // System.out.println(e);
    // assert (false);
    // }

  }
}
