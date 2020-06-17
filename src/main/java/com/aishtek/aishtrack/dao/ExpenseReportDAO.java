package com.aishtek.aishtrack.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.aishtek.aishtrack.beans.ExpenseReport;

public class ExpenseReportDAO extends BaseDAO {

  public static int create(Connection connection, int serviceReportId, int customerId,
      int technicianId, BigDecimal advanceAmount, BigDecimal carryForwardAmount, String location,
      Date advanceAmountDate)
      throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into expense_reports (service_report_id, customer_id, technician_id, advance_amount, carry_forward_amount, location, advance_amount_date) values(?, ?, ?, ?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);

    if (serviceReportId > 0) {
      preparedStatement.setInt(1, serviceReportId);
    } else {
      preparedStatement.setNull(1, java.sql.Types.INTEGER);
    }
    if (customerId > 0) {
      preparedStatement.setInt(2, customerId);
    } else {
      preparedStatement.setNull(2, java.sql.Types.INTEGER);
    }
    preparedStatement.setInt(3, technicianId);
    preparedStatement.setBigDecimal(4, advanceAmount);
    preparedStatement.setBigDecimal(5, carryForwardAmount);
    preparedStatement.setString(6, location);
    preparedStatement.setTimestamp(7, timestampFor(advanceAmountDate));

    preparedStatement.executeUpdate();

    ResultSet result = preparedStatement.getGeneratedKeys();
    if (result.next()) {
      return result.getInt(1);
    } else {
      throw new SQLException("Expense Report ID not generted");
    }
  }

  public static void update(Connection connection, int expenseReportId, BigDecimal advanceAmount,
      BigDecimal carryForwardAmount, String location, Date advanceAmountDate)
      throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement(
            "update expense_reports set advance_amount = ?, carry_forward_amount = ?, location = ?, advance_amount_date = ? where id =  ?");

    preparedStatement.setBigDecimal(1, advanceAmount);
    preparedStatement.setBigDecimal(2, carryForwardAmount);
    preparedStatement.setString(3, location);
    preparedStatement.setTimestamp(4, timestampFor(advanceAmountDate));
    preparedStatement.setInt(5, expenseReportId);

    preparedStatement.executeUpdate();
  }

  public static ExpenseReport findById(Connection connection, int expenseReportId)
      throws SQLException {
    String sql =
        "SELECT er.id, er.service_report_id, er.customer_id, er.technician_id, c.name customer_name, (p.first_name || ' ' || p.last_name) technician_name, er.advance_amount, er.settled, er.carry_forward_amount, er.location, er.advance_amount_date, er.created_at "
            + " FROM expense_reports er left outer join customers c on er.customer_id = c.id "
            + " left outer join technicians t on er.technician_id = t.id left outer join persons p on t.person_id = p.id "
            + " where er.id = ?";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, expenseReportId);
    ResultSet result = statement.executeQuery();
    if (result.next()) {
      ExpenseReport expenseReport =
          new ExpenseReport(result.getInt(1), result.getInt(2), result.getInt(3), result.getInt(4),
              result.getBigDecimal(7), result.getInt(8), result.getBigDecimal(9),
              result.getString(10), dateFor(result.getTimestamp(11)),
              dateFor(result.getTimestamp(12)));
      expenseReport.setExpenses(ExpenseDAO.findByExpenseReportId(connection, expenseReportId));
      expenseReport.setCustomerName(result.getString(5));
      expenseReport.setTechnicianName(result.getString(6));
      return expenseReport;
    } else {
      throw new SQLException("No Expense Report found, ID does not exist");
    }
  }
  public static ArrayList<HashMap<String, String>> getExpenseReportsForSR(Connection connection,
      int serviceReportId) throws SQLException {
    String sql =
        "SELECT er.id, tpr.first_name, tpr.last_name from expense_reports er left outer join technicians t on er.technician_id = t.id left outer join persons tpr on t.person_id = tpr.id where er.deleted = 0 and er.service_report_id = ?";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, serviceReportId);

    ResultSet result = statement.executeQuery();

    ArrayList<HashMap<String, String>> expenseReports = new ArrayList<HashMap<String, String>>();
    while (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("technician", result.getString(2) + " " + result.getString(3));
      expenseReports.add(hashMap);
    }
    return expenseReports;
  }

  public static ArrayList<HashMap<String, String>> getExpenseReportsForCustomer(
      Connection connection, int customerId) throws SQLException {
    String sql =
        "SELECT er.id, tpr.first_name, tpr.last_name from expense_reports er left outer join technicians t on er.technician_id = t.id left outer join persons tpr on t.person_id = tpr.id where er.deleted = 0 and sr.customer_id = ?";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, customerId);

    ResultSet result = statement.executeQuery();

    ArrayList<HashMap<String, String>> expenseReports = new ArrayList<HashMap<String, String>>();
    while (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("technician", result.getString(2) + " " + result.getString(3));
      expenseReports.add(hashMap);
    }
    return expenseReports;
  }

  public static ArrayList<HashMap<String, String>> getExpenseReportsForTechnician(
      Connection connection, int technicianId) throws SQLException {
    String sql =
        "SELECT er.id, tpr.first_name, tpr.last_name from expense_reports er left outer join technicians t on er.technician_id = t.id left outer join persons tpr on t.person_id = tpr.id where er.deleted = 0 and er.technician_id = ?";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, technicianId);

    ResultSet result = statement.executeQuery();

    ArrayList<HashMap<String, String>> expenseReports = new ArrayList<HashMap<String, String>>();
    while (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("technician", result.getString(2) + " " + result.getString(3));
      expenseReports.add(hashMap);
    }
    return expenseReports;
  }

  public static ArrayList<HashMap<String, String>> searchFor(Connection connection,
      int technicianId, int customerId, int settled, Date startDate, Date endDate)
      throws SQLException {
    String sql =
        "SELECT er.id, tpr.first_name, tpr.last_name, c.name, er.settled from expense_reports er left outer join customers c on er.customer_id = c.id left outer join technicians t on er.technician_id = t.id left outer join persons tpr on t.person_id = tpr.id where er.deleted = 0 ";

    if (technicianId > 0) {
      sql += " and er.technician_id = ? ";
    }
    if (customerId > 0) {
      sql += " and er.customer_id = ? ";
    }
    if (settled >= 0) {
      sql += " and er.settled = ? ";
    }
    if (endDate != null) {
      sql += " and er.created_at <= ? ";
    }
    if (startDate != null) {
      sql += " and er.created_at >= ? ";
    }

    PreparedStatement statement = connection.prepareStatement(sql);

    int index = 1;
    if (technicianId > 0) {
      statement.setInt(index, technicianId);
      index++;
    }
    if (customerId > 0) {
      statement.setInt(index, customerId);
      index++;
    }
    if (settled > 0) {
      statement.setInt(index, settled);
      index++;
    }
    if (endDate != null) {
      statement.setTimestamp(index, endOfDayTimestamp(endDate));
      index++;
    }
    if (startDate != null) {
      statement.setTimestamp(index, beginningOfDayTimestamp(startDate));
      index++;
    }
    ResultSet result = statement.executeQuery();

    ArrayList<HashMap<String, String>> expenseReports = new ArrayList<HashMap<String, String>>();
    while (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("technician", result.getString(2) + " " + result.getString(3));
      hashMap.put("customer", result.getString(4));
      hashMap.put("settled", result.getInt(5) == 0 ? "No" : "Yes");
      expenseReports.add(hashMap);
    }
    return expenseReports;
  }

  public static void settle(Connection connection, int expenseReportId) throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement("update expense_reports set settled = 1 where id = ?");
    preparedStatement.setInt(1, expenseReportId);
    preparedStatement.executeUpdate();
  }

  public static void delete(Connection connection, int expenseReportId) throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement("update expense_reports set deleted = 1 where id = ?");
    preparedStatement.setInt(1, expenseReportId);
    preparedStatement.executeUpdate();
  }
}
