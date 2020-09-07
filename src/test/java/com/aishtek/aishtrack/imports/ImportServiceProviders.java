package com.aishtek.aishtrack.imports;

import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Ignore;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Address;
import com.aishtek.aishtrack.dao.AddressDAO;
import com.aishtek.aishtrack.dao.BankAccountDAO;
import com.aishtek.aishtrack.dao.SupplierDAO;
import com.amazonaws.util.StringUtils;

public class ImportServiceProviders {

  private Connection getConnection() throws SQLException {
    Connection connection = DriverManager.getConnection(
        "jdbc:postgresql://78.47.31.201:54320/aishwary_staging", "aishwary_stagelogin", "x0n9jNsP");
    connection.setAutoCommit(false);
    return connection;
  }

  @Test
  @Ignore
  public void importServiceProvidersFromSpreadsheet() throws Exception {
    try (Connection connection = getConnection()) {
      String urlStr =
          "https://aishtek.s3.ap-south-1.amazonaws.com/Revised+Service+Providers+bank+details.xlsx";
      URL url = new URL(urlStr);
      URLConnection uc = url.openConnection();

      XSSFWorkbook workbook = new XSSFWorkbook(uc.getInputStream());
      XSSFSheet sheet = workbook.getSheetAt(0);

      // Iterate through each rows one by one
      Iterator<Row> rowIterator = sheet.iterator();
      DataFormatter formatter = new DataFormatter();
      int i = 0;
      while (rowIterator.hasNext()) {
        try {
          Row row = rowIterator.next();
          i = 1 + 1;
          System.out.println("starting: " + i);
          // For each row, iterate through all the columns
          Iterator<Cell> cellIterator = row.cellIterator();

          Cell cell1 = cellIterator.next();
          String supplier = formatter.formatCellValue(cell1);

          Cell cell2 = cellIterator.next();
          String bankName = formatter.formatCellValue(cell2);

          Cell cell3 = cellIterator.next();
          String bankBranch = formatter.formatCellValue(cell3);

          Cell cell4 = cellIterator.next();
          String accountNumber = formatter.formatCellValue(cell4);

          Cell cell5 = cellIterator.next();
          String ifsc = formatter.formatCellValue(cell5);

          if (!StringUtils.isNullOrEmpty(supplier)) {
            int supplierId = SupplierDAO.create(connection, supplier, "domestic");
            Address address = new Address("", "", "", "", "");
            int addressId = AddressDAO.create(connection, address);

            int bankAccountId = BankAccountDAO.create(connection, bankName, bankBranch, ifsc,
                accountNumber, "", "", addressId);

            SupplierDAO.createSupplierBankAccount(connection, supplierId, bankAccountId);
            System.out.println("finished: " + i);
          }
        } catch (Exception e) {
          System.out.println(e);
        }
      }
      workbook.close();

      connection.commit();
      assert (true);
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
