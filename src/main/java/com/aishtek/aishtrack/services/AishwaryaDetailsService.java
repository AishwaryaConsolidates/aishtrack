package com.aishtek.aishtrack.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import com.aishtek.aishtrack.beans.Address;
import com.aishtek.aishtrack.beans.NameId;
import com.aishtek.aishtrack.dao.AddressDAO;
import com.aishtek.aishtrack.dao.BankAccountDAO;
import com.aishtek.aishtrack.dao.InlandPolicyDeclarationDAO;
import com.aishtek.aishtrack.dao.MarinePolicyDeclarationDAO;

public class AishwaryaDetailsService {

  public static Address getCurrentAishwaryaAddress(Connection connection) throws SQLException {
    return AddressDAO.getAishwaryaAddress(connection);
  }

  public static ArrayList<NameId> getCurrentAishwaryaBankAccounts(Connection connection)
      throws SQLException {
    return BankAccountDAO.getCurrentAishwaryaBankAccounts(connection);
  }

  public static ArrayList<NameId> getCurrentMarinePolicies(Connection connection)
      throws SQLException {
    return MarinePolicyDeclarationDAO.getCurrentMarinePolicies(connection);
  }

  public static ArrayList<NameId> getCurrentInlandPolicies(Connection connection)
      throws SQLException {
    return InlandPolicyDeclarationDAO.getCurrentInlandPolicies(connection);
  }
}