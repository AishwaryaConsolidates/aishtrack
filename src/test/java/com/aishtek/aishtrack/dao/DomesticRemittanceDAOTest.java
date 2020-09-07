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
import com.aishtek.aishtrack.beans.Address;
import com.aishtek.aishtrack.beans.BankAccount;
import com.aishtek.aishtrack.beans.DomesticRemittance;
import com.aishtek.aishtrack.beans.Supplier;
import com.aishtek.aishtrack.test.BaseIntegrationTest;
import com.aishtek.aishtrack.utils.Util;

public class DomesticRemittanceDAOTest extends BaseIntegrationTest {

  @Test
  public void findByIdAndCreateTest() throws Exception {
    try (Connection connection = getConnection()) {
      int fromBankAccountId = createBankAccount(connection);
      int supplierBankAccountId = createBankAccount(connection);
      int fromBankAddressId = createTestAddress(connection);
      int supplierId = createSupplier(connection, "domestic");
      BigDecimal amount = new BigDecimal(20);
      String purpose = "Test";
      Date signatureDate = new Date();
      DomesticRemittance domesticRemittance = new DomesticRemittance(0, fromBankAccountId,
          fromBankAddressId, supplierId, supplierBankAccountId, amount, purpose, signatureDate, 0);

      int id = DomesticRemittanceDAO.create(connection, domesticRemittance);

      DomesticRemittance createdDomesticRemittance = DomesticRemittanceDAO.findById(connection, id);

      assertEquals(createdDomesticRemittance.getFromBankAccountId(), fromBankAccountId);
      assertEquals(createdDomesticRemittance.getSupplierBankAccountId(), supplierBankAccountId);
      assertEquals(createdDomesticRemittance.getFromBankAddressId(), fromBankAddressId);
      assertEquals(createdDomesticRemittance.getSupplierId(), supplierId);
      assertEquals(createdDomesticRemittance.getAmount().toPlainString(), "20.00");
      assertEquals(createdDomesticRemittance.getPurpose(), purpose);
      DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      assertEquals(formatter.format(createdDomesticRemittance.getSignatureDate()),
          formatter.format(signatureDate));
      assertEquals(createdDomesticRemittance.getDeleted(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void updateTest() throws Exception {
    try (Connection connection = getConnection()) {
      DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

      int id = createDomesticRemittance(connection);
      DomesticRemittance createdDomesticRemittance = DomesticRemittanceDAO.findById(connection, id);

      createdDomesticRemittance.setAmount(new BigDecimal(500));
      createdDomesticRemittance
          .setFromBankAccountId(createdDomesticRemittance.getSupplierBankAccountId());
      createdDomesticRemittance
          .setSupplierBankAccountId(createdDomesticRemittance.getFromBankAccountId());
      int newSupplierId = createSupplier(connection, "domestic");
      createdDomesticRemittance.setSupplierId(newSupplierId);
      createdDomesticRemittance.setPurpose("Update test");
      createdDomesticRemittance.setSignatureDate(formatter.parse("05/09/2020"));

      DomesticRemittanceDAO.update(connection, createdDomesticRemittance);
      createdDomesticRemittance = DomesticRemittanceDAO.findById(connection, id);

      assertEquals(createdDomesticRemittance.getFromBankAccountId(),
          createdDomesticRemittance.getSupplierBankAccountId());
      assertEquals(createdDomesticRemittance.getSupplierBankAccountId(),
          createdDomesticRemittance.getFromBankAccountId());

      assertEquals(createdDomesticRemittance.getSupplierId(), newSupplierId);
      assertEquals(createdDomesticRemittance.getAmount().toPlainString(), "500.00");
      assertEquals(createdDomesticRemittance.getPurpose(), "Update test");

      assertEquals(formatter.format(createdDomesticRemittance.getSignatureDate()), "05/09/2020");
      assertEquals(createdDomesticRemittance.getDeleted(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }


  @Test
  public void findForPrintTest() throws Exception {
    try (Connection connection = getConnection()) {
      int id = createDomesticRemittance(connection);
      DomesticRemittance domesticRemittance = DomesticRemittanceDAO.findById(connection, id);
      Supplier supplier = SupplierDAO.findById(connection, domesticRemittance.getSupplierId());
      BankAccount fromBankAccount =
          BankAccountDAO.findById(connection, domesticRemittance.getFromBankAccountId());
      BankAccount supplierBankAccount =
          BankAccountDAO.findById(connection, domesticRemittance.getSupplierBankAccountId());
      Address fromBankAddress =
          AddressDAO.findById(connection, domesticRemittance.getFromBankAddressId());
      HashMap<String, String> hashMap = DomesticRemittanceDAO.findForPrint(connection, id);
      assertEquals(hashMap.get("id"), "" + id);
      
      assertEquals(hashMap.get("amount"), "20.00");
      assertEquals(hashMap.get("purpose"), "Test");
      assertEquals(hashMap.get("signatureDate"),
          Util.formatDate(domesticRemittance.getSignatureDate()));
      assertEquals(hashMap.get("supplierName"), supplier.getName());

      assertEquals(hashMap.get("fromBankStreet"), fromBankAddress.getStreet());
      assertEquals(hashMap.get("fromBankArea"), fromBankAddress.getArea());
      assertEquals(hashMap.get("fromBankCity"), fromBankAddress.getCity());
      assertEquals(hashMap.get("fromBankState"), fromBankAddress.getState());
      assertEquals(hashMap.get("fromBankPincode"), fromBankAddress.getPincode());

      assertEquals(hashMap.get("supplierBank"), supplierBankAccount.getName());
      assertEquals(hashMap.get("supplierBankBranch"), supplierBankAccount.getBranch());
      assertEquals(hashMap.get("supplierBankSwiftCode"), supplierBankAccount.getSwiftCode());
      assertEquals(hashMap.get("supplierBankAccountNumber"),
          supplierBankAccount.getAccountNumber());
      assertEquals(hashMap.get("supplierBankIban"), supplierBankAccount.getIban());
      assertEquals(hashMap.get("supplierBankOtherDetails"), supplierBankAccount.getOtherDetails());
      assertEquals(hashMap.get("supplierBankAccountNumberEncrypted"),
          supplierBankAccount.getEncryptedAccountNumber());

      assertEquals(hashMap.get("fromBank"), fromBankAccount.getName());
      assertEquals(hashMap.get("fromBankBranch"), fromBankAccount.getBranch());
      assertEquals(hashMap.get("fromBankSwiftCode"), fromBankAccount.getSwiftCode());
      assertEquals(hashMap.get("fromBankAccountNumber"), fromBankAccount.getAccountNumber());
      assertEquals(hashMap.get("fromBankIban"), fromBankAccount.getIban());
      assertEquals(hashMap.get("fromBankOtherDetails"), fromBankAccount.getOtherDetails());
      assertEquals(hashMap.get("fromBankAccountNumberEncrypted"),
          fromBankAccount.getEncryptedAccountNumber());
      assertEquals(hashMap.get("fromrBankAccountId"), "" + fromBankAccount.getId());
      
      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void searchForTest() throws Exception {
    try (Connection connection = getConnection()) {
      int id = createDomesticRemittance(connection);
      DomesticRemittance domesticRemittance = DomesticRemittanceDAO.findById(connection, id);
      
      ArrayList<HashMap<String, String>> results = DomesticRemittanceDAO.searchFor(connection,
          domesticRemittance.getSupplierId(), yesterday(), tomorrow());
      assertEquals(results.size(), 1);
      assertEquals(results.get(0).get("id"), "" + domesticRemittance.getId());

      // start date
      results = DomesticRemittanceDAO.searchFor(connection, domesticRemittance.getSupplierId(),
          tomorrow(), tomorrow());
      assertEquals(results.size(), 0);

      // end date
      results = DomesticRemittanceDAO.searchFor(connection, domesticRemittance.getSupplierId(),
          yesterday(), yesterday());
      assertEquals(results.size(), 0);

      // supplier
      results = DomesticRemittanceDAO.searchFor(connection, domesticRemittance.getSupplierId() + 1,
          yesterday(), tomorrow());
      assertEquals(results.size(), 0);
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
