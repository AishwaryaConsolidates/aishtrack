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

  public static int createCategory(Connection connection, String category) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into categories (name) values (?)", PreparedStatement.RETURN_GENERATED_KEYS);
    preparedStatement.setString(1, category);

    int affectedRows = preparedStatement.executeUpdate();
    if (affectedRows == 0) {
      throw new SQLException("Creating category failed, no rows affected.");
    }
    try (ResultSet result = preparedStatement.getGeneratedKeys()) {
      if (result.next()) {
        return result.getInt(1);
      } else {
        throw new SQLException("category Id not generted");
      }
    } catch (SQLException sqle) {
      throw sqle;
    }
  }

  public static int createEquipment(Connection connection, int categoryId, String equipment)
      throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement("insert into equipments (category_id, name) values (?, ?)",
            PreparedStatement.RETURN_GENERATED_KEYS);
    preparedStatement.setInt(1, categoryId);
    preparedStatement.setString(2, equipment);

    int affectedRows = preparedStatement.executeUpdate();
    if (affectedRows == 0) {
      throw new SQLException("Creating equipment failed, no rows affected.");
    }
    try (ResultSet result = preparedStatement.getGeneratedKeys()) {
      if (result.next()) {
        return result.getInt(1);
      } else {
        throw new SQLException("equipment Id not generted");
      }
    } catch (SQLException sqle) {
      throw sqle;
    }
  }
}
