package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.aishtek.aishtrack.beans.Visit;

public class VisitDAO extends BaseDAO {

  public static int create(Connection connection, Visit visit) throws SQLException {
      PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into visits (visit_date, complaint, findings, work_done, customer_remarks, service_report_id) values(?, ?, ?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);
      
      preparedStatement.setTimestamp(1, timestampFor(visit.getVisitDate()));
    preparedStatement.setString(2, visit.getComplaint());
      preparedStatement.setString(3, visit.getFindings());
      preparedStatement.setString(4, visit.getWorkDone());
      preparedStatement.setString(5, visit.getCustomerRemarks());
    preparedStatement.setInt(6, visit.getServiceReportId());

      preparedStatement.executeUpdate();

      ResultSet result = preparedStatement.getGeneratedKeys();
      if (result.next()) {
      return result.getInt(1);
      } else {
        throw new SQLException("Visit ID not generted");
      }
  }

  public static void update(Connection connection, Visit visit) throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement(
            "update visits set visit_date = ?, complaint =?, findings =?, work_done =?, customer_remarks =? where id = ?");
    preparedStatement.setTimestamp(1, timestampFor(visit.getVisitDate()));
    preparedStatement.setString(2, visit.getComplaint());
    preparedStatement.setString(3, visit.getFindings());
    preparedStatement.setString(4, visit.getWorkDone());
    preparedStatement.setString(5, visit.getCustomerRemarks());
    preparedStatement.setInt(6, visit.getId());
    preparedStatement.executeUpdate();
  }

  public static Visit findById(Connection connection, int visitId) throws SQLException {
    String sql = "SELECT id, service_report_id, visit_date, complaint, findings, work_done, customer_remarks FROM visits where id = ?";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, visitId);
    ResultSet result = statement.executeQuery();
    if (result.next()) {
      return new Visit(result.getInt(1), result.getInt(2), dateFor(result.getTimestamp(3)),
          result.getString(4), result.getString(5), result.getString(6), result.getString(7));
    } else {
      throw new SQLException("No visit found, ID does not exist");
    }
  }

  public static ArrayList<HashMap<String, String>> getVisits(Connection connection,
      int serviceReportId) throws SQLException {
    String sql =
        "SELECT id, visit_date from visits where service_report_id = ? order by visit_date desc ";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, serviceReportId);

    ResultSet result = statement.executeQuery();

    ArrayList<HashMap<String, String>> visits = new ArrayList<HashMap<String, String>>();
    while (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("visitDate", formatTimestamp(result.getTimestamp(2)));
      visits.add(hashMap);
    }
    return visits;
  }

  public static ArrayList<HashMap<String, String>> getVisitFiles(Connection connection, int visitId)
      throws SQLException {
    String sql =
        "SELECT vf.id, f.name, f.location from files f, visit_files vf where f.id = vf.file_id and vf.visit_id = ? order by f.upload_date desc ";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, visitId);

    ResultSet result = statement.executeQuery();

    ArrayList<HashMap<String, String>> files = new ArrayList<HashMap<String, String>>();
    while (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("name", result.getString(2));
      hashMap.put("location", result.getString(3));
      files.add(hashMap);
    }
    return files;
  }

  public static ArrayList<HashMap<String, String>> getScoutingReportFiles(Connection connection,
      int scoutingReportId) throws SQLException {
    String sql =
        "SELECT vf.id, f.name, f.location from files f, visit_files vf, visits v where f.id = vf.file_id and vf.visit_id = v.id and v.scouting_report_id = ? order by f.upload_date desc ";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, scoutingReportId);

    ResultSet result = statement.executeQuery();

    ArrayList<HashMap<String, String>> files = new ArrayList<HashMap<String, String>>();
    while (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("name", result.getString(2));
      hashMap.put("location", result.getString(3));
      files.add(hashMap);
    }
    return files;
  }
}
