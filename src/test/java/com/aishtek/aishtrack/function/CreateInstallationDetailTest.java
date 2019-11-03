package com.aishtek.aishtrack.function;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.aishtek.aishtrack.test.BaseIntegrationTest;
import com.amazonaws.services.lambda.runtime.Context;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class CreateInstallationDetailTest extends BaseIntegrationTest {

  private int customerId;

  @Mocked
  Context context;

  @Test
  public void itSetsEquipmentAsDamaged() {
    try (Connection connection = getConnection()) {
      createArtifacts(connection);


      connection.rollback();
    } catch (SQLException e) {
      e.printStackTrace();
      assert (false);
    }
  }

  @Test
  public void itSetsEquipmentAsNotDamaged() {
    try (Connection connection = getConnection()) {
      createArtifacts(connection);


      connection.rollback();
    } catch (SQLException e) {
      e.printStackTrace();
      assert (false);
    }
  }

  @Test
  public void itCreatesInstallationDetail() {
    try (Connection connection = getConnection()) {
      createArtifacts(connection);


      connection.rollback();
    } catch (SQLException e) {
      e.printStackTrace();
      assert (false);
    }
  }

  @Test
  public void itModifiesInstallationDetail() {
    try (Connection connection = getConnection()) {
      createArtifacts(connection);


      connection.rollback();
    } catch (SQLException e) {
      e.printStackTrace();
      assert (false);
    }
  }

  @Test
  public void itAppendsInstallationDetail() {
    try (Connection connection = getConnection()) {
      createArtifacts(connection);


      connection.rollback();
    } catch (SQLException e) {
      e.printStackTrace();
      assert (false);
    }
  }

  private void createArtifacts(Connection connection) throws SQLException {
    customerId = this.createTestCustomer(connection);

  }

}
