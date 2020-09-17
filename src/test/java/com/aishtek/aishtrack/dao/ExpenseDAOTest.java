package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Expense;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class ExpenseDAOTest extends BaseIntegrationTest {

  @Test
  public void createTest() throws Exception {
    try (Connection connection = getConnection()) {
      int expenseReportId = getExpenseReport(connection);
      
      int expenseId = ExpenseDAO.create(connection, expenseReportId, new Date(), "Travel",
          "notes on expense", new BigDecimal(300));
      ArrayList<Expense> expenses = ExpenseDAO.findByExpenseReportId(connection, expenseReportId);

      assertEquals(expenses.size(), 1);
      assertEquals(expenses.get(0).getId(), expenseId);

      assertEquals(expenses.get(0).getAmount().toString(), "300.00");
      assertEquals(expenses.get(0).getExpenseType(), "Travel");
      assertEquals(expenses.get(0).getNotes(), "notes on expense");
      assertEquals(expenses.get(0).getExpenseReportId(), expenseReportId);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void deleteForTest() throws Exception {
    try (Connection connection = getConnection()) {
      int expenseReportId = getExpenseReport(connection);

      ExpenseDAO.create(connection, expenseReportId, new Date(), "Travel",
          "notes on expense", new BigDecimal(300));
      ExpenseDAO.create(connection, expenseReportId, new Date(), "Hotel",
          "Hotel Expenses", new BigDecimal(400));

      ArrayList<Expense> expenses = ExpenseDAO.findByExpenseReportId(connection, expenseReportId);
      assertEquals(expenses.size(), 2);

      ExpenseDAO.deleteFor(connection, expenseReportId);
      expenses = ExpenseDAO.findByExpenseReportId(connection, expenseReportId);
      assertEquals(expenses.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void findByExpenseReportIdTest() throws Exception {
    try (Connection connection = getConnection()) {
      int expenseReportId = getExpenseReport(connection);

      int expenseId1 = ExpenseDAO.create(connection, expenseReportId, new Date(), "Travel",
          "notes on expense", new BigDecimal(300));
      int expenseId2 = ExpenseDAO.create(connection, expenseReportId, new Date(), "Hotel",
          "Hotel Expenses", new BigDecimal(400));

      ArrayList<Expense> expenses = ExpenseDAO.findByExpenseReportId(connection, expenseReportId);

      assertEquals(expenses.size(), 2);
      assertEquals(expenses.get(0).getId(), expenseId1);
      assertEquals(expenses.get(0).getAmount().toString(), "300.00");
      assertEquals(expenses.get(0).getExpenseType(), "Travel");
      assertEquals(expenses.get(0).getNotes(), "notes on expense");
      assertEquals(expenses.get(0).getExpenseReportId(), expenseReportId);

      assertEquals(expenses.get(1).getId(), expenseId2);
      assertEquals(expenses.get(1).getAmount().toString(), "400.00");
      assertEquals(expenses.get(1).getExpenseType(), "Hotel");
      assertEquals(expenses.get(1).getNotes(), "Hotel Expenses");
      assertEquals(expenses.get(1).getExpenseReportId(), expenseReportId);
      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
