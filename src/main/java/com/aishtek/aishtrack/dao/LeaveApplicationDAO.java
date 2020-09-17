package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.aishtek.aishtrack.beans.LeaveApplication;
import com.amazonaws.util.StringUtils;

public class LeaveApplicationDAO extends BaseDAO {

  public static LeaveApplication findById(Connection connection, int leaveApplicationId)
      throws SQLException {

    String sql =
        "SELECT la.id, la.from_date, la.to_date, la.technician_id, (p.first_name || ' ' || p.last_name) technician_name, la.reason, la.status, la.signature, la.signature_date, la.deleted "
            + " FROM leave_applications la "
            + " left outer join technicians t on la.technician_id = t.id left outer join persons p on t.person_id = p.id "
            + " where la.id = ?";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, leaveApplicationId);
    ResultSet result = statement.executeQuery();
    if (result.next()) {
      LeaveApplication leaveApplication =
          new LeaveApplication(result.getInt(1), result.getInt(4), dateFor(result.getTimestamp(2)),
              dateFor(result.getTimestamp(3)), result.getString(6), result.getString(7),
              result.getString(8), dateFor(result.getTimestamp(9)), result.getInt(10));

      leaveApplication.setTechnicianName(result.getString(5));
      return leaveApplication;
    } else {
      throw new SQLException("No Expense Report found, ID does not exist");
    }
  }

  public static ArrayList<HashMap<String, String>> searchFor(Connection connection,
      int technicianId, Date startDate, Date endDate, String status) throws SQLException {
    String sql =
        "SELECT la.id, la.from_date, la.to_date, (p.first_name || ' ' || p.last_name) technician_name, la.status, (1 + DATE_PART('day', to_date - from_date)) "
            + " FROM leave_applications la "
            + " left outer join technicians t on la.technician_id = t.id left outer join persons p on t.person_id = p.id "
            + " where la.deleted = 0 ";

    if (technicianId > 0) {
      sql += " and la.technician_id = ? ";
    }
    if (endDate != null) {
      sql += " and la.from_date <= ? ";
    }
    if (startDate != null) {
      sql += " and la.from_date >= ? ";
    }
    if (StringUtils.hasValue(status)) {
      sql += " and la.status = ? ";
    }
    PreparedStatement statement = connection.prepareStatement(sql);

    int index = 1;
    if (technicianId > 0) {
      statement.setInt(index, technicianId);
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
    if (StringUtils.hasValue(status)) {
      statement.setString(index, status);
      index++;
    }
    ResultSet result = statement.executeQuery();

    ArrayList<HashMap<String, String>> leaveApplications = new ArrayList<HashMap<String, String>>();
    while (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("fromDate", formatTimestamp(result.getTimestamp(2)));
      hashMap.put("toDate", formatTimestamp(result.getTimestamp(3)));
      hashMap.put("technician", result.getString(4));
      hashMap.put("status", result.getString(5));
      hashMap.put("noOfDays", "" + result.getInt(6));
      leaveApplications.add(hashMap);
    }
    return leaveApplications;
  }

  public static int save(Connection connection, LeaveApplication leaveApplication)
      throws SQLException {
    if (leaveApplication.getId() > 0) {
      update(connection, leaveApplication);
      return leaveApplication.getId();
    } else {
      leaveApplication.setStatus("processing");
      leaveApplication.setId(create(connection, leaveApplication));
      return leaveApplication.getId();
    }
  }

  public static int create(Connection connection, LeaveApplication leaveApplication)
      throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into leave_applications (technician_id, from_date, to_date, reason, status) values(?, ?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);

    preparedStatement.setInt(1, leaveApplication.getTechnicianId());
    preparedStatement.setTimestamp(2, timestampFor(leaveApplication.getFromDate()));
    preparedStatement.setTimestamp(3, timestampFor(leaveApplication.getToDate()));
    preparedStatement.setString(4, leaveApplication.getReason());
    preparedStatement.setString(5, leaveApplication.getStatus());

    preparedStatement.executeUpdate();

    ResultSet result = preparedStatement.getGeneratedKeys();
    if (result.next()) {
      return result.getInt(1);
    } else {
      throw new SQLException("Leave Application ID not generted");
    }
  }

  private static void update(Connection connection, LeaveApplication leaveApplication)
      throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "update leave_applications set technician_id = ?, from_date = ?, to_date = ?, reason = ?, status = ?, signature =?, signature_date = ? where id =  ?");

    preparedStatement.setInt(1, leaveApplication.getTechnicianId());
    preparedStatement.setTimestamp(2, timestampFor(leaveApplication.getFromDate()));
    preparedStatement.setTimestamp(3, timestampFor(leaveApplication.getToDate()));
    preparedStatement.setString(4, leaveApplication.getReason());
    preparedStatement.setString(5, leaveApplication.getStatus());
    preparedStatement.setString(6, leaveApplication.getSignature());
    preparedStatement.setTimestamp(7, timestampFor(leaveApplication.getSignatureDate()));
    preparedStatement.setInt(8, leaveApplication.getId());

    preparedStatement.executeUpdate();
  }
}
