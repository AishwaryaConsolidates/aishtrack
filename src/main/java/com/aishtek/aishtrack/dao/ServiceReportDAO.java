package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.beans.ServiceReport;
import com.aishtek.aishtrack.beans.Technician;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.utils.Util;

public class ServiceReportDAO extends BaseDAO {

  public static int create(Connection connection, int workOrderId,
      ServiceReport serviceReport)
      throws SQLException {
      PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into service_reports (customer_id, address_id, contact_person_id, category_id, equipment_id, status, status_date, notes, brand, model, serial_number, part_number, report_date, created_at) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);
      preparedStatement.setInt(1, serviceReport.getCustomerId());
      preparedStatement.setInt(2, serviceReport.getAddressId());
      preparedStatement.setInt(3, serviceReport.getContactPersonId());
    preparedStatement.setInt(4, serviceReport.getCategoryId());
    preparedStatement.setInt(5, serviceReport.getEquipmentId());
    preparedStatement.setString(6, serviceReport.getStatus());
    preparedStatement.setTimestamp(7, timestampFor(serviceReport.getStatusDate()));
    preparedStatement.setString(8, serviceReport.getNotes());
    preparedStatement.setString(9, serviceReport.getBrand());
    preparedStatement.setString(10, serviceReport.getModel());
    preparedStatement.setString(11, serviceReport.getSerialNumber());
    preparedStatement.setString(12, serviceReport.getPartNumber());
    preparedStatement.setTimestamp(13, timestampFor(serviceReport.getReportDate()));
    preparedStatement.setTimestamp(14, currentTimestamp());
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

  public static void update(Connection connection, int serviceReportId, int contactPersonId, int categoryId, int equipmentId, String brand, String model, String serialNumber, String partNumber, String notes)
      throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement("update service_reports set contact_person_id = ?, category_id = ?, equipment_id = ?, brand = ?, model = ?, serial_number = ?, part_number = ?, notes = ? where id = ?");
    preparedStatement.setInt(1, contactPersonId);
    preparedStatement.setInt(2, categoryId);
    preparedStatement.setInt(3, equipmentId);
    preparedStatement.setString(4, brand);
    preparedStatement.setString(5, model);
    preparedStatement.setString(6, serialNumber);
    preparedStatement.setString(7, partNumber);
    preparedStatement.setString(8, notes);
    preparedStatement.setInt(9, serviceReportId);
    preparedStatement.executeUpdate();
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

  public static HashMap<String, String> findByCode(Connection connection, String serviceReportCode)
      throws SQLException {
    String sql =
        "SELECT sr.id, sr.report_date, sr.status, sr.status_date, ct.name, eq.name, sr.brand, sr.model, sr.serial_number, sr.part_number, sr.notes, "
            + " c.name, cp.first_name, cp.last_name, cp.designation, cp.email, cp.phone, "
            + " ca.street, ca.area, ca.city, ca.state, ca.pincode"
            + " FROM service_reports sr, customers c, addresses ca, persons cp, categories ct, equipments eq "
            + " WHERE sr.code = ? and sr.customer_id = c.id and sr.address_id = ca.id and sr.contact_person_id = cp.id "
            + " and sr.category_id = ct.id and sr.equipment_id = eq.id ";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setObject(1, java.util.UUID.fromString(serviceReportCode));
    ResultSet result = statement.executeQuery();

    HashMap<String, String> hashMap = new HashMap<String, String>();
    if (result.next()) {
      int serviceReportId = result.getInt(1);
      hashMap.put("id", "" + serviceReportId);
      hashMap.put("reportDate", formatTimestamp(result.getTimestamp(2)));
      hashMap.put("status", result.getString(3));
      hashMap.put("statusDate", formatTimestamp(result.getTimestamp(4)));
      hashMap.put("category", result.getString(5));
      hashMap.put("equipment", result.getString(6));
      hashMap.put("brand", result.getString(7));
      hashMap.put("model", result.getString(8));
      hashMap.put("serialNumber", result.getString(9));
      hashMap.put("partNumber", result.getString(10));
      hashMap.put("notes", result.getString(11));

      hashMap.put("customerName", result.getString(12));
      hashMap.put("contactFirstName", result.getString(13));
      hashMap.put("contactLastName", result.getString(14));
      hashMap.put("contactDesignation", result.getString(15));
      hashMap.put("contactEmail", result.getString(16));
      hashMap.put("contactPhone", result.getString(17));

      hashMap.put("customerStreet", result.getString(18));
      hashMap.put("customerArea", result.getString(19));
      hashMap.put("customerCity", result.getString(20));
      hashMap.put("customerState", result.getString(21));
      hashMap.put("customerPincode", result.getString(22));

      ArrayList<Technician> technicians = getTechniciansFor(connection, serviceReportId);
      String technicianNames = "";
      for (Technician technician : technicians) {
        technicianNames = technicianNames + ", " + technician.getPerson().getFullName();
      }
      hashMap.put("technicians", technicianNames);
      return hashMap;
    } else {
      throw new SQLException("No Service Report found, code does not exist");
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

  public static void update(Connection connection, WorkOrder workOrder) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "update work_orders set customer_id =?, type = ?, notes = ? where id = ?");
    preparedStatement.setInt(1, workOrder.getCustomerId());
    preparedStatement.setString(2, workOrder.getType());
    preparedStatement.setString(3, workOrder.getNotes());
    preparedStatement.setInt(4, workOrder.getId());
    preparedStatement.executeUpdate();
  }

  public static ArrayList<ServiceReport> searchFor(Connection connection, String customerName,
      int customerId, int workOrderId, String[] statuses) throws SQLException {
    String sql =
        "SELECT sr.id, sr.code, sr.status, sr.status_date, c.name from service_reports sr, customers c, work_order_service_reports wosr where sr.customer_id = c.id and sr.deleted = 0 and sr.id = wosr.service_report_id ";
    if (!Util.isNullOrEmpty(statuses)) {
      sql += " and sr.status = ANY (?) ";
    }
    if (!Util.isNullOrEmpty(customerName)) {
      sql += " and (c.name like ? or c.nick_name like ?) ";
    }
    if (customerId > 0) {
      sql += " and c.id = ? ";
    }
    if (workOrderId > 0) {
      sql += " and wosr.work_order_id = ? ";
    }
    sql += " order by status, status_date ";

    PreparedStatement statement = connection.prepareStatement(sql);

    int index = 1;
    if (!Util.isNullOrEmpty(statuses)) {
      statement.setArray(index, connection.createArrayOf("varchar", statuses));
      index++;
    }
    if (!Util.isNullOrEmpty(customerName)) {
      customerName = "%" + customerName + "%";
      statement.setString(index, customerName);
      index++;
      statement.setString(index, customerName);
      index++;
    }
    if (customerId > 0) {
      statement.setInt(index, customerId);
      index++;
    }
    if (workOrderId > 0) {
      statement.setInt(index, workOrderId);
      index++;
    }

    ResultSet result = statement.executeQuery();

    ArrayList<ServiceReport> serviceReports = new ArrayList<ServiceReport>();
    while (result.next()) {
      ServiceReport serviceReport = new ServiceReport(result.getInt(1), result.getString(2),
          result.getString(3), dateFor(result.getTimestamp(4)));
      serviceReport.setCustomerName(result.getString(5));
      serviceReports.add(serviceReport);
    }
    return serviceReports;
  }
}
