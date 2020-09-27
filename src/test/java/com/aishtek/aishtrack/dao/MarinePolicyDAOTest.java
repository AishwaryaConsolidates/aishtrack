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
import com.aishtek.aishtrack.beans.MarinePolicyDeclaration;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class MarinePolicyDAOTest extends BaseIntegrationTest {

  DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

  @Test
  public void getMarinePoliciyReportsTest() throws Exception {
    try (Connection connection = getConnection()) {
      int marinePolicyId = createTestMarinePolicy(connection, "Provider 1");
      int marinePolicyId2 = createTestMarinePolicy(connection, "Provider 2");

      ArrayList<DeclarationsReport> reports =
          MarinePolicyDAO.getMarinePoliciyReports(connection, new Date(), new Date());
      assertEquals(reports.size(), 2);
      assertEquals(reports.get(0).getPolicyId(), marinePolicyId);
      assertEquals(reports.get(1).getPolicyId(), marinePolicyId2);
      assertEquals(reports.get(0).getProvider(), "Provider 1");
      assertEquals(reports.get(1).getProvider(), "Provider 2");

      // start date
      reports = MarinePolicyDAO.getMarinePoliciyReports(connection, yesterday(), new Date());
      assertEquals(reports.size(), 0);

      // start date
      reports = MarinePolicyDAO.getMarinePoliciyReports(connection, new Date(), tomorrow());
      assertEquals(reports.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getCurrentMarinePoliciesTest() throws Exception {
    try (Connection connection = getConnection()) {
      int addressId = createTestAddress(connection);
      int marinePolicyId = MarinePolicyDAO.create(connection, addressId, "Provider 1",
          new BigDecimal(1000), yesterday(), tomorrow());

      ArrayList<NameId> reports = MarinePolicyDAO.getCurrentMarinePolicies(connection);
      assertEquals(reports.size(), 1);
      assertEquals(reports.get(0).getId(), marinePolicyId);
      assertEquals(reports.get(0).getName(), "Provider 1");


      // start date
      runSQL(connection, "update marine_policies set start_date = TIMESTAMP 'tomorrow'");
      reports = MarinePolicyDAO.getCurrentMarinePolicies(connection);
      assertEquals(reports.size(), 0);

      // start date
      runSQL(connection,
          "update marine_policies set start_date = TIMESTAMP 'yesterday', end_date = TIMESTAMP 'yesterday' ");
      reports = MarinePolicyDAO.getCurrentMarinePolicies(connection);
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
      int marinePolicyId = createTestMarinePolicy(connection, "Provider 1");
      int supplierId = createSupplier(connection, "oversees");
      int supplierAddressId = createTestAddress(connection);
      
      MarinePolicyDeclaration marinePolicyDeclaration1 =
          new MarinePolicyDeclaration(0, marinePolicyId, supplierId, supplierAddressId,
              new BigDecimal(20), "Dollar", "Description", 7, "toLocation", "fromLocation",
              "invoiceNumber", yesterday(), "receiptNumber", yesterday(), 0, new BigDecimal(5),
              new BigDecimal(10));

      int marinePolicyDeclarationId =
          MarinePolicyDeclarationDAO.create(connection, marinePolicyDeclaration1);

      MarinePolicyDeclaration marinePolicyDeclaration2 =
          new MarinePolicyDeclaration(0, marinePolicyId, supplierId, supplierAddressId,
              new BigDecimal(20), "Dollar", "Description", 7, "toLocation", "fromLocation",
              "invoiceNumber", yesterday(), "receiptNumber", yesterday(), 0, new BigDecimal(5),
              new BigDecimal(10));
      int marinePolicyDeclarationId2 =
          MarinePolicyDeclarationDAO.create(connection, marinePolicyDeclaration2);

      BigDecimal amountUsed = MarinePolicyDAO.getAmountUsed(connection, marinePolicyId, tomorrow());
      assertEquals(amountUsed.toString(), "40.00");

      // deleted not included
      MarinePolicyDeclarationDAO.delete(connection, marinePolicyDeclarationId);

      amountUsed = MarinePolicyDAO.getAmountUsed(connection, marinePolicyId, tomorrow());
      assertEquals(amountUsed.toString(), "20.00");

      // outside data passed
      MarinePolicyDeclaration outsideDatemarinePolicyDeclaration =
          new MarinePolicyDeclaration(0, marinePolicyId, supplierId, supplierAddressId,
              new BigDecimal(20), "Dollar", "Description", 7, "toLocation", "fromLocation",
              "invoiceNumber", tomorrow(), "receiptNumber", yesterday(), 0, new BigDecimal(5),
              new BigDecimal(10));

      amountUsed = MarinePolicyDAO.getAmountUsed(connection, marinePolicyId, new Date());
      assertEquals(amountUsed.toString(), "20.00");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
