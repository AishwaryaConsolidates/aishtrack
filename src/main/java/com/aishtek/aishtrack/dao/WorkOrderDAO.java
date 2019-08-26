package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.utils.Util;
import com.aishtek.aishtrack.utils.WorkStatus;

public class WorkOrderDAO extends BaseDAO {
  public static WorkOrder findById(Connection connection, int workOrderId) throws SQLException {
      String sql =
        "SELECT id, customer_id, contact_person_id, type, status, status_date, notes, deleted, category_id, equipment_id, brand, model, serial_number, part_number FROM work_orders where id = ?";

      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, workOrderId);
      ResultSet result = statement.executeQuery();

      if (result.next()) {
      WorkOrder workOrder = new WorkOrder(result.getInt(1), result.getInt(2), result.getInt(3),
          result.getString(4), result.getString(5), dateFor(result.getTimestamp(6)),
          result.getString(7), result.getInt(8), result.getInt(9), result.getInt(10),
          result.getString(11), result.getString(12), result.getString(13), result.getString(14));
        return workOrder;
      } else {
        throw new SQLException("No Record found, ID does not exist");
      }
  }

  public static int create(Connection connection, WorkOrder workOrder) throws SQLException {
      PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into work_orders (customer_id, contact_person_id, type, status, status_date, notes, created_at, category_id, equipment_id, brand, model, serial_number, part_number) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);
      preparedStatement.setInt(1, workOrder.getCustomerId());
    preparedStatement.setInt(2, workOrder.getContactPersonId());
    preparedStatement.setString(3, workOrder.getType());
    preparedStatement.setString(4, workOrder.getStatus());
    preparedStatement.setTimestamp(5, timestampFor(workOrder.getStatusDate()));
    preparedStatement.setString(6, workOrder.getNotes());
    preparedStatement.setTimestamp(7, currentTimestamp());
    preparedStatement.setInt(8, workOrder.getCategoryId());
    preparedStatement.setInt(9, workOrder.getEquipmentId());
    preparedStatement.setString(10, workOrder.getBrand());
    preparedStatement.setString(11, workOrder.getModel());
    preparedStatement.setString(12, workOrder.getSerialNumber());
    preparedStatement.setString(13, workOrder.getPartNumber());
      preparedStatement.executeUpdate();

    ResultSet result = preparedStatement.getGeneratedKeys();
    if (result.next()) {
      return result.getInt(1);
    } else {
      throw new SQLException("WorkOrder Id not generted");
    }
  }

  public static void markAsAssigned(Connection connection, int workOrderId)
      throws SQLException {
    PreparedStatement preparedStatement = connection
        .prepareStatement("update work_orders set status =?, status_date = ? where id = ?");
    preparedStatement.setString(1, WorkStatus.ASSIGNED_STATUS);
    preparedStatement.setTimestamp(2, timestampFor(currentTimestamp()));
    preparedStatement.setInt(3, workOrderId);
    preparedStatement.executeUpdate();
  }

  public static void update(Connection connection, WorkOrder workOrder) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "update work_orders set customer_id =?, contact_person_id = ?, type = ?, notes = ?, category_id = ?, equipment_id = ?, brand = ?, model = ?, serial_number = ?, part_number = ? where id = ?");
    preparedStatement.setInt(1, workOrder.getCustomerId());
    preparedStatement.setInt(2, workOrder.getContactPersonId());
    preparedStatement.setString(3, workOrder.getType());
    preparedStatement.setString(4, workOrder.getNotes());
    preparedStatement.setInt(5, workOrder.getCategoryId());
    preparedStatement.setInt(6, workOrder.getEquipmentId());
    preparedStatement.setString(7, workOrder.getBrand());
    preparedStatement.setString(8, workOrder.getModel());
    preparedStatement.setString(9, workOrder.getSerialNumber());
    preparedStatement.setString(10, workOrder.getPartNumber());

    preparedStatement.setInt(11, workOrder.getId());
    preparedStatement.executeUpdate();
  }

  public static ArrayList<WorkOrder> searchFor(Connection connection, String customerName,
      int customerId, String[] statuses) throws SQLException {
    String sql =
        "SELECT wo.id, wo.customer_id, wo.contact_person_id, wo.type, wo.status, wo.status_date, wo.notes, wo.deleted, wo.brand, wo.model, wo.serial_number, wo.part_number, c.name, ct.name, eq.name "
            + "from work_orders wo inner join customers c on wo.customer_id = c.id "
            + "left join categories ct on wo.category_id = ct.id "
            + "left join equipments eq on wo.equipment_id = eq.id where wo.deleted = 0 ";

    if (!Util.isNullOrEmpty(statuses)) {
      sql += " and wo.status = ANY (?) ";
    }
    if (!Util.isNullOrEmpty(customerName)) {
      sql += " and (c.name like ? or c.nick_name like ?) ";
    }
    if (customerId > 0) {
      sql += " and c.id = ? ";
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
    }

    ResultSet result = statement.executeQuery();

    ArrayList<WorkOrder> workOrders = new ArrayList<WorkOrder>();
    while (result.next()) {
      WorkOrder workOrder = new WorkOrder(result.getInt(1), result.getInt(2), result.getInt(3),
          result.getString(4), result.getString(5), dateFor(result.getTimestamp(6)),
          result.getString(7), result.getInt(8), 0, 0, result.getString(9), result.getString(10),
          result.getString(11), result.getString(12));
      workOrder.setCustomerName(result.getString(13));
      workOrder.setCategory(result.getString(14));
      workOrder.setEquipment(result.getString(15));
      workOrders.add(workOrder);
    }
    return workOrders;
  }
}
