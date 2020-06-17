package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.aishtek.aishtrack.beans.OutwardRemittance;
import com.aishtek.aishtrack.utils.Util;

public class OutwardRemittanceDAO extends BaseDAO {
  public static OutwardRemittance findById(Connection connection, int id) throws SQLException {
      String sql =
        "SELECT id, from_bank_account_id, from_bank_address_id, from_address_id, supplier_id, supplier_address_id, supplier_bank_account_id, supplier_bank_address_id, amount, goods, goods_classification_no, country_of_origin, currency, purpose, other_info, signature_place, signature_date, deleted FROM outward_remittances where id = ?";

      PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, id);
      ResultSet result = statement.executeQuery();

      if (result.next()) {
      OutwardRemittance outwardRemittance = new OutwardRemittance(result.getInt(1),
          result.getInt(2), result.getInt(3), result.getInt(4), result.getInt(5), result.getInt(6),
          result.getInt(7), result.getInt(8), result.getBigDecimal(9), result.getString(10),
          result.getString(11), result.getString(12), result.getString(13), result.getString(14),
          result.getString(15), result.getString(16), result.getDate(17), result.getInt(18));

      return outwardRemittance;
    } else {
      throw new SQLException("No outwardRemittance for Id");
      }
  }

  public static HashMap<String, String> findForPrint(Connection connection, int id)
      throws SQLException {
    String sql =
        "SELECT otr.id, otr.amount, otr.goods, otr.goods_classification_no, otr.country_of_origin, otr.currency, otr.purpose, otr.other_info, otr.signature_place, otr.signature_date, "
            + " s.name, sa.street, sa.area, sa.city, sa.state, sa.pincode, "
            + " sbad.street, sbad.area, sbad.city, sbad.state, sbad.pincode, "
            + " fad.street, fad.area, fad.city, fad.state, fad.pincode, "
            + " fbad.street, fbad.area, fbad.city, fbad.state, fbad.pincode, "
            + " sb.name, sb.branch, sb.swift_code, sb.account_number, sb.iban, sb.other_details, "
            + " fb.name, fb.branch, fb.swift_code, fb.account_number, fb.iban, fb.other_details "
            + " FROM outward_remittances otr inner join suppliers s on otr.supplier_id = s.id "
            + " INNER JOIN addresses sa on otr.supplier_address_id = sa.id "
            + " INNER JOIN addresses sbad on otr.supplier_bank_address_id = sbad.id "
            + " INNER JOIN addresses fad on otr.from_address_id = fad.id "
            + " INNER JOIN addresses fbad on otr.from_bank_address_id = fbad.id "
            + " INNER JOIN bank_accounts sb on otr.supplier_bank_account_id = sb.id "
            + " INNER JOIN bank_accounts fb on otr.from_bank_account_id = fb.id "
            + " WHERE otr.id = ? ";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, id);
    ResultSet result = statement.executeQuery();

    HashMap<String, String> hashMap = new HashMap<String, String>();
    if (result.next()) {
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("amount", result.getBigDecimal(2).toString());
      hashMap.put("goods", result.getString(3));
      hashMap.put("goodsClassificationNo", result.getString(4));
      hashMap.put("countryOfOrigin", result.getString(5));
      hashMap.put("currency", result.getString(6));
      hashMap.put("purpose", result.getString(7));
      hashMap.put("otherInfo", result.getString(8));
      hashMap.put("signaturePlace", result.getString(9));
      hashMap.put("signatureDate", Util.formatDate(result.getDate(10)));
      hashMap.put("supplierName", result.getString(11));

      hashMap.put("supplierStreet", result.getString(12));
      hashMap.put("supplierArea", result.getString(13));
      hashMap.put("supplierCity", result.getString(14));
      hashMap.put("supplierState", result.getString(15));
      hashMap.put("supplierPincode", result.getString(16));

      hashMap.put("supplierBankStreet", result.getString(17));
      hashMap.put("supplierBankArea", result.getString(18));
      hashMap.put("supplierBankCity", result.getString(19));
      hashMap.put("supplierBankState", result.getString(20));
      hashMap.put("supplierBankPincode", result.getString(21));

      hashMap.put("fromStreet", result.getString(22));
      hashMap.put("fromArea", result.getString(23));
      hashMap.put("fromCity", result.getString(24));
      hashMap.put("fromState", result.getString(25));
      hashMap.put("fromPincode", result.getString(26));

      hashMap.put("fromBankStreet", result.getString(27));
      hashMap.put("fromBankArea", result.getString(28));
      hashMap.put("fromBankCity", result.getString(29));
      hashMap.put("fromBankState", result.getString(30));
      hashMap.put("fromBankPincode", result.getString(31));

      hashMap.put("supplierBank", result.getString(32));
      hashMap.put("supplierBankBranch", result.getString(33));
      hashMap.put("supplierBankSwiftCode", result.getString(34));
      hashMap.put("supplierBankAccountNumber", result.getString(35));
      hashMap.put("supplierBankIban", result.getString(36));
      hashMap.put("supplierBankOtherDetails", result.getString(37));

      hashMap.put("fromBank", result.getString(38));
      hashMap.put("fromBankBranch", result.getString(39));
      hashMap.put("fromBankSwiftCode", result.getString(40));
      hashMap.put("fromBankAccountNumber", result.getString(41));
      hashMap.put("fromBankIban", result.getString(42));
      hashMap.put("fromBankOtherDetails", result.getString(43));

      return hashMap;
    } else {
      throw new SQLException("No outwardRemittance for Id");
    }
  }

  public static int create(Connection connection, OutwardRemittance outwardRemittance)
      throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into outward_remittances (from_bank_account_id, from_bank_address_id, from_address_id, supplier_id, supplier_address_id, supplier_bank_account_id, supplier_bank_address_id, amount, goods, goods_classification_no, country_of_origin, currency, purpose, other_info, signature_place, signature_date) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);

    preparedStatement.setInt(1, outwardRemittance.getFromBankAccountId());
    preparedStatement.setInt(2, outwardRemittance.getFromBankAddressId());
    preparedStatement.setInt(3, outwardRemittance.getFromAddressId());
    preparedStatement.setInt(4, outwardRemittance.getSupplierId());
    preparedStatement.setInt(5, outwardRemittance.getSupplierAddressId());
    preparedStatement.setInt(6, outwardRemittance.getSupplierBankAccountId());
    preparedStatement.setInt(7, outwardRemittance.getSupplierBankAddressId());
    preparedStatement.setBigDecimal(8, outwardRemittance.getAmount());
    preparedStatement.setString(9, outwardRemittance.getGoods());
    preparedStatement.setString(10, outwardRemittance.getGoodsClassificationNo());
    preparedStatement.setString(11, outwardRemittance.getCountryOfOrigin());
    preparedStatement.setString(12, outwardRemittance.getCurrency());
    preparedStatement.setString(13, outwardRemittance.getPurpose());
    preparedStatement.setString(14, outwardRemittance.getOtherInfo());
    preparedStatement.setString(15, outwardRemittance.getSignaturePlace());
    preparedStatement.setTimestamp(16, timestampFor(outwardRemittance.getSignatureDate()));

    preparedStatement.executeUpdate();

    ResultSet result = preparedStatement.getGeneratedKeys();
    if (result.next()) {
      return result.getInt(1);
    } else {
      throw new SQLException("Outward Remittance ID not generted");
    }
  }

  public static void update(Connection connection, OutwardRemittance outwardRemittance)
      throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "update outward_remittances set from_bank_account_id = ?, from_bank_address_id = ?, from_address_id = ?, supplier_id = ?, supplier_address_id = ?, supplier_bank_account_id = ?, supplier_bank_address_id = ?, amount = ?, goods = ?, goods_classification_no = ?, country_of_origin = ?, currency = ?, purpose = ?, other_info = ?, signature_place = ?, signature_date = ? where id = ?");

    preparedStatement.setInt(1, outwardRemittance.getFromBankAccountId());
    preparedStatement.setInt(2, outwardRemittance.getFromBankAddressId());
    preparedStatement.setInt(3, outwardRemittance.getFromAddressId());
    preparedStatement.setInt(4, outwardRemittance.getSupplierId());
    preparedStatement.setInt(5, outwardRemittance.getSupplierAddressId());
    preparedStatement.setInt(6, outwardRemittance.getSupplierBankAccountId());
    preparedStatement.setInt(7, outwardRemittance.getSupplierBankAddressId());
    preparedStatement.setBigDecimal(8, outwardRemittance.getAmount());
    preparedStatement.setString(9, outwardRemittance.getGoods());
    preparedStatement.setString(10, outwardRemittance.getGoodsClassificationNo());
    preparedStatement.setString(11, outwardRemittance.getCountryOfOrigin());
    preparedStatement.setString(12, outwardRemittance.getCurrency());
    preparedStatement.setString(13, outwardRemittance.getPurpose());
    preparedStatement.setString(14, outwardRemittance.getOtherInfo());
    preparedStatement.setString(15, outwardRemittance.getSignaturePlace());
    preparedStatement.setTimestamp(16, timestampFor(outwardRemittance.getSignatureDate()));

    preparedStatement.setInt(17, outwardRemittance.getId());
    preparedStatement.executeUpdate();
  }

  public static ArrayList<HashMap<String, String>> searchFor(Connection connection, int supplierId,
      Date startDate, Date endDate)
      throws SQLException {
    String sql =
        "SELECT otr.id, s.name, ba.name, otr.signature_date, otr.amount, otr.goods from outward_remittances otr "
            + "inner join suppliers s on otr.supplier_id = s.id "
            + "inner join bank_accounts ba on otr.supplier_bank_account_id = ba.id "
            + "where otr.deleted = 0 ";

    if (supplierId > 0) {
      sql += " and otr.supplier_id = ? ";
    }
    if (endDate != null) {
      sql += " and otr.signature_date <= ? ";
    }
    if (startDate != null) {
      sql += " and otr.signature_date >= ? ";
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

    ArrayList<HashMap<String, String>> outwardRemittances =
        new ArrayList<HashMap<String, String>>();
    while (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("supplier", result.getString(2));
      hashMap.put("bank", result.getString(3));
      hashMap.put("date", formatDate(result.getDate(4)));
      hashMap.put("amount", result.getBigDecimal(5).toString());
      hashMap.put("goods", result.getString(6));
      outwardRemittances.add(hashMap);
    }
    return outwardRemittances;
  }

  public static void save(Connection connection, OutwardRemittance outwardRemittance)
      throws SQLException {
    if (outwardRemittance.getId() > 0) {
      update(connection, outwardRemittance);
    } else {
      outwardRemittance.setId(create(connection, outwardRemittance));
    }
  }
}
