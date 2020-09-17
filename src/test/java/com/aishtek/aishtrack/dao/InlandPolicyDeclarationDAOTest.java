package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.junit.Test;
import com.aishtek.aishtrack.beans.InlandPolicyDeclaration;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class InlandPolicyDeclarationDAOTest extends BaseIntegrationTest {

  DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

  @Test
  public void createAndFindByIdTest() throws Exception {
    try (Connection connection = getConnection()) {
      int inlandPolicyId = createTestInlandPolicy(connection, "Provider 1");
      int customerId = createTestCustomer(connection);

      InlandPolicyDeclaration inlandPolicyDeclaration = new InlandPolicyDeclaration(0,
          inlandPolicyId, customerId, new BigDecimal(20), "Description 101", 5, "To Location",
          "From Location", "Invoice Number", yesterday(), "Receipt Number", new Date(), 0);
      int inlandPolicyDeclarationId =
          InlandPolicyDeclarationDAO.create(connection, inlandPolicyDeclaration);

      InlandPolicyDeclaration updatedInlandPolicyDeclaration =
          InlandPolicyDeclarationDAO.findById(connection, inlandPolicyDeclarationId);

      assertEquals(updatedInlandPolicyDeclaration.getId(), inlandPolicyDeclarationId);
      assertEquals(updatedInlandPolicyDeclaration.getAmount().toString(), "20.00");
      assertEquals(updatedInlandPolicyDeclaration.getCustomerId(), customerId);
      assertEquals(updatedInlandPolicyDeclaration.getDescription(), "Description 101");
      assertEquals(updatedInlandPolicyDeclaration.getQuantity(), 5);
      assertEquals(updatedInlandPolicyDeclaration.getFromLocation(), "From Location");
      assertEquals(updatedInlandPolicyDeclaration.getToLocation(), "To Location");
      assertEquals(updatedInlandPolicyDeclaration.getInvoiceNumber(), "Invoice Number");
      assertEquals(updatedInlandPolicyDeclaration.getReceiptNumber(), "Receipt Number");
      assertEquals(formatter.format(updatedInlandPolicyDeclaration.getInvoiceDate()),
          formatter.format(yesterday()));
      assertEquals(formatter.format(updatedInlandPolicyDeclaration.getReceiptDate()),
          formatter.format(new Date()));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void updateTest() throws Exception {
    try (Connection connection = getConnection()) {
      int inlandPolicyId = createTestInlandPolicy(connection, "Provider 1");
      int customerId = createTestCustomer(connection);

      InlandPolicyDeclaration inlandPolicyDeclaration = new InlandPolicyDeclaration(0,
          inlandPolicyId, customerId, new BigDecimal(20), "Description 101", 5, "To Location",
          "From Location", "Invoice Number", yesterday(), "Receipt Number", new Date(), 0);
      int inlandPolicyDeclarationId =
          InlandPolicyDeclarationDAO.create(connection, inlandPolicyDeclaration);

      InlandPolicyDeclaration createdInlandPolicyDeclaration =
          InlandPolicyDeclarationDAO.findById(connection, inlandPolicyDeclarationId);

      createdInlandPolicyDeclaration.setId(inlandPolicyDeclarationId);
      createdInlandPolicyDeclaration.setAmount(new BigDecimal(40));
      createdInlandPolicyDeclaration.setCustomerId(customerId);
      createdInlandPolicyDeclaration.setDescription("Description 202");
      createdInlandPolicyDeclaration.setQuantity(25);
      createdInlandPolicyDeclaration.setFromLocation("From Location 2");
      createdInlandPolicyDeclaration.setToLocation("To Location 2");
      createdInlandPolicyDeclaration.setInvoiceNumber("Invoice Number 2");
      createdInlandPolicyDeclaration.setReceiptNumber("Receipt Number 2");
      createdInlandPolicyDeclaration.setInvoiceDate(new Date());
      createdInlandPolicyDeclaration.setReceiptDate(yesterday());

      InlandPolicyDeclarationDAO.update(connection, createdInlandPolicyDeclaration);
      InlandPolicyDeclaration updatedInlandPolicyDeclaration =
          InlandPolicyDeclarationDAO.findById(connection, inlandPolicyDeclarationId);

      assertEquals(updatedInlandPolicyDeclaration.getId(), inlandPolicyDeclarationId);
      assertEquals(updatedInlandPolicyDeclaration.getAmount().toString(), "40.00");
      assertEquals(updatedInlandPolicyDeclaration.getCustomerId(), customerId);
      assertEquals(updatedInlandPolicyDeclaration.getDescription(), "Description 202");
      assertEquals(updatedInlandPolicyDeclaration.getQuantity(), 25);
      assertEquals(updatedInlandPolicyDeclaration.getFromLocation(), "From Location 2");
      assertEquals(updatedInlandPolicyDeclaration.getToLocation(), "To Location 2");
      assertEquals(updatedInlandPolicyDeclaration.getInvoiceNumber(), "Invoice Number 2");
      assertEquals(updatedInlandPolicyDeclaration.getReceiptNumber(), "Receipt Number 2");
      assertEquals(formatter.format(updatedInlandPolicyDeclaration.getInvoiceDate()),
          formatter.format(new Date()));
      assertEquals(formatter.format(updatedInlandPolicyDeclaration.getReceiptDate()),
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
      int inlandPolicyId = createTestInlandPolicy(connection, "Provider 1");
      int customerId = createTestCustomer(connection);

      InlandPolicyDeclaration inlandPolicyDeclaration = new InlandPolicyDeclaration(0,
          inlandPolicyId, customerId, new BigDecimal(20), "Description 101", 5, "To Location",
          "From Location", "Invoice Number", new Date(), "Receipt Number", yesterday(), 0);
      int inlandPolicyDeclarationId =
          InlandPolicyDeclarationDAO.create(connection, inlandPolicyDeclaration);

      ArrayList<HashMap<String, String>> results = InlandPolicyDeclarationDAO.searchFor(connection,
          customerId, yesterday(), tomorrow(), inlandPolicyId);
      assertEquals(results.size(), 1);
      assertEquals(results.get(0).get("id"), "" + inlandPolicyDeclarationId);

      // customer id
      results = InlandPolicyDeclarationDAO.searchFor(connection, customerId + 1, yesterday(),
          tomorrow(), inlandPolicyId);
      assertEquals(results.size(), 0);

      // inlandPolicyId
      results = InlandPolicyDeclarationDAO.searchFor(connection, customerId, yesterday(),
          tomorrow(), inlandPolicyId + 1);
      assertEquals(results.size(), 0);

      // startDate
      results = InlandPolicyDeclarationDAO.searchFor(connection, customerId, tomorrow(), tomorrow(),
          inlandPolicyId);
      assertEquals(results.size(), 0);

      //endDate
      results = InlandPolicyDeclarationDAO.searchFor(connection, customerId, yesterday(),
          yesterday(), inlandPolicyId);
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
      int inlandPolicyId = createTestInlandPolicy(connection, "Provider 1");
      int customerId = createTestCustomer(connection);

      InlandPolicyDeclaration inlandPolicyDeclaration = new InlandPolicyDeclaration(0,
          inlandPolicyId, customerId, new BigDecimal(20), "Description 101", 5, "To Location",
          "From Location", "Invoice Number", new Date(), "Receipt Number", yesterday(), 0);
      int inlandPolicyDeclarationId =
          InlandPolicyDeclarationDAO.create(connection, inlandPolicyDeclaration);

      ArrayList<HashMap<String, String>> results = InlandPolicyDeclarationDAO.searchFor(connection,
          customerId, yesterday(), tomorrow(), inlandPolicyId);
      assertEquals(results.size(), 1);

      InlandPolicyDeclarationDAO.delete(connection, inlandPolicyDeclarationId);

      results = InlandPolicyDeclarationDAO.searchFor(connection, customerId, yesterday(),
          tomorrow(), inlandPolicyId);
      assertEquals(results.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
