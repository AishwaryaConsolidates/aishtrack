package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import com.aishtek.aishtrack.beans.BankAccount;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.services.EncryptionService;

public class BankAccountDAO extends BaseDAO {

  public static ArrayList<NameId> forSupplier(Connection connection, int supplierId)
      throws SQLException {
    String sql =
        "SELECT ba.id, ba.name, ba.branch, ba.account_number from bank_accounts ba, supplier_bank_accounts sba where ba.deleted = 0 and ba.id = sba.bank_account_id and sba.supplier_id = ? ";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, supplierId);
    ResultSet result = statement.executeQuery();

    ArrayList<NameId> bankAccounts = new ArrayList<NameId>();
    while (result.next()) {
      bankAccounts.add(
          new NameId(result.getInt(1),
              (result.getString(2) + " (" + result.getString(3) + ") " + result.getString(4))));
    }
    return bankAccounts;
  }

  public static ArrayList<NameId> getCurrentAishwaryaBankAccounts(Connection connection)
      throws SQLException {
    String sql =
        "SELECT ba.id, ba.name, ba.branch, ba.account_number from bank_accounts ba, aishwarya_bank_accounts aba "
            + "where ba.deleted = 0 and ba.id = aba.bank_account_id and aba.start_date <= NOW() and aba.end_date >= NOW()";

    PreparedStatement statement = connection.prepareStatement(sql);
    ResultSet result = statement.executeQuery();

    ArrayList<NameId> bankAccounts = new ArrayList<NameId>();
    while (result.next()) {
      bankAccounts.add(
          new NameId(result.getInt(1),
              (result.getString(2) + " (" + result.getString(3) + ")" + result.getString(4))));
    }
    return bankAccounts;
  }

  public static ArrayList<NameId> getAddressesFor(Connection connection, int bankAccountId)
      throws SQLException {
    String sql =
        "SELECT a.id, a.area, a.city from bank_account_addresses baa, addresses a where baa.address_id = a.id and baa.bank_account_id = ? ";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, bankAccountId);
    ResultSet result = statement.executeQuery();

    ArrayList<NameId> bankAccountAddresses = new ArrayList<NameId>();
    while (result.next()) {
      bankAccountAddresses.add(
          new NameId(result.getInt(1), (result.getString(2) + " (" + result.getString(3) + ")")));
    }
    return bankAccountAddresses;
  }

  public static int create(Connection connection, String name, String branch, String swiftCode,
      String accountNumber, String iban, String otherDetails, int addressId) throws Exception {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into bank_accounts (name, branch, swift_code, account_number, iban, other_details) values(?, ?, ? ,?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);
    preparedStatement.setString(1, name);
    preparedStatement.setString(2, branch);
    preparedStatement.setString(3, swiftCode);
    preparedStatement.setString(4, accountNumber);
    preparedStatement.setString(5, iban);
    preparedStatement.setString(6, otherDetails);
    preparedStatement.executeUpdate();

    ResultSet result = preparedStatement.getGeneratedKeys();
    if (result.next()) {
      int bankAccountId = result.getInt(1);

      EncryptionService encryptionService = new EncryptionService();
      String encryptedAccountNumber = encryptionService.encrypt(accountNumber, bankAccountId);
      String obfuscatedAccountNumber = encryptionService.obfuscate(accountNumber);

      preparedStatement = connection.prepareStatement(
          "update bank_accounts set account_number = ?, encrypted_account_number = ? where id = ?");

      preparedStatement.setString(1, obfuscatedAccountNumber);
      preparedStatement.setString(2, encryptedAccountNumber);
      preparedStatement.setInt(3, bankAccountId);
      preparedStatement.executeUpdate();

      createBankAccountAddress(connection, bankAccountId, addressId);
      return bankAccountId;
    } else {
      throw new SQLException("Bank Account ID not generted");
    }
  }

  public static void createBankAccountAddress(Connection connection, int bankAccountId,
      int addressId) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into bank_account_addresses (bank_account_id, address_id) values(?, ?)");
    preparedStatement.setInt(1, bankAccountId);
    preparedStatement.setInt(2, addressId);
    preparedStatement.executeUpdate();
  }

  public static void deleteBankAccount(Connection connection, int bankAccountId)
      throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement("update bank_accounts set deleted = 1 where id = ?");
    preparedStatement.setInt(1, bankAccountId);
    preparedStatement.executeUpdate();
  }

  public static void createAishwaryaBankAccount(Connection connection, int bankAccountId,
      Date startDate, Date endDate)
      throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into aishwarya_bank_accounts (bank_account_id, start_date, end_date) values(?, ?, ?)");
    preparedStatement.setInt(1, bankAccountId);
    preparedStatement.setTimestamp(2, timestampFor(startDate));
    preparedStatement.setTimestamp(3, timestampFor(endDate));
    preparedStatement.executeUpdate();
  }

  public static BankAccount findById(Connection connection, int id) throws SQLException {
    String sql =
        "SELECT ba.id, ba.name, ba.branch, ba.swift_code, ba.account_number, ba.iban, ba.other_details, ba.encrypted_account_number, ba.deleted "
            + " FROM bank_accounts ba WHERE ba.id = ? ";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, id);
    ResultSet result = statement.executeQuery();

    if (result.next()) {
      BankAccount bankAccount = new BankAccount(result.getInt(1), result.getString(2),
          result.getString(3), result.getString(4), result.getString(5), result.getString(6),
          result.getString(7), result.getString(8), result.getInt(9));


      return bankAccount;
    } else {
      throw new SQLException("No Bank Account for Id");
    }
  }
}
