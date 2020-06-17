package com.aishtek.aishtrack.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.aishtek.aishtrack.beans.DeclarationsReport;
import com.aishtek.aishtrack.beans.NameId;

public class MarinePolicyDAO extends BaseDAO {
  public static HashMap<String, String> getMarinePolicyDetails(Connection connection, int id)
      throws SQLException {
      String sql =
        "SELECT mp.provider, address.street, address.area, address.city, address.pincode FROM marine_policies mp inner join addresses address on mp.address_id = address.id where mp.id = ?";

      PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, id);
      ResultSet result = statement.executeQuery();

      if (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("provider", "" + result.getString(1));
      hashMap.put("street", "" + result.getString(2));
      hashMap.put("area", "" + result.getString(3));
      hashMap.put("city", "" + result.getString(4));
      hashMap.put("pin", "" + result.getString(5));

      return hashMap;
    } else {
      throw new SQLException("No marine policy for Id");
      }
  }

  public static ArrayList<NameId> getCurrentMarinePolicies(Connection connection)
      throws SQLException {
    String sql =
        "SELECT id, provider from marine_policies where start_date <= NOW() and end_date >= NOW()";

    PreparedStatement statement = connection.prepareStatement(sql);
    ResultSet result = statement.executeQuery();

    ArrayList<NameId> marinePolicies = new ArrayList<NameId>();
    while (result.next()) {
      marinePolicies.add(new NameId(result.getInt(1), result.getString(2)));
    }
    return marinePolicies;
  }

  public static ArrayList<DeclarationsReport> getMarinePoliciyReport(Connection connection,
      Date startDate, Date endDate) throws SQLException {

    String sql = "SELECT id, amount from marine_policies where start_date <= ? and end_date >= ?";

    PreparedStatement statement = connection.prepareStatement(sql);

    statement.setTimestamp(1, beginningOfDayTimestamp(startDate));
    statement.setTimestamp(2, endOfDayTimestamp(endDate));
    ResultSet result = statement.executeQuery();

    ArrayList<DeclarationsReport> policyAmounts = new ArrayList<DeclarationsReport>();
    while (result.next()) {
      DeclarationsReport declarationsReport = new DeclarationsReport();
      declarationsReport.setPolicyId(result.getInt(1));
      declarationsReport.setAmountInsured(result.getBigDecimal(2));
      policyAmounts.add(declarationsReport);
    }
    return policyAmounts;
  }

  public static BigDecimal getAmountUsed(Connection connection, int marinePolicyId, Date startDate,
      Date endDate) throws SQLException {
    String sql =
        "SELECT sum(amount) from marine_policy_declarations where marine_policy_id = ? and invoice_date > ? and invoice_date <= ? and deleted = 0";

    PreparedStatement statement = connection.prepareStatement(sql);

    statement.setInt(1, marinePolicyId);
    statement.setTimestamp(2, beginningOfDayTimestamp(startDate));
    statement.setTimestamp(3, beginningOfDayTimestamp(startDate));

    ResultSet result = statement.executeQuery();

    BigDecimal amount = new BigDecimal(0);
    if (result.next()) {
      amount = result.getBigDecimal(1);
    }
    return amount;
  }
}
