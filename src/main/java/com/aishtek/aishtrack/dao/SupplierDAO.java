package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.aishtek.aishtrack.beans.NameId;

public class SupplierDAO extends BaseDAO {

  public static ArrayList<NameId> searchFor(Connection connection)
      throws SQLException {
    String sql = "SELECT s.id, s.name from suppliers s where s.deleted = 0 ";

    PreparedStatement statement = connection.prepareStatement(sql);
    ResultSet result = statement.executeQuery();

    ArrayList<NameId> suppliers = new ArrayList<NameId>();
    while (result.next()) {
      suppliers.add(new NameId(result.getInt(1), result.getString(2)));
    }
    return suppliers;
  }

  public static ArrayList<NameId> getAddressesFor(Connection connection,
      int supplierId) throws SQLException {
    String sql =
        "SELECT a.id, a.area, a.city from supplier_addresses sa, addresses a where sa.address_id = a.id and sa.supplier_id = ? ";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, supplierId);
    ResultSet result = statement.executeQuery();

    ArrayList<NameId> addresses = new ArrayList<NameId>();
    while (result.next()) {
        addresses.add(
            new NameId(result.getInt(1), (result.getString(2) + " (" + result.getString(3) + ")")));
    }
    return addresses;
  }
}
