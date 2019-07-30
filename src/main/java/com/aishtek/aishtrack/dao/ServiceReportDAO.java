package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.beans.ServiceReport;
import com.aishtek.aishtrack.beans.Technician;

public class ServiceReportDAO extends BaseDAO {

  public static int create(Connection connection, int workOrderId,
      ServiceReport serviceReport)
      throws SQLException {
      PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into service_reports (customer_id, address_id, contact_person_id, status, status_date, notes, created_at) values(?, ?, ?, ?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);
      preparedStatement.setInt(1, serviceReport.getCustomerId());
      preparedStatement.setInt(2, serviceReport.getAddressId());
      preparedStatement.setInt(3, serviceReport.getContactPersonId());
      preparedStatement.setString(4, serviceReport.getStatus());
      preparedStatement.setTimestamp(5, timestampFor(serviceReport.getStatusDate()));
      preparedStatement.setString(6, serviceReport.getNotes());
      preparedStatement.setTimestamp(7, currentTimestamp());
      preparedStatement.executeUpdate();

      ResultSet result = preparedStatement.getGeneratedKeys();
      if (result.next()) {
        int serviceReportId = result.getInt(1);
        preparedStatement = connection.prepareStatement(
            "insert into work_order_service_reports (work_order_id, service_report_id) values(?, ?)");
        preparedStatement.setInt(1, workOrderId);
        preparedStatement.setInt(2, serviceReportId);
        preparedStatement.executeUpdate();

        return serviceReportId;
      } else {
        throw new SQLException("Service Report ID not generted");
      }
  }

  public static ServiceReport findById(Connection connection, int serviceReportId)
      throws SQLException {
    String sql =
        "SELECT id, code, customer_id, address_id, contact_person_id, report_date, status, status_date, brand, model, serial_number, service_rating, notes, deleted FROM service_reports where id = ?";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, serviceReportId);
    ResultSet result = statement.executeQuery();

    if (result.next()) {
      ServiceReport serviceReport = new ServiceReport(result.getInt(1), result.getString(2),
          result.getInt(3), result.getInt(4), result.getInt(5), dateFor(result.getTimestamp(6)),
          result.getString(7), dateFor(result.getTimestamp(8)), result.getString(9),
          result.getString(10), result.getString(11), result.getInt(12), result.getString(13),
          result.getInt(14));

      return serviceReport;
    } else {
      throw new SQLException("No Service Report found, ID does not exist");
    }
  }

  public static void assignTechniciansToServiceReport(Connection connection, int serviceReportId,
      ArrayList<Integer> technicianIds) throws SQLException {

      PreparedStatement statement = connection
          .prepareStatement("delete from service_report_technicians where service_report_id = ?");
      statement.setInt(1, serviceReportId);
      statement.executeUpdate();

      statement = connection.prepareStatement(
          "insert into service_report_technicians (service_report_id, technician_id) values (?, ?)");
      for (int technicianId : technicianIds) {
        statement.setInt(1, serviceReportId);
      statement.setInt(2, technicianId);
        statement.executeUpdate();
      }
  }

  public static ArrayList<Technician> getTechniciansFor(Connection connection, int serviceReportId)
      throws SQLException {
    String sql =
        "SELECT t.person_id, p.first_name, p.last_name, p.designation, p.phone, p.email FROM technicians t, persons p, service_report_technicians srt where srt.technician_id = t.id and t.person_id = p.id and srt.service_report_id = ?";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, serviceReportId);
    ResultSet result = statement.executeQuery();

    ArrayList<Technician> technicians = new ArrayList<Technician>();
    while (result.next()) {
      Technician technician = new Technician(result.getInt(1));
      technician.setPerson(new Person(result.getString(2), result.getString(3), result.getString(4),
          result.getString(5), result.getString(6)));
      technicians.add(technician);
    }
    return technicians;
  }
}
