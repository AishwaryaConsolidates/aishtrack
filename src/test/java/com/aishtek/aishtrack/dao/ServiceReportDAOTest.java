package com.aishtek.aishtrack.dao;

import static org.junit.Assert.assertEquals;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.junit.Test;
import com.aishtek.aishtrack.beans.Customer;
import com.aishtek.aishtrack.beans.ServiceReport;
import com.aishtek.aishtrack.test.BaseIntegrationTest;
import com.aishtek.aishtrack.utils.WorkStatus;

public class ServiceReportDAOTest extends BaseIntegrationTest {

  @Test
  public void createServiceReportSavesTheReport() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      int addressId = createTestAddress(connection);
      int personId = createTestPerson(connection);
      int categoryId = CategoryDAO.createCategory(connection, "categ");
      int equipmentId = CategoryDAO.createEquipment(connection, categoryId, "equip");

      ServiceReport serviceReport = new ServiceReport(0, "",
          customerId, addressId, personId, new Date(), "created", new Date(), "brand101",
          "model111", "serialno101", 5, "some notes", 0, "installation");

      serviceReport.setCategoryId(categoryId);
      serviceReport.setEquipmentId(equipmentId);

      int workOrderId = createTestWorkOrder(connection, customerId, 0);

      int serviceReportId = ServiceReportDAO.create(connection, workOrderId, serviceReport);

      ServiceReport createdServiceReport = ServiceReportDAO.findById(connection, serviceReportId);
      assertEquals(createdServiceReport.getCustomerId(), customerId);
      assertEquals(createdServiceReport.getAddressId(), addressId);
      assertEquals(createdServiceReport.getContactPersonId(), personId);
      assertEquals(createdServiceReport.getStatus(), WorkStatus.CREATED_STATUS);
      assertEquals(createdServiceReport.getNotes(), "some notes");
      assertEquals(createdServiceReport.getBrand(), "brand101");
      assertEquals(createdServiceReport.getModel(), "model111");
      assertEquals(createdServiceReport.getSerialNumber(), "serialno101");
      assertEquals(createdServiceReport.getType(), "installation");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }

  }

  @Test
  public void updateTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int serviceReportId = createTestServiceReport(connection, 0, 0, 0);

      int personId = createTestPerson(connection);
      int categoryId = CategoryDAO.createCategory(connection, "categ");
      int equipmentId = CategoryDAO.createEquipment(connection, categoryId, "equip");
      String brand = "brand101";
      String model = "model111";
      String serialNumber = "serialno101";
      String partNumber = "part101";
      String notes = "some notes";
      
      ServiceReportDAO.update(connection, serviceReportId, personId, categoryId, equipmentId, brand, model, serialNumber, partNumber, notes);
      ServiceReport updatedServiceReport = ServiceReportDAO.findById(connection, serviceReportId);

      assertEquals(updatedServiceReport.getContactPersonId(), personId);
      assertEquals(updatedServiceReport.getNotes(), "some notes");
      assertEquals(updatedServiceReport.getBrand(), "brand101");
      assertEquals(updatedServiceReport.getModel(), "model111");
      assertEquals(updatedServiceReport.getSerialNumber(), "serialno101");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void updateEquipmentTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int serviceReportId = createTestServiceReport(connection, 0, 0, 0);

      int categoryId = CategoryDAO.createCategory(connection, "categ");
      int equipmentId = CategoryDAO.createEquipment(connection, categoryId, "equip");
      String brand = "brand101";
      String model = "model111";
      String serialNumber = "serialno101";
      String partNumber = "part101";

      ServiceReportDAO.updateEquipment(connection, serviceReportId, categoryId, equipmentId, brand,
          model, serialNumber, partNumber);;
      ServiceReport updatedServiceReport = ServiceReportDAO.findById(connection, serviceReportId);

      assertEquals(updatedServiceReport.getBrand(), "brand101");
      assertEquals(updatedServiceReport.getModel(), "model111");
      assertEquals(updatedServiceReport.getSerialNumber(), "serialno101");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void updateAdditionalEmailTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int serviceReportId = createTestServiceReport(connection, 0, 0, 0);

      String additionalEmail = "otheremail@email.tst";
      ServiceReportDAO.updateAdditionalEmail(connection, serviceReportId, additionalEmail);

      ArrayList<String> emails = ServiceReportDAO.getEmailForFeedback(connection, serviceReportId);
      assertEquals(emails.get(1), additionalEmail);

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void updateFeedbackTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int serviceReportId = createTestServiceReport(connection, 0, 0, 0);
      String serviceReportCode = ServiceReportDAO.getCodeForId(connection, serviceReportId);

      ServiceReportDAO.updateFeedback(connection, serviceReportCode, 5, "Customer is King",
          "Good Job");

      HashMap<String, String> serviceReport =
          ServiceReportDAO.findByCode(connection, serviceReportCode);

      assertEquals(serviceReport.get("customerRemarks"), "Good Job");
      assertEquals(serviceReport.get("signedBy"), "Customer is King");
      assertEquals(serviceReport.get("serviceRating"), "5");
      assertEquals(serviceReport.get("status"), WorkStatus.COMPLETED_STATUS);
      assertEquals(serviceReport.get("statusDate"), formatter.format(today()));

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void assignTechniciansToServiceReportTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int serviceReportId = createTestServiceReport(connection, 0, 0, 0);
      int technicianId = createTestTechnician(connection);
      ArrayList<Integer> technicianIds = new ArrayList<Integer>();
      technicianIds.add(technicianId);
      technicianIds.add(technicianId);

      ServiceReportDAO.assignTechniciansToServiceReport(connection, serviceReportId, technicianIds);


      String technician = ServiceReportDAO.getTechnicians(connection, serviceReportId);

      assertEquals(technician, "Asterix Gaul, Asterix Gaul");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void addInstallationDetailTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int serviceReportId = createTestServiceReport(connection, 0, 0, 0);
      String serviceReportCode = ServiceReportDAO.getCodeForId(connection, serviceReportId);
      
      String [] keys = {"key 1", "key 2", "key 3"};
      String [] values = {"value 1", "value 2", "value 3"};
      
      HashMap<String, HashMap<String, String>> installationsDetails = ServiceReportDAO
          .addInstallationDetail(connection, serviceReportId, "some detail", keys, values);

      HashMap<String, String> installationDetail = installationsDetails.get("some detail");
      
      assertEquals(installationDetail.get("key 1"), "value 1");
      assertEquals(installationDetail.get("key 2"), "value 2");
      assertEquals(installationDetail.get("key 3"), "value 3");


      String[] keys2 = {"key 7", "key 8", "key 9"};
      String[] values2 = {"value 7", "value 8", "value 9"};

      installationsDetails = ServiceReportDAO.addInstallationDetail(connection, serviceReportId,
          "some other detail", keys2, values2);

      HashMap<String, String> serviceReport =
          ServiceReportDAO.findByCode(connection, serviceReportCode);


      assertEquals(serviceReport.get("installationDetails"),
          "{\"some detail\":{\"key 3\":\"value 3\",\"key 2\":\"value 2\",\"key 1\":\"value 1\"},\"some other detail\":{\"key 8\":\"value 8\",\"key 7\":\"value 7\",\"key 9\":\"value 9\"}}");

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void getCustomerFortTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int customerId = createTestCustomer(connection);
      Customer customer = CustomerDAO.findById(connection, customerId);

      int serviceReportId = createTestServiceReport(connection, customerId, 0, 0);

      Customer srCustomer = ServiceReportDAO.getCustomerFor(connection, serviceReportId);

      assertEquals(customer.getName(), srCustomer.getName());
      assertEquals(customer.getNickName(), srCustomer.getNickName());
      assertEquals(customer.getAddressId(), srCustomer.getAddressId());
      assertEquals(customer.getGstIN(), srCustomer.getGstIN());

      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }


  @Test
  public void deleteForWorkOrderTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int serviceReportId = createTestServiceReport(connection, 0, 0, 0);
      String serviceReportCode = ServiceReportDAO.getCodeForId(connection, serviceReportId);
      HashMap<String, String> serviceReport =
          ServiceReportDAO.findByCode(connection, serviceReportCode);

      ServiceReportDAO.deleteForWorkOrder(connection,
          Integer.parseInt(serviceReport.get("workOrderId")));

      ServiceReport serviceReportBean = ServiceReportDAO.findById(connection, serviceReportId);

      assertEquals(serviceReportBean.getStatus(), "deleted");
      assertEquals(serviceReportBean.getDeleted(), 1);
      assertEquals(formatter.format(serviceReportBean.getStatusDate()), formatter.format(today()));
      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }

  @Test
  public void updateStatusTest() throws SQLException {
    try (Connection connection = getConnection()) {
      int serviceReportId = createTestServiceReport(connection, 0, 0, 0);
      ServiceReportDAO.updateStatus(connection, serviceReportId, "Some Status");

      ServiceReport serviceReportBean = ServiceReportDAO.findById(connection, serviceReportId);

      assertEquals(serviceReportBean.getStatus(), "Some Status");
      assertEquals(formatter.format(serviceReportBean.getStatusDate()), formatter.format(today()));
      connection.rollback();
    } catch (SQLException e) {
      System.out.println(e);
      assert (false);
    }
  }
}
