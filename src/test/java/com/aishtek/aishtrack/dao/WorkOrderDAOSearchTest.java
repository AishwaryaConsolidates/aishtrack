package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import com.aishtek.aishtrack.test.BaseIntegrationTest;
import com.aishtek.aishtrack.utils.WorkStatus;

public class WorkOrderDAOSearchTest extends BaseIntegrationTest {

  @Test
  public void searchSearchesByCustomerNameNickName() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      int personId = createTestPerson(connection);

      int workOrderId = createTestWorkOrder(connection, customerId, personId);

      ArrayList<HashMap<String, String>> workOrders =
          WorkOrderDAO.searchFor(connection, "Bajji", 0, null);

      assertEquals(workOrders.size(), 1);
      assertEquals(workOrders.get(0).get("id"), "" + workOrderId);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void searchSearchesByCustomerId() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      int personId = createTestPerson(connection);

      int workOrderId = createTestWorkOrder(connection, customerId, personId);

      ArrayList<HashMap<String, String>> workOrders =
          WorkOrderDAO.searchFor(connection, null, customerId, null);

      assertEquals(workOrders.size(), 1);
      assertEquals(workOrders.get(0).get("id"), "" + workOrderId);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void searchSearchesByStatus() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      int personId = createTestPerson(connection);

      int workOrderId = createTestWorkOrder(connection, customerId, personId);
      String[] searchStatuses = {WorkStatus.CREATED_STATUS};
      ArrayList<HashMap<String, String>> workOrders =
          WorkOrderDAO.searchFor(connection, null, 0, searchStatuses);

      assertEquals(workOrders.size(), 1);
      assertEquals(workOrders.get(0).get("id"), "" + workOrderId);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
