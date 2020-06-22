package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.aishtek.aishtrack.beans.DomesticRemittance;
import com.aishtek.aishtrack.utils.Util;

public class DomesticRemittanceDAO extends BaseDAO {
  public static DomesticRemittance findById(Connection connection, int id) throws SQLException {
      String sql =
        "SELECT id, from_bank_account_id, from_bank_address_id, supplier_id, supplier_bank_account_id, amount, purpose, signature_date, deleted FROM domestic_remittances where id = ?";

      PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, id);
      ResultSet result = statement.executeQuery();

      if (result.next()) {
      DomesticRemittance domesticRemittance = new DomesticRemittance(result.getInt(1),
          result.getInt(2), result.getInt(3), result.getInt(4), result.getInt(5),
          result.getBigDecimal(6), result.getString(7), result.getDate(8), result.getInt(9));

      return domesticRemittance;
    } else {
      throw new SQLException("No domesticRemittance for Id");
      }
  }

  public static HashMap<String, String> findForPrint(Connection connection, int id)
      throws SQLException {
    String sql =
        "SELECT dr.id, dr.amount, dr.purpose, dr.signature_date, s.name, "
            + " fbad.street, fbad.area, fbad.city, fbad.state, fbad.pincode, "
            + " sb.name, sb.branch, sb.swift_code, sb.account_number, sb.iban, sb.other_details, "
            + " fb.name, fb.branch, fb.swift_code, fb.account_number, fb.iban, fb.other_details "
            + " FROM domestic_remittances dr inner join suppliers s on dr.supplier_id = s.id "
            + " INNER JOIN addresses fbad on dr.from_bank_address_id = fbad.id "
            + " INNER JOIN bank_accounts sb on dr.supplier_bank_account_id = sb.id "
            + " INNER JOIN bank_accounts fb on dr.from_bank_account_id = fb.id "
            + " WHERE dr.id = ? ";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, id);
    ResultSet result = statement.executeQuery();

    HashMap<String, String> hashMap = new HashMap<String, String>();
    if (result.next()) {
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("amount", result.getBigDecimal(2).toString());
      hashMap.put("purpose", result.getString(3));
      hashMap.put("signatureDate", Util.formatDate(result.getDate(4)));
      hashMap.put("supplierName", result.getString(5));

      hashMap.put("fromBankStreet", result.getString(6));
      hashMap.put("fromBankArea", result.getString(7));
      hashMap.put("fromBankCity", result.getString(8));
      hashMap.put("fromBankState", result.getString(9));
      hashMap.put("fromBankPincode", result.getString(10));

      hashMap.put("supplierBank", result.getString(11));
      hashMap.put("supplierBankBranch", result.getString(12));
      hashMap.put("supplierBankSwiftCode", result.getString(13));
      hashMap.put("supplierBankAccountNumber", result.getString(14));
      hashMap.put("supplierBankIban", result.getString(15));
      hashMap.put("supplierBankOtherDetails", result.getString(16));

      hashMap.put("fromBank", result.getString(17));
      hashMap.put("fromBankBranch", result.getString(18));
      hashMap.put("fromBankSwiftCode", result.getString(19));
      hashMap.put("fromBankAccountNumber", result.getString(20));
      hashMap.put("fromBankIban", result.getString(21));
      hashMap.put("fromBankOtherDetails", result.getString(22));

      return hashMap;
    } else {
      throw new SQLException("No domesticRemittance for Id");
    }
  }

  public static int create(Connection connection, DomesticRemittance domesticRemittance)
      throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into domestic_remittances (from_bank_account_id, from_bank_address_id, supplier_id, supplier_bank_account_id, amount, purpose, signature_date) values(?, ?, ?, ?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);

    preparedStatement.setInt(1, domesticRemittance.getFromBankAccountId());
    preparedStatement.setInt(2, domesticRemittance.getFromBankAddressId());
    preparedStatement.setInt(3, domesticRemittance.getSupplierId());
    preparedStatement.setInt(4, domesticRemittance.getSupplierBankAccountId());
    preparedStatement.setBigDecimal(5, domesticRemittance.getAmount());
    preparedStatement.setString(6, domesticRemittance.getPurpose());
    preparedStatement.setTimestamp(7, timestampFor(domesticRemittance.getSignatureDate()));

    preparedStatement.executeUpdate();

    ResultSet result = preparedStatement.getGeneratedKeys();
    if (result.next()) {
      return result.getInt(1);
    } else {
      throw new SQLException("Domestic Remittance ID not generted");
    }
  }

  public static void update(Connection connection, DomesticRemittance domesticRemittance)
      throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "update domestic_remittances set from_bank_account_id = ?, from_bank_address_id = ?, supplier_id = ?, supplier_bank_account_id = ?, amount = ?, purpose = ?, signature_date = ? where id = ?");

    preparedStatement.setInt(1, domesticRemittance.getFromBankAccountId());
    preparedStatement.setInt(2, domesticRemittance.getFromBankAddressId());
    preparedStatement.setInt(3, domesticRemittance.getSupplierId());
    preparedStatement.setInt(4, domesticRemittance.getSupplierBankAccountId());
    preparedStatement.setBigDecimal(5, domesticRemittance.getAmount());
    preparedStatement.setString(6, domesticRemittance.getPurpose());
    preparedStatement.setTimestamp(7, timestampFor(domesticRemittance.getSignatureDate()));

    preparedStatement.setInt(8, domesticRemittance.getId());
    preparedStatement.executeUpdate();
  }

  public static ArrayList<HashMap<String, String>> searchFor(Connection connection, int supplierId,
      Date startDate, Date endDate)
      throws SQLException {
    String sql =
        "SELECT dr.id, s.name, ba.name, dr.signature_date, dr.amount from domestic_remittances dr "
            + "inner join suppliers s on dr.supplier_id = s.id "
            + "inner join bank_accounts ba on dr.supplier_bank_account_id = ba.id "
            + "where dr.deleted = 0 ";

    if (supplierId > 0) {
      sql += " and dr.supplier_id = ? ";
    }
    if (endDate != null) {
      sql += " and dr.signature_date <= ? ";
    }
    if (startDate != null) {
      sql += " and dr.signature_date >= ? ";
    }

    PreparedStatement statement = connection.prepareStatement(sql);

    int index = 1;
    if (supplierId > 0) {
      statement.setInt(index, supplierId);
      index++;
    }
    if (endDate != null) {
      statement.setTimestamp(index, endOfDayTimestamp(endDate));
      index++;
    }
    if (startDate != null) {
      statement.setTimestamp(index, beginningOfDayTimestamp(startDate));
      index++;
    }
    ResultSet result = statement.executeQuery();

    ArrayList<HashMap<String, String>> domesticRemittances =
        new ArrayList<HashMap<String, String>>();
    while (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("supplier", result.getString(2));
      hashMap.put("bank", result.getString(3));
      hashMap.put("date", formatDate(result.getDate(4)));
      hashMap.put("amount", result.getBigDecimal(5).toString());
      domesticRemittances.add(hashMap);
    }
    return domesticRemittances;
  }

  public static void save(Connection connection, DomesticRemittance domesticRemittance)
      throws SQLException {
    if (domesticRemittance.getId() > 0) {
      update(connection, domesticRemittance);
    } else {
      domesticRemittance.setId(create(connection, domesticRemittance));
    }
  }
}
