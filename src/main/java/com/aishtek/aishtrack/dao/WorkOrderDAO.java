package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.utils.WorkStatus;

public class WorkOrderDAO extends BaseDAO {
  public static WorkOrder findById(Connection connection, int workOrderId) throws SQLException {
      String sql =
          "SELECT id, customer_id, type, status, status_date, notes, deleted FROM work_orders where id = ?";

      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, workOrderId);
      ResultSet result = statement.executeQuery();

      if (result.next()) {
        WorkOrder workOrder = new WorkOrder(result.getInt(1), result.getInt(2), result.getString(3),
            result.getString(4), dateFor(result.getTimestamp(5)), result.getString(6),
            result.getInt(7));
        return workOrder;
      } else {
        throw new SQLException("No Record found, ID does not exist");
      }
  }

  public static int create(Connection connection, WorkOrder workOrder) throws SQLException {
      PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into work_orders (customer_id, type, status, status_date, notes, created_at) values(?, ?, ?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);
      preparedStatement.setInt(1, workOrder.getCustomerId());
      preparedStatement.setString(2, workOrder.getType());
      preparedStatement.setString(3, workOrder.getStatus());
      preparedStatement.setTimestamp(4, timestampFor(workOrder.getStatusDate()));
      preparedStatement.setString(5, workOrder.getNotes());
      preparedStatement.setTimestamp(6, currentTimestamp());
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
}
