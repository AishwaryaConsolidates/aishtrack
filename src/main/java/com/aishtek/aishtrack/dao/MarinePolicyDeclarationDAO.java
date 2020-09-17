package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.aishtek.aishtrack.beans.MarinePolicyDeclaration;
import com.aishtek.aishtrack.utils.Util;

public class MarinePolicyDeclarationDAO extends BaseDAO {
  public static MarinePolicyDeclaration findById(Connection connection, int id)
      throws SQLException {
      String sql =
        "SELECT id, marine_policy_id, supplier_id, supplier_address_id, invoice_number, invoice_date, description, amount, currency, quantity, from_location, to_location, receipt_number, receipt_date, deleted FROM marine_policy_declarations where id = ?";

      PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, id);
      ResultSet result = statement.executeQuery();

      if (result.next()) {
      MarinePolicyDeclaration marinePolicyDeclaration =
          new MarinePolicyDeclaration(result.getInt(1), result.getInt(2), result.getInt(3),
              result.getInt(4), result.getBigDecimal(8), result.getString(9), result.getString(7),
              result.getInt(10), result.getString(12), result.getString(11), result.getString(5),
              dateFor(result.getTimestamp(6)), result.getString(13),
              dateFor(result.getTimestamp(14)), result.getInt(15));

      return marinePolicyDeclaration;
    } else {
      throw new SQLException("No outwardRemittance for Id");
      }
  }


  public static int create(Connection connection, MarinePolicyDeclaration marinePolicyDeclaration)
      throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into marine_policy_declarations (marine_policy_id, supplier_id, supplier_address_id, invoice_number, invoice_date, description, amount, currency, quantity, from_location, to_location, receipt_number, receipt_date) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);

    preparedStatement.setInt(1, marinePolicyDeclaration.getMarinePolicyId());
    preparedStatement.setInt(2, marinePolicyDeclaration.getSupplierId());
    preparedStatement.setInt(3, marinePolicyDeclaration.getSupplierAddressId());
    preparedStatement.setString(4, marinePolicyDeclaration.getInvoiceNumber());
    preparedStatement.setTimestamp(5, timestampFor(marinePolicyDeclaration.getInvoiceDate()));
    preparedStatement.setString(6, marinePolicyDeclaration.getDescription());
    preparedStatement.setBigDecimal(7, marinePolicyDeclaration.getAmount());
    preparedStatement.setString(8, marinePolicyDeclaration.getCurrency());
    preparedStatement.setInt(9, marinePolicyDeclaration.getQuantity());
    preparedStatement.setString(10, marinePolicyDeclaration.getFromLocation());
    preparedStatement.setString(11, marinePolicyDeclaration.getToLocation());
    preparedStatement.setString(12, marinePolicyDeclaration.getReceiptNumber());
    preparedStatement.setTimestamp(13, timestampFor(marinePolicyDeclaration.getReceiptDate()));

    preparedStatement.executeUpdate();

    ResultSet result = preparedStatement.getGeneratedKeys();
    if (result.next()) {
      return result.getInt(1);
    } else {
      throw new SQLException("Outward Remittance ID not generted");
    }
  }

  public static void update(Connection connection, MarinePolicyDeclaration marinePolicyDeclaration)
      throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "update marine_policy_declarations set marine_policy_id = ?, supplier_id = ?, supplier_address_id = ?, invoice_number = ?, invoice_date = ?, description = ?, amount = ?, currency = ?, quantity = ?, from_location = ?, to_location = ?, receipt_number = ?, receipt_date = ? where id = ?");

    preparedStatement.setInt(1, marinePolicyDeclaration.getMarinePolicyId());
    preparedStatement.setInt(2, marinePolicyDeclaration.getSupplierId());
    preparedStatement.setInt(3, marinePolicyDeclaration.getSupplierAddressId());
    preparedStatement.setString(4, marinePolicyDeclaration.getInvoiceNumber());
    preparedStatement.setTimestamp(5, timestampFor(marinePolicyDeclaration.getInvoiceDate()));
    preparedStatement.setString(6, marinePolicyDeclaration.getDescription());
    preparedStatement.setBigDecimal(7, marinePolicyDeclaration.getAmount());
    preparedStatement.setString(8, marinePolicyDeclaration.getCurrency());
    preparedStatement.setInt(9, marinePolicyDeclaration.getQuantity());
    preparedStatement.setString(10, marinePolicyDeclaration.getFromLocation());
    preparedStatement.setString(11, marinePolicyDeclaration.getToLocation());
    preparedStatement.setString(12, marinePolicyDeclaration.getReceiptNumber());
    preparedStatement.setTimestamp(13, timestampFor(marinePolicyDeclaration.getReceiptDate()));
    preparedStatement.setInt(14, marinePolicyDeclaration.getId());
    preparedStatement.executeUpdate();
  }

  public static ArrayList<HashMap<String, String>> searchFor(Connection connection, int supplierId,
      Date startDate, Date endDate, int marinePolicyId) throws SQLException {
    String sql =
        "SELECT mpd.id, s.name, mpd.invoice_date, mpd.amount, mpd.currency, mpd.from_location, mpd.to_location, mpd.invoice_number, mpd.receipt_number, mpd.receipt_date, mpd.quantity, mpd.description, "
            + " sad.street, sad.area, sad.city, sad.pincode "
            + " from marine_policy_declarations mpd "
            + " inner join suppliers s on mpd.supplier_id = s.id "
            + " inner join addresses sad on mpd.supplier_address_id = sad.id "
            + " where mpd.deleted = 0 ";

    if (supplierId > 0) {
      sql += " and mpd.supplier_id = ? ";
    }
    if (endDate != null) {
      sql += " and mpd.invoice_date <= ? ";
    }
    if (startDate != null) {
      sql += " and mpd.invoice_date >= ? ";
    }
    if (marinePolicyId > 0) {
      sql += " and mpd.marine_policy_id = ? ";
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
    if (marinePolicyId > 0) {
      statement.setInt(index, marinePolicyId);
      index++;
    }
    ResultSet result = statement.executeQuery();

    ArrayList<HashMap<String, String>> marinePolicyDeclarations =
        new ArrayList<HashMap<String, String>>();
    while (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("supplier", result.getString(2));
      hashMap.put("invoiceDate", Util.formatDate(result.getDate(3)));
      hashMap.put("amount", result.getBigDecimal(4).toString());
      hashMap.put("currency", result.getString(5));
      hashMap.put("fromLocation", result.getString(6));
      hashMap.put("toLocation", result.getString(7));
      hashMap.put("invoiceNumber", result.getString(8));
      hashMap.put("receiptNumber", result.getString(9));
      hashMap.put("receiptDate", Util.formatDate(result.getDate(10)));
      hashMap.put("quantity", "" + result.getInt(11));
      hashMap.put("description", result.getString(12));
      hashMap.put("supplierStreet", result.getString(13));
      hashMap.put("supplierArea", result.getString(14));
      hashMap.put("supplierCity", result.getString(15));
      hashMap.put("supplierPin", result.getString(16));

      marinePolicyDeclarations.add(hashMap);
    }
    return marinePolicyDeclarations;
  }

  public static void save(Connection connection, MarinePolicyDeclaration marinePolicyDeclaration)
      throws SQLException {
    if (marinePolicyDeclaration.getId() > 0) {
      update(connection, marinePolicyDeclaration);
    } else {
      marinePolicyDeclaration.setId(create(connection, marinePolicyDeclaration));
    }
  }

  public static void delete(Connection connection, int id) throws SQLException {
    PreparedStatement preparedStatement = connection
        .prepareStatement("update marine_policy_declarations set deleted = 1 where id = ?");
    preparedStatement.setInt(1, id);
    preparedStatement.executeUpdate();
  }
}
