package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.aishtek.aishtrack.beans.InlandPolicyDeclaration;

public class InlandPolicyDeclarationDAO extends BaseDAO {
  public static InlandPolicyDeclaration findById(Connection connection, int id)
      throws SQLException {
      String sql =
        "SELECT id, inland_policy_id, customer_id, invoice_number, invoice_date, description, amount, quantity, from_location, to_location, receipt_number, receipt_date, deleted FROM inland_policy_declarations where id = ?";

      PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, id);
      ResultSet result = statement.executeQuery();

      if (result.next()) {
      InlandPolicyDeclaration inlandPolicyDeclaration =
          new InlandPolicyDeclaration(result.getInt(1), result.getInt(2), result.getInt(3),
              result.getBigDecimal(7), result.getString(6), result.getInt(8), result.getString(10),
              result.getString(9), result.getString(4), dateFor(result.getTimestamp(5)),
              result.getString(11), dateFor(result.getTimestamp(12)), result.getInt(13));

      return inlandPolicyDeclaration;
    } else {
      throw new SQLException("No inland policy declaration for Id");
      }
  }


  public static int create(Connection connection, InlandPolicyDeclaration inlandPolicyDeclaration)
      throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into inland_policy_declarations (inland_policy_id, customer_id, invoice_number, invoice_date, description, amount, quantity, from_location, to_location, receipt_number, receipt_date) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS);

    preparedStatement.setInt(1, inlandPolicyDeclaration.getInlandPolicyId());
    preparedStatement.setInt(2, inlandPolicyDeclaration.getCustomerId());
    preparedStatement.setString(3, inlandPolicyDeclaration.getInvoiceNumber());
    preparedStatement.setTimestamp(4, timestampFor(inlandPolicyDeclaration.getInvoiceDate()));
    preparedStatement.setString(5, inlandPolicyDeclaration.getDescription());
    preparedStatement.setBigDecimal(6, inlandPolicyDeclaration.getAmount());
    preparedStatement.setInt(7, inlandPolicyDeclaration.getQuantity());
    preparedStatement.setString(8, inlandPolicyDeclaration.getFromLocation());
    preparedStatement.setString(9, inlandPolicyDeclaration.getToLocation());
    preparedStatement.setString(10, inlandPolicyDeclaration.getReceiptNumber());
    preparedStatement.setTimestamp(11, timestampFor(inlandPolicyDeclaration.getReceiptDate()));

    preparedStatement.executeUpdate();

    ResultSet result = preparedStatement.getGeneratedKeys();
    if (result.next()) {
      return result.getInt(1);
    } else {
      throw new SQLException("Outward Remittance ID not generted");
    }
  }

  public static void update(Connection connection, InlandPolicyDeclaration inlandPolicyDeclaration)
      throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "update inland_policy_declarations set inland_policy_id = ?, customer_id = ?, invoice_number = ?, invoice_date = ?, description = ?, amount = ?, quantity = ?, from_location = ?, to_location = ?, receipt_number = ?, receipt_date = ? where id = ?");

    preparedStatement.setInt(1, inlandPolicyDeclaration.getInlandPolicyId());
    preparedStatement.setInt(2, inlandPolicyDeclaration.getCustomerId());
    preparedStatement.setString(3, inlandPolicyDeclaration.getInvoiceNumber());
    preparedStatement.setTimestamp(4, timestampFor(inlandPolicyDeclaration.getInvoiceDate()));
    preparedStatement.setString(5, inlandPolicyDeclaration.getDescription());
    preparedStatement.setBigDecimal(6, inlandPolicyDeclaration.getAmount());
    preparedStatement.setInt(7, inlandPolicyDeclaration.getQuantity());
    preparedStatement.setString(8, inlandPolicyDeclaration.getFromLocation());
    preparedStatement.setString(9, inlandPolicyDeclaration.getToLocation());
    preparedStatement.setString(10, inlandPolicyDeclaration.getReceiptNumber());
    preparedStatement.setTimestamp(11, timestampFor(inlandPolicyDeclaration.getReceiptDate()));
    preparedStatement.setInt(12, inlandPolicyDeclaration.getId());
    preparedStatement.executeUpdate();
  }

  public static ArrayList<HashMap<String, String>> searchFor(Connection connection, int customerId,
      Date startDate, Date endDate, int inlandPolicyId) throws SQLException {
    String sql =
        "SELECT ipd.id, c.name, ipd.invoice_date, ipd.amount, ipd.from_location, ipd.to_location, ipd.invoice_number, ipd.receipt_number, ipd.receipt_date, ipd.quantity, ipd.description,"
            + " cad.street, cad.area, cad.city, cad.pincode"
            + " from inland_policy_declarations ipd "
            + " inner join customers c on ipd.customer_id = c.id "
            + " inner join addresses cad on c.address_id = cad.id where ipd.deleted = 0 ";

    if (customerId > 0) {
      sql += " and ipd.customer_id = ? ";
    }
    if (endDate != null) {
      sql += " and ipd.invoice_date <= ? ";
    }
    if (startDate != null) {
      sql += " and ipd.invoice_date >= ? ";
    }
    if (inlandPolicyId > 0) {
      sql += " and ipd.inland_policy_id = ? ";
    }

    PreparedStatement statement = connection.prepareStatement(sql);

    int index = 1;
    if (customerId > 0) {
      statement.setInt(index, customerId);
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
    if (inlandPolicyId > 0) {
      statement.setInt(index, inlandPolicyId);
      index++;
    }
    
    ResultSet result = statement.executeQuery();

    ArrayList<HashMap<String, String>> inlandPolicyDeclarations =
        new ArrayList<HashMap<String, String>>();
    while (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("customer", result.getString(2));
      hashMap.put("invoiceDate", formatDate(result.getDate(3)));
      hashMap.put("amount", result.getBigDecimal(4).toString());
      hashMap.put("fromLocation", result.getString(5));
      hashMap.put("toLocation", result.getString(6));
      hashMap.put("invoiceNumber", result.getString(7));
      hashMap.put("receiptNumber", result.getString(8));
      hashMap.put("receiptDate", formatDate(result.getDate(9)));
      hashMap.put("quantity", "" + result.getInt(10));
      hashMap.put("description", result.getString(11));
      hashMap.put("customerStreet", result.getString(12));
      hashMap.put("customerArea", result.getString(13));
      hashMap.put("customerCity", result.getString(14));
      hashMap.put("customerPin", result.getString(15));
      inlandPolicyDeclarations.add(hashMap);
    }
    return inlandPolicyDeclarations;
  }

  public static void save(Connection connection, InlandPolicyDeclaration inlandPolicyDeclaration)
      throws SQLException {
    if (inlandPolicyDeclaration.getId() > 0) {
      update(connection, inlandPolicyDeclaration);
    } else {
      inlandPolicyDeclaration.setId(create(connection, inlandPolicyDeclaration));
    }
  }
}
