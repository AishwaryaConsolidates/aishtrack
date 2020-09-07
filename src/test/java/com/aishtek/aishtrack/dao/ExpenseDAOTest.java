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
}
