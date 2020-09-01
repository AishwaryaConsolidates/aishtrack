package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.Test;
import com.aishtek.aishtrack.beans.BankAccount;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.test.BaseIntegrationTest;

public class BankAccountDAOTest extends BaseIntegrationTest {

  @Test
  public void testForSupplierReturnsAccountsForTheSupplier() throws Exception {
    try (Connection connection = getConnection()) {
      int[] supplierBank1 = createSupplierAndBankAccount(connection, "domestic");
      @SuppressWarnings("unused")
      int[] supplierBank2 = createSupplierAndBankAccount(connection, "domestic");

      ArrayList<NameId> bankAccounts = BankAccountDAO.forSupplier(connection, supplierBank1[0]);

      assertEquals(bankAccounts.size(), 1);
      assertEquals(bankAccounts.get(0).getId(), supplierBank1[1]);

      // deleted accounts
      BankAccountDAO.deleteBankAccount(connection, supplierBank1[1]);
      bankAccounts = BankAccountDAO.forSupplier(connection, supplierBank1[0]);
      assertEquals(bankAccounts.size(), 0);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void testGetCurrentAishwaryaBankAccounts() throws Exception {
    try (Connection connection = getConnection()) {
      int aishwaryaBankAccountId1 = createBankAccount(connection);
      int aishwaryaBankAccountId2 = createBankAccount(connection);
      
      BankAccountDAO.createAishwaryaBankAccount(connection, aishwaryaBankAccountId1, yesterday(),
          tomorrow());
      BankAccountDAO.createAishwaryaBankAccount(connection, aishwaryaBankAccountId2, yesterday(),
          tomorrow());

      ArrayList<NameId> aishwaryaBankAccounts =
          BankAccountDAO.getCurrentAishwaryaBankAccounts(connection);

      assertEquals(aishwaryaBankAccounts.size(), 2);
      assertEquals(aishwaryaBankAccounts.get(0).getId(), aishwaryaBankAccountId1);
      assertEquals(aishwaryaBankAccounts.get(1).getId(), aishwaryaBankAccountId2);
    }
  }

  @Test
  public void testGetCurrentAishwaryaBankAccountsDoesNotGetOldAccounts() throws Exception {
    try (Connection connection = getConnection()) {
      int aishwaryaBankAccountId1 = createBankAccount(connection);
      int aishwaryaBankAccountId2 = createBankAccount(connection);

      BankAccountDAO.createAishwaryaBankAccount(connection, aishwaryaBankAccountId1, yesterday(),
          tomorrow());
      BankAccountDAO.createAishwaryaBankAccount(connection, aishwaryaBankAccountId2, yesterday(),
          yesterday());

      ArrayList<NameId> aishwaryaBankAccounts =
          BankAccountDAO.getCurrentAishwaryaBankAccounts(connection);

      assertEquals(aishwaryaBankAccounts.size(), 1);
      assertEquals(aishwaryaBankAccounts.get(0).getId(), aishwaryaBankAccountId1);
    }
  }

  @Test
  public void testGetAddressesFor() throws Exception {
    try (Connection connection = getConnection()) {
      int bankAccountId = createBankAccount(connection);
      int anotherAddressId = createTestAddress(connection);
      BankAccountDAO.createBankAccountAddress(connection, bankAccountId, anotherAddressId);
      
      ArrayList<NameId> bankAccountAddresses =
          BankAccountDAO.getAddressesFor(connection, bankAccountId);
      assertEquals(bankAccountAddresses.size(), 2);
    }
  }

  @Test
  public void testCreate() throws Exception {
    String name = "ABC Bank";
    String branch = "ZXCV Branch";
    String swiftCode = "ASDFGHJKL";
    String accountNumber = "1234567890";
    String iban = "POIUYT";
    String otherDetails = "qwe asd zxc bnm ghj tyu iop";

    try (Connection connection = getConnection()) {
      int addressId = createTestAddress(connection);
      int bankAccountId = BankAccountDAO.create(connection, name, branch, swiftCode, accountNumber,
          iban, otherDetails, addressId);

      BankAccount bankAccount = BankAccountDAO.findById(connection, bankAccountId);

      assertEquals(bankAccount.getId(), bankAccountId);
      assertEquals(bankAccount.getName(), name);
      assertEquals(bankAccount.getBranch(), branch);
      assertEquals(bankAccount.getSwiftCode(), swiftCode);
      assertEquals(bankAccount.getAccountNumber(), "*****7890");
      assertEquals(bankAccount.getIban(), iban);
      assertEquals(bankAccount.getOtherDetails(), otherDetails);
    }
  }

  @Test
  public void testCreateBankAccountAddress() throws Exception {
    try (Connection connection = getConnection()) {
      int bankAccountId = createBankAccount(connection);
      int anotherAddressId = createTestAddress(connection);
      BankAccountDAO.createBankAccountAddress(connection, bankAccountId, anotherAddressId);

      ArrayList<NameId> bankAccountAddresses =
          BankAccountDAO.getAddressesFor(connection, bankAccountId);
      assertEquals(bankAccountAddresses.size(), 2);
    }
  }

  @Test
  public void testDeleteBankAccount() throws Exception {
    try (Connection connection = getConnection()) {
      int bankAccountId = createBankAccount(connection);
      BankAccountDAO.deleteBankAccount(connection, bankAccountId);
      BankAccount bankAccount = BankAccountDAO.findById(connection, bankAccountId);
      assertEquals(bankAccount.getDeleted(), 1);
    }
  }

  @Test
  public void testCreateAishwaryaBankAccount() throws Exception {
    try (Connection connection = getConnection()) {
      int aishwaryaBankAccountId1 = createBankAccount(connection);
      BankAccountDAO.createAishwaryaBankAccount(connection, aishwaryaBankAccountId1, yesterday(),
          tomorrow());


      ArrayList<NameId> aishwaryaBankAccounts =
          BankAccountDAO.getCurrentAishwaryaBankAccounts(connection);

      assertEquals(aishwaryaBankAccounts.size(), 1);
      assertEquals(aishwaryaBankAccounts.get(0).getId(), aishwaryaBankAccountId1);
    }
  }
}
