package com.aishtek.aishtrack.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import com.aishtek.aishtrack.beans.Expense;

public class ExpenseDAO extends BaseDAO {

  public static void deleteFor(Connection connection, int expenseReportId) throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement("delete from expenses where expense_report_id = ?");
    preparedStatement.setInt(1, expenseReportId);
    preparedStatement.executeUpdate();
  }

  public static int create(Connection connection, int expenseReportId, Date expenseDate,
      String expenseType, String notes, BigDecimal amount) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into expenses (expense_report_id, expense_date, expense_type, notes, amount) values(?, ?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);

    preparedStatement.setInt(1, expenseReportId);
    preparedStatement.setTimestamp(2, timestampFor(expenseDate));
    preparedStatement.setString(3, expenseType);
    preparedStatement.setString(4, notes);
    preparedStatement.setBigDecimal(5, amount);

    preparedStatement.executeUpdate();

    ResultSet result = preparedStatement.getGeneratedKeys();
    if (result.next()) {
      return result.getInt(1);
    } else {
      throw new SQLException("Expense ID not generted");
    }
  }

  public static ArrayList<Expense> findByExpenseReportId(Connection connection, int expenseReportId)
      throws SQLException {
    String sql =
        "SELECT expenses.id, expense_report_id, expense_date, expense_type, notes, amount, f.location "
            + "FROM expenses left outer join files f on expenses.file_id = f.id "
            + "where expense_report_id = ? order by expense_date asc ";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, expenseReportId);
    ResultSet result = statement.executeQuery();
    ArrayList<Expense> expenses = new ArrayList<Expense>();
    while (result.next()) {
      expenses.add(new Expense(result.getInt(1), result.getInt(2), result.getDate(3),
          result.getString(4), result.getString(5), result.getBigDecimal(6), result.getString(7)));
    }
    return expenses;
  }

  public static void updateFile(Connection connection, int expenseId, int fileId)
      throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement("update expenses set file_id = ? where id = ?");

    preparedStatement.setInt(1, fileId);
    preparedStatement.setInt(2, expenseId);
    preparedStatement.executeUpdate();
  }
}
