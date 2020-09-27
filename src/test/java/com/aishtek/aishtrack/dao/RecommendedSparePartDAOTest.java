package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.Test;
import com.aishtek.aishtrack.beans.SparePart;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class RecommendedSparePartDAOTest extends BaseIntegrationTest {

  @Test
  public void createFindByTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int visitId = createTestVisit(connection, 0);
      SparePart sparePart = new SparePart(visitId, "partNumber", "description", 42);
      int sportPartId = RecommendedSparePartDAO.create(connection, sparePart);

      ArrayList<SparePart> spareParts = RecommendedSparePartDAO.findByVisitId(connection, visitId);
      assertEquals(spareParts.size(), 1);
      assertEquals(spareParts.get(0).getId(), sportPartId);
      assertEquals(spareParts.get(0).getPartNumber(), "partNumber");
      assertEquals(spareParts.get(0).getQuantity(), 42);
      assertEquals(spareParts.get(0).getVisitId(), visitId);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void deleteForTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int visitId = createTestVisit(connection, 0);
      SparePart sparePart = new SparePart(visitId, "partNumber", "description", 42);
      RecommendedSparePartDAO.create(connection, sparePart);

      ArrayList<SparePart> spareParts = RecommendedSparePartDAO.findByVisitId(connection, visitId);
      assertEquals(spareParts.size(), 1);

      RecommendedSparePartDAO.deleteFor(connection, visitId);
      spareParts = RecommendedSparePartDAO.findByVisitId(connection, visitId);
      assertEquals(spareParts.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
