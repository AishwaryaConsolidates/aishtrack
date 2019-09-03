package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.aishtek.aishtrack.beans.SparePart;

public class RecommendedSparePartDAO extends BaseDAO {

  public static int create(Connection connection, SparePart sparePart) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into visit_recommended_spare_parts (visit_id, part_number, description, quantity) values(?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);
      
    preparedStatement.setInt(1, sparePart.getVisitId());
    preparedStatement.setString(2, sparePart.getPartNumber());
    preparedStatement.setString(3, sparePart.getDescription());
    preparedStatement.setInt(4, sparePart.getQuantity());

      preparedStatement.executeUpdate();

      ResultSet result = preparedStatement.getGeneratedKeys();
      if (result.next()) {
      return result.getInt(1);
      } else {
      throw new SQLException("RecommendedSparePart not generted");
      }
  }

  public static void deleteFor(Connection connection, int visitId) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "delete from visit_recommended_spare_parts where visit_id = ?");
    preparedStatement.setInt(1, visitId);
    preparedStatement.executeUpdate();
  }

  public static ArrayList<SparePart> findByVisitId(Connection connection, int visitId)
      throws SQLException {
    String sql =
        "SELECT id, visit_id, part_number, description, quantity FROM visit_recommended_spare_parts where visit_id = ?";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, visitId);
    ResultSet result = statement.executeQuery();
    ArrayList<SparePart> spareParts = new ArrayList<SparePart>();
    while (result.next()) {
      spareParts.add(new SparePart(result.getInt(1), result.getInt(2), result.getString(3),
          result.getString(4), result.getInt(5)));
    }
    return spareParts;
  }
}
