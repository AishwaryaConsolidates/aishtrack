package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.aishtek.aishtrack.beans.NameId;

public class CategoryDAO extends BaseDAO {

  public static ArrayList<NameId> getCategories(Connection connection) throws SQLException {
    String sql = "SELECT id, name FROM categories order by name asc ";
    PreparedStatement statement = connection.prepareStatement(sql);
    ResultSet result = statement.executeQuery();

    ArrayList<NameId> categories = new ArrayList<NameId>();
    while (result.next()) {
      categories.add(new NameId(result.getInt(1), result.getString(2)));
    }
    return categories;
  }

  public static ArrayList<NameId> getEquipments(Connection connection, int categoryId)
      throws SQLException {
    String sql = "SELECT id, name FROM equipments where category_id = ? order by name asc ";
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, categoryId);
    ResultSet result = statement.executeQuery();

    ArrayList<NameId> equipments = new ArrayList<NameId>();
    while (result.next()) {
      equipments.add(new NameId(result.getInt(1), result.getString(2)));
    }
    return equipments;
  }
}
