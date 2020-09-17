package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.junit.Test;
import com.aishtek.aishtrack.beans.MarinePolicyDeclaration;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class MarinePolicyDeclarationDAOTest extends BaseIntegrationTest {

  @Test
  public void createAndFindByIdTest() throws Exception {
    try (Connection connection = getConnection()) {
      int marinePolicyId = createTestMarinePolicy(connection, "Provider 1");
      int supplierId = createSupplier(connection, "oversees");
      int supplierAddressId = createTestAddress(connection);

      MarinePolicyDeclaration marinePolicyDeclaration =
          new MarinePolicyDeclaration(0, marinePolicyId, supplierId, supplierAddressId,
              new BigDecimal(20), "Dollar", "Description", 7, "toLocation", "fromLocation",
              "invoiceNumber", yesterday(), "receiptNumber", yesterday(), 0);

      int marinePolicyDeclarationId =
          MarinePolicyDeclarationDAO.create(connection, marinePolicyDeclaration);

      MarinePolicyDeclaration updatedMarinePolicyDeclaration =
          MarinePolicyDeclarationDAO.findById(connection, marinePolicyDeclarationId);

      assertEquals(updatedMarinePolicyDeclaration.getId(), marinePolicyDeclarationId);
      assertEquals(updatedMarinePolicyDeclaration.getAmount().toString(), "20.00");
      assertEquals(updatedMarinePolicyDeclaration.getSupplierId(), supplierId);
      assertEquals(updatedMarinePolicyDeclaration.getSupplierAddressId(), supplierAddressId);
      assertEquals(updatedMarinePolicyDeclaration.getDescription(), "Description");
      assertEquals(updatedMarinePolicyDeclaration.getQuantity(), 7);
      assertEquals(updatedMarinePolicyDeclaration.getFromLocation(), "fromLocation");
      assertEquals(updatedMarinePolicyDeclaration.getToLocation(), "toLocation");
      assertEquals(updatedMarinePolicyDeclaration.getInvoiceNumber(), "invoiceNumber");
      assertEquals(updatedMarinePolicyDeclaration.getReceiptNumber(), "receiptNumber");
      assertEquals(formatter.format(updatedMarinePolicyDeclaration.getInvoiceDate()),
          formatter.format(yesterday()));
      assertEquals(formatter.format(updatedMarinePolicyDeclaration.getReceiptDate()),
          formatter.format(yesterday()));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void updateTest() throws Exception {
    try (Connection connection = getConnection()) {
      int marinePolicyId = createTestMarinePolicy(connection, "Provider 1");
      int supplierId = createSupplier(connection, "oversees");
      int supplierAddressId = createTestAddress(connection);

      MarinePolicyDeclaration marinePolicyDeclaration =
          new MarinePolicyDeclaration(0, marinePolicyId, supplierId, supplierAddressId,
              new BigDecimal(20), "Dollar", "Description", 7, "toLocation", "fromLocation",
              "invoiceNumber", yesterday(), "receiptNumber", yesterday(), 0);
      int marinePolicyDeclarationId =
          MarinePolicyDeclarationDAO.create(connection, marinePolicyDeclaration);

      MarinePolicyDeclaration createdMarinePolicyDeclaration =
          MarinePolicyDeclarationDAO.findById(connection, marinePolicyDeclarationId);

      createdMarinePolicyDeclaration.setId(marinePolicyDeclarationId);
      createdMarinePolicyDeclaration.setAmount(new BigDecimal(40));
      createdMarinePolicyDeclaration.setDescription("Description 202");
      createdMarinePolicyDeclaration.setQuantity(25);
      createdMarinePolicyDeclaration.setFromLocation("From Location 2");
      createdMarinePolicyDeclaration.setToLocation("To Location 2");
      createdMarinePolicyDeclaration.setInvoiceNumber("Invoice Number 2");
      createdMarinePolicyDeclaration.setReceiptNumber("Receipt Number 2");
      createdMarinePolicyDeclaration.setInvoiceDate(new Date());
      createdMarinePolicyDeclaration.setReceiptDate(yesterday());

      MarinePolicyDeclarationDAO.update(connection, createdMarinePolicyDeclaration);
      MarinePolicyDeclaration updatedMarinePolicyDeclaration =
          MarinePolicyDeclarationDAO.findById(connection, marinePolicyDeclarationId);

      assertEquals(updatedMarinePolicyDeclaration.getId(), marinePolicyDeclarationId);
      assertEquals(updatedMarinePolicyDeclaration.getAmount().toString(), "40.00");
      assertEquals(updatedMarinePolicyDeclaration.getDescription(), "Description 202");
      assertEquals(updatedMarinePolicyDeclaration.getQuantity(), 25);
      assertEquals(updatedMarinePolicyDeclaration.getFromLocation(), "From Location 2");
      assertEquals(updatedMarinePolicyDeclaration.getToLocation(), "To Location 2");
      assertEquals(updatedMarinePolicyDeclaration.getInvoiceNumber(), "Invoice Number 2");
      assertEquals(updatedMarinePolicyDeclaration.getReceiptNumber(), "Receipt Number 2");
      assertEquals(formatter.format(updatedMarinePolicyDeclaration.getInvoiceDate()),
          formatter.format(new Date()));
      assertEquals(formatter.format(updatedMarinePolicyDeclaration.getReceiptDate()),
          formatter.format(yesterday()));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void searchForTest() throws Exception {
    try (Connection connection = getConnection()) {
      int marinePolicyId = createTestMarinePolicy(connection, "Provider 1");
      int supplierId = createSupplier(connection, "oversees");
      int supplierAddressId = createTestAddress(connection);

      MarinePolicyDeclaration marinePolicyDeclaration =
          new MarinePolicyDeclaration(0, marinePolicyId, supplierId, supplierAddressId,
              new BigDecimal(20), "Dollar", "Description", 7, "toLocation", "fromLocation",
              "invoiceNumber", yesterday(), "receiptNumber", yesterday(), 0);
      int marinePolicyDeclarationId =
          MarinePolicyDeclarationDAO.create(connection, marinePolicyDeclaration);

      ArrayList<HashMap<String, String>> results = MarinePolicyDeclarationDAO.searchFor(connection,
          supplierId, yesterday(), tomorrow(), marinePolicyId);
      assertEquals(results.size(), 1);
      assertEquals(results.get(0).get("id"), "" + marinePolicyDeclarationId);

      // supplier id
      results = MarinePolicyDeclarationDAO.searchFor(connection, supplierId + 1, yesterday(),
          tomorrow(), marinePolicyId);
      assertEquals(results.size(), 0);

      // marinePolicyId
      results = MarinePolicyDeclarationDAO.searchFor(connection, supplierId, yesterday(),
          tomorrow(), marinePolicyId + 1);
      assertEquals(results.size(), 0);

      // startDate
      results = MarinePolicyDeclarationDAO.searchFor(connection, supplierId, tomorrow(), today(),
          marinePolicyId);
      assertEquals(results.size(), 0);

      //endDate
      results = MarinePolicyDeclarationDAO.searchFor(connection, supplierId, today(), tomorrow(),
          marinePolicyId);
      assertEquals(results.size(), 0);
      
      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void deleteTest() throws Exception {
    try (Connection connection = getConnection()) {
      int marinePolicyId = createTestMarinePolicy(connection, "Provider 1");
      int supplierId = createSupplier(connection, "oversees");
      int supplierAddressId = createTestAddress(connection);

      MarinePolicyDeclaration marinePolicyDeclaration =
          new MarinePolicyDeclaration(0, marinePolicyId, supplierId, supplierAddressId,
              new BigDecimal(20), "Dollar", "Description", 7, "toLocation", "fromLocation",
              "invoiceNumber", yesterday(), "receiptNumber", yesterday(), 0);
      int marinePolicyDeclarationId =
          MarinePolicyDeclarationDAO.create(connection, marinePolicyDeclaration);

      ArrayList<HashMap<String, String>> results = MarinePolicyDeclarationDAO.searchFor(connection,
          supplierId, yesterday(), tomorrow(), marinePolicyId);
      assertEquals(results.size(), 1);

      MarinePolicyDeclarationDAO.delete(connection, marinePolicyDeclarationId);

      results = MarinePolicyDeclarationDAO.searchFor(connection, supplierId, yesterday(),
          tomorrow(), marinePolicyId);
      assertEquals(results.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
