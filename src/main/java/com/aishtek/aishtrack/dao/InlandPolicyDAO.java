package com.aishtek.aishtrack.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import com.aishtek.aishtrack.beans.DeclarationsReport;
import com.aishtek.aishtrack.beans.NameId;

public class InlandPolicyDAO extends BaseDAO {

  public static ArrayList<DeclarationsReport> getInlandPoliciyReports(Connection connection,
      Date startDate, Date endDate)
      throws SQLException {
  
    String sql =
        "SELECT ip.id, ip.provider, ip.amount, address.street, address.area, address.city, address.pincode from inland_policies ip inner join addresses address on ip.address_id = address.id where start_date <= ? and end_date >= ?";
  
    PreparedStatement statement = connection.prepareStatement(sql);
  
    statement.setTimestamp(1, beginningOfDayTimestamp(startDate));
    statement.setTimestamp(2, endOfDayTimestamp(endDate));
    ResultSet result = statement.executeQuery();
  
    ArrayList<DeclarationsReport> policyAmounts = new ArrayList<DeclarationsReport>();
    while (result.next()) {
      DeclarationsReport declarationsReport = new DeclarationsReport();
      declarationsReport.setPolicyId(result.getInt(1));
      declarationsReport.setProvider(result.getString(2));
      declarationsReport.setAmountInsured(result.getBigDecimal(3));
      declarationsReport.setPolicyStreet(result.getString(4));
      declarationsReport.setPolicyArea(result.getString(5));
      declarationsReport.setPolicyCity(result.getString(6));
      declarationsReport.setPolicyPin(result.getString(7));
      policyAmounts.add(declarationsReport);
    }
    return policyAmounts;
  }

  public static BigDecimal getAmountUsed(Connection connection, int inlandPolicyId, Date startDate)
      throws SQLException {
    String sql =
        "SELECT sum(amount) from inland_policy_declarations where inland_policy_id = ? and invoice_date < ? and deleted = 0";
  
    PreparedStatement statement = connection.prepareStatement(sql);
  
    statement.setInt(1, inlandPolicyId);
    statement.setTimestamp(2, beginningOfDayTimestamp(startDate));
  
    ResultSet result = statement.executeQuery();
  
    BigDecimal amount = new BigDecimal(0);
    if (result.next()) {
      amount = result.getBigDecimal(1);
    }
    return amount;
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
}
