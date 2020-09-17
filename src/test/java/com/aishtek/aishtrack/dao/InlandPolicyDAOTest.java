package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.junit.Test;
import com.aishtek.aishtrack.beans.DeclarationsReport;
import com.aishtek.aishtrack.beans.InlandPolicyDeclaration;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class InlandPolicyDAOTest extends BaseIntegrationTest {

  DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

  @Test
  public void getInlandPoliciyReportsTest() throws Exception {
    try (Connection connection = getConnection()) {
      int inlandPolicyId = createTestInlandPolicy(connection, "Provider 1");
      int inlandPolicyId2 = createTestInlandPolicy(connection, "Provider 2");

      ArrayList<DeclarationsReport> reports =
          InlandPolicyDAO.getInlandPoliciyReports(connection, new Date(), new Date());
      assertEquals(reports.size(), 2);
      assertEquals(reports.get(0).getPolicyId(), inlandPolicyId);
      assertEquals(reports.get(1).getPolicyId(), inlandPolicyId2);
      assertEquals(reports.get(0).getProvider(), "Provider 1");
      assertEquals(reports.get(1).getProvider(), "Provider 2");

      // start date
      reports = InlandPolicyDAO.getInlandPoliciyReports(connection, yesterday(), new Date());
      assertEquals(reports.size(), 0);

      // start date
      reports = InlandPolicyDAO.getInlandPoliciyReports(connection, new Date(), tomorrow());
      assertEquals(reports.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getCurrentInlandPoliciesTest() throws Exception {
    try (Connection connection = getConnection()) {
      int addressId = createTestAddress(connection);
      int contactPersonId = createTestPerson(connection);
      int inlandPolicyId = InlandPolicyDAO.create(connection, addressId, contactPersonId,
          "Provider 1", new BigDecimal(1000), yesterday(), tomorrow());

      ArrayList<NameId> reports = InlandPolicyDAO.getCurrentInlandPolicies(connection);
      assertEquals(reports.size(), 1);
      assertEquals(reports.get(0).getId(), inlandPolicyId);
      assertEquals(reports.get(0).getName(), "Provider 1");


      // start date
      runSQL(connection, "update inland_policies set start_date = TIMESTAMP 'tomorrow'");
      reports = InlandPolicyDAO.getCurrentInlandPolicies(connection);
      assertEquals(reports.size(), 0);

      // start date
      runSQL(connection,
          "update inland_policies set start_date = TIMESTAMP 'yesterday', end_date = TIMESTAMP 'yesterday' ");
      reports = InlandPolicyDAO.getCurrentInlandPolicies(connection);
      assertEquals(reports.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getAmountUsedTest() throws Exception {
    try (Connection connection = getConnection()) {
      int inlandPolicyId = createTestInlandPolicy(connection, "Provider 1");
      int customerId = createTestCustomer(connection);

      InlandPolicyDeclaration inlandPolicyDeclaration1 =
          new InlandPolicyDeclaration(0, inlandPolicyId, customerId, new BigDecimal(20), null,
              customerId, null, null, null, yesterday(), null, null, 0);
      InlandPolicyDeclarationDAO.create(connection, inlandPolicyDeclaration1);

      InlandPolicyDeclaration inlandPolicyDeclaration2 =
          new InlandPolicyDeclaration(0, inlandPolicyId, customerId, new BigDecimal(20), null,
              customerId, null, null, null, yesterday(), null, null, 0);
      InlandPolicyDeclarationDAO.create(connection, inlandPolicyDeclaration2);


      BigDecimal amountUsed = InlandPolicyDAO.getAmountUsed(connection, inlandPolicyId, tomorrow());
      assertEquals(amountUsed.toString(), "40.00");

      // deleted not included
      InlandPolicyDeclaration deletedDeclaration =
          new InlandPolicyDeclaration(0, inlandPolicyId, customerId, new BigDecimal(20), null,
              customerId, null, null, null, yesterday(), null, null, 0);
      int deletedDeclarationId =
          InlandPolicyDeclarationDAO.create(connection, deletedDeclaration);
      InlandPolicyDeclarationDAO.delete(connection, deletedDeclarationId);

      amountUsed = InlandPolicyDAO.getAmountUsed(connection, inlandPolicyId, tomorrow());
      assertEquals(amountUsed.toString(), "40.00");

      // outside data passed
      InlandPolicyDeclaration tomorrowDeclaration =
          new InlandPolicyDeclaration(0, inlandPolicyId, customerId, new BigDecimal(20), null,
              customerId, null, null, null, tomorrow(), null, null, 0);
      InlandPolicyDeclarationDAO.create(connection, tomorrowDeclaration);

      amountUsed = InlandPolicyDAO.getAmountUsed(connection, inlandPolicyId, new Date());
      assertEquals(amountUsed.toString(), "40.00");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
