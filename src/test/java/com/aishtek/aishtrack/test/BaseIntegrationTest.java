package com.aishtek.aishtrack.test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.RandomStringUtils;
import com.aishtek.aishtrack.beans.Address;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.beans.DomesticRemittance;
import com.aishtek.aishtrack.beans.Person;
import com.aishtek.aishtrack.beans.ServiceReport;
import com.aishtek.aishtrack.beans.WorkOrder;
import com.aishtek.aishtrack.dao.AddressDAO;
import com.aishtek.aishtrack.dao.BankAccountDAO;
import com.aishtek.aishtrack.dao.CategoryDAO;
import com.aishtek.aishtrack.dao.CustomerDAO;
import com.aishtek.aishtrack.dao.DomesticRemittanceDAO;
import com.aishtek.aishtrack.dao.ExpenseReportDAO;
import com.aishtek.aishtrack.dao.PersonDAO;
import com.aishtek.aishtrack.dao.ServiceReportDAO;
import com.aishtek.aishtrack.dao.SupplierDAO;
import com.aishtek.aishtrack.dao.TechnicianDAO;
import com.aishtek.aishtrack.dao.WorkOrderDAO;

public class BaseIntegrationTest {

  public Connection getConnection() throws SQLException {
    Connection connection =
        DriverManager.getConnection("jdbc:postgresql://localhost/aishtek_test", "postgres",
            "postgres");
    connection.setAutoCommit(false);
    return connection;
  }

  public int createTestPerson(Connection connection) throws SQLException {
    return PersonDAO.create(connection,
        new Person("Asterix", "Gaul", "Troubleshooter", "asterix@aishtek.tst", "9999999999",
            "8888888888", "7777777777"));
  }

  public int createTestAddress(Connection connection) throws SQLException {
    return AddressDAO.create(connection,
        new Address("Food St.", "VV Puram", "Blore", "KTKA", "560004"));
  }

  public int createTestCustomer(Connection connection) throws SQLException {
    int customerId = CustomerDAO.create(connection,
        new Customer(0, "Bajji Corner", "Bajji", createTestAddress(connection), 0, "GSTIN"));
    CustomerDAO.createContactPerson(connection, customerId, new Person("Asterix", "Gaul",
        "Troubleshooter", "asterix@aishtek.tst", "9999999999", "8888888888", "7777777777"));
    return customerId;
  }

  public int createTestWorkOrder(Connection connection, int customerId, int personId)
      throws SQLException {
    if (customerId == 0) {
      customerId = createTestCustomer(connection);
    }
    if (personId == 0) {
      personId = createTestPerson(connection);
    }
    int categoryId = CategoryDAO.createCategory(connection, "canine");
    int equipmentId = CategoryDAO.createEquipment(connection, categoryId, "belt");

    WorkOrder workOrder = new WorkOrder(customerId, personId, "installation",
        "install sonething notes", categoryId, equipmentId, "brand a",
        "model10", "serno1001", "partno101", createTestAddress(connection));

    return WorkOrderDAO.create(connection, workOrder);
  }

  public int createTestServiceReport(Connection connection, int customerId, int personId,
      int addressId) throws SQLException {
    if (customerId == 0) {
      customerId = createTestCustomer(connection);
    }
    if (personId == 0) {
      personId = createTestPerson(connection);
    }
    if (addressId == 0) {
      addressId = createTestAddress(connection);
    }

    ServiceReport serviceReport = new ServiceReport(0, "588bc7ff-8aeb-4cf8-94b1-04cb294a840c",
        customerId, addressId, personId, new Date(), "created", new Date(), "brand101", "model111",
        "serialno101", 5, "some notes", 0, "installation");
    serviceReport.setCategoryId(CategoryDAO.createCategory(connection, "Asinine"));
    serviceReport.setEquipmentId(
        CategoryDAO.createEquipment(connection, serviceReport.getCategoryId(), "Shoe"));
    return ServiceReportDAO.create(connection,
        createTestWorkOrder(connection, customerId, personId),
        serviceReport);
  }

  public int createTestTechnician(Connection connection) throws SQLException {
    return TechnicianDAO.create(connection, createTestPerson(connection));
  }

  public int createBankAccount(Connection connection) throws SQLException, Exception {
    String bank = randomString(5);
    return BankAccountDAO.create(connection, bank + " Bank", bank + " Branch", bank + "SWIFT",
        randomString(10), bank + "IBAN", bank + "Other Details",
        createTestAddress(connection));
  }

  public int[] createSupplierAndBankAccount(Connection connection, String type) throws Exception {
    int bankAccountId = createBankAccount(connection);
    int supplierId = createSupplier(connection, type);
    SupplierDAO.createSupplierBankAccount(connection, supplierId, bankAccountId);
    int[] ids = {supplierId, bankAccountId};
    return ids;
  }

  public int createSupplier(Connection connection, String type) throws SQLException {
    return SupplierDAO.create(connection, "Supplier " + randomNumber(5), type);
  }

  public int createDomesticRemittance(Connection connection) throws Exception {
    int fromBankAccountId = createBankAccount(connection);
    int supplierBankAccountId = createBankAccount(connection);
    int fromBankAddressId = createTestAddress(connection);
    int supplierId = createSupplier(connection, "domestic");

    BigDecimal amount = new BigDecimal(20);
    String purpose = "Test";
    Date signatureDate = new Date();
    DomesticRemittance domesticRemittance = new DomesticRemittance(0, fromBankAccountId,
        fromBankAddressId, supplierId, supplierBankAccountId, amount, purpose, signatureDate, 0);

    return DomesticRemittanceDAO.create(connection, domesticRemittance);
  }

  public int getExpenseReport(Connection connection) throws SQLException {
    int customerId = createTestCustomer(connection);
    int technicianId = createTestTechnician(connection);
    int serviceReportId = createTestServiceReport(connection, customerId, 0, 0);
    return ExpenseReportDAO.create(connection, serviceReportId, customerId, technicianId,
        new BigDecimal(1000), new BigDecimal(200), "Bangalore", new Date());
  }

  public static Timestamp currentTimestamp() {
    return new Timestamp(new Date().getTime());
  }

  public String randomString(int length) {
    boolean useLetters = true;
    boolean useNumbers = true;
    return RandomStringUtils.random(length, useLetters, useNumbers);
  }

  public int randomNumber(int length) {
    boolean useLetters = false;
    boolean useNumbers = true;
    return Integer.parseInt(RandomStringUtils.random(length, useLetters, useNumbers));
  }

  public Date yesterday() {
    Calendar yesterday = Calendar.getInstance();
    yesterday.add(Calendar.DATE, -1);
    return yesterday.getTime();
  }

  public Date tomorrow() {
    Calendar tomorrow = Calendar.getInstance();
    tomorrow.add(Calendar.DATE, 1);
    return tomorrow.getTime();
  }
}
