package com.aishtek.aishtrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.beans.Supplier;
import com.aishtek.aishtrack.utils.Util;

public class SupplierDAO extends BaseDAO {

  public static ArrayList<NameId> searchFor(Connection connection, String type, String name)
      throws SQLException {
    String sql = "SELECT s.id, s.name from suppliers s where s.deleted = 0 ";

    if (!Util.isNullOrEmpty(name)) {
      sql += " and s.name ilike ? ";
    }
    if (type != null) {
      sql += " and s.type = ? ";
    }

    PreparedStatement statement = connection.prepareStatement(sql);

    int index = 1;
    if (!Util.isNullOrEmpty(name)) {
      name = "%" + name + "%";
      statement.setString(index, name);
      index++;
    }

    if (!Util.isNullOrEmpty(type)) {
      statement.setString(index, type);
      index++;
    }

    ResultSet result = statement.executeQuery();

    ArrayList<NameId> suppliers = new ArrayList<NameId>();
    while (result.next()) {
      suppliers.add(new NameId(result.getInt(1), result.getString(2)));
    }
    return suppliers;
  }

  public static Supplier findById(Connection connection, int supplierId)
      throws SQLException {
    String sql = "SELECT s.id, s.name, s.type from suppliers s where s.id = ? ";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, supplierId);

    ResultSet result = statement.executeQuery();

    if (result.next()) {
      Supplier supplier = new Supplier(result.getInt(1), result.getString(2), result.getString(3));
      return supplier;
    } else {
      throw new SQLException("No supplier for Id");
    }
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

  public static ArrayList<HashMap<String, String>> getAddresses(Connection connection,
      int supplierId) throws SQLException {
    String sql =
        "SELECT a.id, a.street, a.area, a.city, a.state, a.pincode, sa.deleted from supplier_addresses sa, addresses a where sa.address_id = a.id and sa.supplier_id = ? ";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setInt(1, supplierId);
    ResultSet result = statement.executeQuery();

    ArrayList<HashMap<String, String>> addresses = new ArrayList<HashMap<String, String>>();
    while (result.next()) {
      HashMap<String, String> hashMap = new HashMap<String, String>();
      hashMap.put("id", "" + result.getInt(1));
      hashMap.put("street", result.getString(2));
      hashMap.put("area", result.getString(3));
      hashMap.put("city", result.getString(4));
      hashMap.put("state", result.getString(5));
      hashMap.put("pincode", result.getString(6));
      hashMap.put("deleted", "" + result.getInt(7));

      addresses.add(hashMap);
    }
    return addresses;
  }

  public static int create(Connection connection, String name, String type) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into suppliers (name, type) values(?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
    preparedStatement.setString(1, name);
    preparedStatement.setString(2, type);
    preparedStatement.executeUpdate();

    ResultSet result = preparedStatement.getGeneratedKeys();
    if (result.next()) {
      return result.getInt(1);
    } else {
      throw new SQLException("Supplier ID not generted");
    }
  }

  public static void update(Connection connection, String name, String type, int supplierId)
      throws SQLException {
    PreparedStatement preparedStatement =
        connection.prepareStatement("update suppliers set name = ?, type = ? where id = ?");
    preparedStatement.setString(1, name);
    preparedStatement.setString(2, type);
    preparedStatement.setInt(3, supplierId);
    preparedStatement.executeUpdate();

  }

  public static int save(Connection connection, int supplierId, String name, String type)
      throws SQLException {
    if (supplierId > 0) {
      update(connection, name, type, supplierId);
    } else {
      supplierId = create(connection, name, type);
    }
    return supplierId;
  }

  public static void createSupplierAddress(Connection connection, int supplierId, int addressId)
      throws SQLException {
    PreparedStatement preparedStatement = connection
        .prepareStatement("insert into supplier_addresses (supplier_id, address_id) values(?, ?)");
    preparedStatement.setInt(1, supplierId);
    preparedStatement.setInt(2, addressId);
    preparedStatement.executeUpdate();
  }

  public static void createSupplierBankAccount(Connection connection, int supplierId,
      int bankAccountId) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(
        "insert into supplier_bank_accounts (supplier_id, bank_account_id) values(?, ?)");
    preparedStatement.setInt(1, supplierId);
    preparedStatement.setInt(2, bankAccountId);
    preparedStatement.executeUpdate();
  }
}
