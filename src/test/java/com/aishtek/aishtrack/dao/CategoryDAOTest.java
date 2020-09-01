package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.Test;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class CategoryDAOTest extends BaseIntegrationTest {

  @Test
  public void testGetCategories() throws Exception {
    try (Connection connection = getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("delete from categories");
      preparedStatement.executeUpdate();

      CategoryDAO.createCategory(connection, "feline");
      CategoryDAO.createCategory(connection, "canine");

      ArrayList<NameId> categories = CategoryDAO.getCategories(connection);
      assertEquals(categories.size(), 2);
      assertEquals(categories.get(0).getName(), "canine");
      assertEquals(categories.get(1).getName(), "feline");
      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void testGetEquipments() throws Exception {
    try (Connection connection = getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement("delete from equipments");
      preparedStatement.executeUpdate();

      int categoryId = CategoryDAO.createCategory(connection, "feline");
      CategoryDAO.createEquipment(connection, categoryId, "tiger");
      CategoryDAO.createEquipment(connection, categoryId, "leopard");

      ArrayList<NameId> equipments = CategoryDAO.getEquipments(connection, categoryId);
      assertEquals(equipments.size(), 2);
      assertEquals(equipments.get(0).getName(), "leopard");
      assertEquals(equipments.get(1).getName(), "tiger");
      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
