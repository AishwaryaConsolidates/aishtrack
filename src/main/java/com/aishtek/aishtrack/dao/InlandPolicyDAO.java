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

public class InlandPolicyDAO extends BaseDAO {
  public static HashMap<String, String> getInlandPolicyDetails(Connection connection, int id)
      throws SQLException {
      String sql =
        "SELECT ip.provider, ip.amount, address.street, address.area, address.city, address.pincode FROM inland_policies ip inner join addresses address on ip.address_id = address.id where ip.id = ?";

      PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, id);
      ResultSet result = statement.executeQuery();

      if (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("provider", result.getString(1));
      hashMap.put("amount", result.getBigDecimal(2).toString());
      hashMap.put("street", result.getString(3));
      hashMap.put("area", result.getString(4));
      hashMap.put("city", result.getString(5));
      hashMap.put("pin", result.getString(6));

      return hashMap;
    } else {
      throw new SQLException("No inland policy for Id");
    }
  }

  public static ArrayList<NameId> getCurrentInlandPolicies(Connection connection)
      throws SQLException {
    String sql =
        "SELECT id, provider from inland_policies where start_date <= NOW() and end_date >= NOW()";

    PreparedStatement statement = connection.prepareStatement(sql);
    ResultSet result = statement.executeQuery();

    ArrayList<NameId> inlandPolicies = new ArrayList<NameId>();
    while (result.next()) {
      inlandPolicies.add(new NameId(result.getInt(1), result.getString(2)));
    }
    return inlandPolicies;
  }

  public static ArrayList<DeclarationsReport> getInlandPoliciyReport(
      Connection connection, Date startDate,
      Date endDate)
      throws SQLException {

    String sql = "SELECT id, amount from inland_policies where start_date <= ? and end_date >= ?";

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

  public static BigDecimal getAmountUsed(Connection connection, int inlandPolicyId, Date startDate,
      Date endDate) throws SQLException {
    String sql =
        "SELECT sum(amount) from inland_policy_declarations where inland_policy_id = ? and invoice_date > ? and invoice_date <= ? and deleted = 0";

    PreparedStatement statement = connection.prepareStatement(sql);

    statement.setInt(1, inlandPolicyId);
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
