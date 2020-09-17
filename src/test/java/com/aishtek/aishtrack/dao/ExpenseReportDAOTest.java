package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.junit.Test;
import com.aishtek.aishtrack.beans.ExpenseReport;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class ExpenseReportDAOTest extends BaseIntegrationTest {

  DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

  @Test
  public void createAndFindByTest() throws Exception {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      int serviceReportId = createTestServiceReport(connection, customerId, 0, 0);

      int technicianId = createTestTechnician(connection);
      BigDecimal advanceAmount = new BigDecimal(20);
      BigDecimal carryForwardAmount = new BigDecimal(5);
      String location = "Basement";
      Date advanceAmountDate = new Date();
      int expenseReportId = ExpenseReportDAO.create(connection, serviceReportId, customerId,
          technicianId, advanceAmount, carryForwardAmount, location, advanceAmountDate);
      
      ExpenseReport expenseReport = ExpenseReportDAO.findById(connection, expenseReportId);

      assertEquals(expenseReport.getCustomerId(), customerId);
      assertEquals(expenseReport.getCustomerName(), "Bajji Corner");
      assertEquals(expenseReport.getTechnicianId(), technicianId);
      assertEquals(expenseReport.getTechnicianName(), "Asterix Gaul");
      assertEquals(expenseReport.getServiceReportId(), serviceReportId);
      assertEquals(expenseReport.getAdvanceAmount().toString(), "20.00");
      assertEquals(expenseReport.getCarryForwardAmount().toString(), "5.00");
      assertEquals(expenseReport.getLocation(), "Basement");
      assertEquals(formatter.format(expenseReport.getAdvanceAmountDate()),
          formatter.format(new Date()));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void updateTest() throws Exception {
    try (Connection connection = getConnection()) {
      int expenseReportId = getExpenseReport(connection);

      ExpenseReportDAO.update(connection, expenseReportId, new BigDecimal(42), new BigDecimal(42),
          "New Location", yesterday());
      ExpenseReport expenseReport = ExpenseReportDAO.findById(connection, expenseReportId);

      assertEquals(expenseReport.getAdvanceAmount().toString(), "42.00");
      assertEquals(expenseReport.getCarryForwardAmount().toString(), "42.00");
      assertEquals(expenseReport.getLocation(), "New Location");
      assertEquals(formatter.format(expenseReport.getAdvanceAmountDate()),
          formatter.format(yesterday()));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getExpenseReportsForSRTest() throws Exception {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      int serviceReportId = createTestServiceReport(connection, customerId, 0, 0);
      int technicianId = createTestTechnician(connection);

      int expenseReportId = ExpenseReportDAO.create(connection, serviceReportId, customerId,
          technicianId, new BigDecimal(20), new BigDecimal(5), "Basement", new Date());

      ArrayList<HashMap<String, String>> results =
          ExpenseReportDAO.getExpenseReportsForSR(connection, serviceReportId);

      assertEquals(results.size(), 1);
      assertEquals(results.get(0).get("technician"), "Asterix Gaul");
      assertEquals(results.get(0).get("id"), "" + expenseReportId);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void searchForTest() throws Exception {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      int serviceReportId = createTestServiceReport(connection, customerId, 0, 0);
      int technicianId = createTestTechnician(connection);
      int settled = 0;
      int expenseReportId = ExpenseReportDAO.create(connection, serviceReportId, customerId,
          technicianId, new BigDecimal(20), new BigDecimal(5), "Basement", new Date());

      ArrayList<HashMap<String, String>> results =
          ExpenseReportDAO.searchFor(connection, technicianId, customerId, settled, yesterday(),
              tomorrow());

      assertEquals(results.size(), 1);
      assertEquals(results.get(0).get("technician"), "Asterix Gaul");
      assertEquals(results.get(0).get("id"), "" + expenseReportId);

      // different technician
      results = ExpenseReportDAO.searchFor(connection, technicianId + 1, customerId, settled,
          yesterday(), tomorrow());
      assertEquals(results.size(), 0);

      // different customer
      results = ExpenseReportDAO.searchFor(connection, technicianId, customerId + 1, settled,
          yesterday(), tomorrow());
      assertEquals(results.size(), 0);

      // start date
      results = ExpenseReportDAO.searchFor(connection, technicianId, customerId + 1, settled,
          tomorrow(), tomorrow());
      assertEquals(results.size(), 0);

      // end date
      results = ExpenseReportDAO.searchFor(connection, technicianId, customerId + 1, settled,
          yesterday(), yesterday());
      assertEquals(results.size(), 0);

      // settled
      results = ExpenseReportDAO.searchFor(connection, technicianId, customerId, settled + 1,
          yesterday(), tomorrow());
      assertEquals(results.size(), 0);


      ExpenseReportDAO.settle(connection, expenseReportId);
      results = ExpenseReportDAO.searchFor(connection, technicianId, customerId, settled + 1,
          yesterday(), tomorrow());
      assertEquals(results.size(), 1);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void settleTest() throws Exception {
    try (Connection connection = getConnection()) {
      int expenseReportId = getExpenseReport(connection);

      // settled
      ArrayList<HashMap<String, String>> results =
          ExpenseReportDAO.searchFor(connection, 0, 0, 1, yesterday(), tomorrow());
      assertEquals(results.size(), 0);

      ExpenseReportDAO.settle(connection, expenseReportId);

      results = ExpenseReportDAO.searchFor(connection, 0, 0, 1, yesterday(), tomorrow());
      assertEquals(results.size(), 1);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void deleteTest() throws Exception {
    try (Connection connection = getConnection()) {

      int expenseReportId = getExpenseReport(connection);

      ArrayList<HashMap<String, String>> results =
          ExpenseReportDAO.searchFor(connection, 0, 0, 0, yesterday(), tomorrow());
      assertEquals(results.size(), 1);

      ExpenseReportDAO.delete(connection, expenseReportId);
      results = ExpenseReportDAO.searchFor(connection, 0, 0, 0, yesterday(), tomorrow());
      assertEquals(results.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
