package com.aishtek.aishtrack.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import com.aishtek.aishtrack.utils.WorkStatus;

public class ServiceReport extends BaseBean {

  private String type;
  private String code;
  private int customerId;
  private int addressId;
  private int contactPersonId;
  private int categoryId;
  private int equipmentId;
  private Date reportDate;
  private String status;
  private Date statusDate;
  private String brand;
  private String model;
  private String serialNumber;
  private String partNumber;
  private int serviceRating;
  private String notes;

  // installation fields
  private boolean equipmentDamaged;
  private int ratingCardFileId;
  private String installationDetails;

  // search fields
  private String customerName;

  public ServiceReport(Customer customer, int contactPersonId, int categoryId, int equipmentId,
      String notes, String brand, String model, String serialNumber, String partNumber,
      Date reportDate, String type) {
    this.customerId = customer.getId();
    this.addressId = customer.getAddressId();
    this.contactPersonId = contactPersonId;
    this.categoryId = categoryId;
    this.equipmentId = equipmentId;
    this.notes = notes;
    this.brand = brand;
    this.model = model;
    this.reportDate = reportDate;
    this.serialNumber = serialNumber;
    this.partNumber = partNumber;
    this.type = type;
    setStatus(WorkStatus.CREATED_STATUS);
  }

  public ServiceReport(int id, String code, int customerId, int addressId, int contactPersonId,
      Date reportDate, String status, Date statusDate, String brand, String model,
      String serialNumber, int serviceRating, String notes, int deleted, String type) {

    this.id = id;
    this.code = code;
    this.customerId = customerId;
    this.addressId = addressId;
    this.contactPersonId = contactPersonId;
    this.reportDate = reportDate;
    this.status = status;
    this.statusDate = statusDate;
    this.brand = brand;
    this.model = model;
    this.serialNumber = serialNumber;
    this.serviceRating = serviceRating;
    this.notes = notes;
    this.deleted = deleted;
    this.type = type;
  }

  public ServiceReport(int id, String code, String status, Date statusDate) {

    this.id = id;
    this.code = code;
    this.customerId = customerId;
    this.addressId = addressId;
    this.contactPersonId = contactPersonId;
    this.reportDate = reportDate;
    this.status = status;
    this.statusDate = statusDate;
    this.brand = brand;
    this.model = model;
    this.serialNumber = serialNumber;
    this.serviceRating = serviceRating;
    this.notes = notes;
    this.deleted = deleted;
  }

  public void markAsAssigned() throws SQLException {
    status = WorkStatus.ASSIGNED_STATUS;
    try (Connection connection = getConnection()) {
      PreparedStatement preparedStatement = connection
          .prepareStatement("update work_orders set status =?, status_date = ? where id = ?");
      preparedStatement.setString(1, status);
      preparedStatement.setTimestamp(2, currentTimestamp());
      preparedStatement.setInt(3, id);
      preparedStatement.executeUpdate();
    } catch (SQLException sqle) {
      throw sqle;
    }
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
    this.statusDate = new Date();
  }

  public Date getStatusDate() {
    return statusDate;
  }

  public void setStatusDate(Date statusDate) {
    this.statusDate = statusDate;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public int getAddressId() {
    return addressId;
  }

  public void setAddressId(int addressId) {
    this.addressId = addressId;
  }

  public int getContactPersonId() {
    return contactPersonId;
  }

  public void setContactPersonId(int contactPersonId) {
    this.contactPersonId = contactPersonId;
  }

  public Date getReportDate() {
    return reportDate;
  }

  public void setReportDate(Date reportDate) {
    this.reportDate = reportDate;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public int getServiceRating() {
    return serviceRating;
  }

  public void setServiceRating(int serviceRating) {
    this.serviceRating = serviceRating;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }

  public int getEquipmentId() {
    return equipmentId;
  }

  public void setEquipmentId(int equipmentId) {
    this.equipmentId = equipmentId;
  }

  public String getPartNumber() {
    return partNumber;
  }

  public void setPartNumber(String partNumber) {
    this.partNumber = partNumber;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isEquipmentDamaged() {
    return equipmentDamaged;
  }

  public void setEquipmentDamaged(boolean equipmentDamaged) {
    this.equipmentDamaged = equipmentDamaged;
  }

  public int getRatingCardFileId() {
    return ratingCardFileId;
  }

  public void setRatingCardFileId(int ratingCardFileId) {
    this.ratingCardFileId = ratingCardFileId;
  }

  public String getInstallationDetails() {
    return installationDetails;
  }

  public void setInstallationDetails(String installationDetails) {
    this.installationDetails = installationDetails;
  }
}
