package com.aishtek.aishtrack.function;

import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.aishtek.aishtrack.beans.Address;
import com.aishtek.aishtrack.dao.AddressDAO;
import com.aishtek.aishtrack.dao.BankAccountDAO;
import com.aishtek.aishtrack.dao.SupplierDAO;
import com.aishtek.aishtrack.model.ServerlessInput;
import com.aishtek.aishtrack.model.ServerlessOutput;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class ImportLocalServiceProviders extends BaseFunction
    implements RequestHandler<ServerlessInput, ServerlessOutput> {

  @Override
  public ServerlessOutput handleRequest(ServerlessInput serverlessInput, Context context) {
    ServerlessOutput output;
    try (Connection connection = getConnection()) {
      try {
        String result = "";
        String urlStr =
            "https://aishtek.s3.ap-south-1.amazonaws.com/Revised+Service+Providers+bank+details.xlsx";
        URL url = new URL(urlStr);
        URLConnection uc = url.openConnection();

        XSSFWorkbook workbook = new XSSFWorkbook(uc.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);

        // Iterate through each rows one by one
        Iterator<Row> rowIterator = sheet.iterator();
        DataFormatter formatter = new DataFormatter();
        while (rowIterator.hasNext()) {
          Row row = rowIterator.next();
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

          int supplierId = SupplierDAO.create(connection, supplier, "domestic");
          Address address = new Address("", "", "", "", "");
          int addressId = AddressDAO.create(connection, address);

          int bankAccountId = BankAccountDAO.create(connection, bankName, bankBranch, ifsc,
              accountNumber, "", "", addressId);

          SupplierDAO.createSupplierBankAccount(connection, supplierId, bankAccountId);
        }
        workbook.close();
        output = createSuccessOutput(result);
        connection.commit();
      } catch (Exception e) {
        connection.rollback();
        output = createFailureOutput(e);
      }
    } catch (Exception e) {
      output = createFailureOutput(e);
    }
    return output;
  }
}
