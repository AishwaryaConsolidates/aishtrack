package com.aishtek.aishtrack.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import com.aishtek.aishtrack.utils.WorkStatus;

public class ServiceReport extends BaseBean {

  private String code;
  private int customerId;
  private int addressId;
  private int contactPersonId;
  private Date reportDate;
  private String status;
  private Date statusDate;
  private String brand;
  private String model;
  private String serialNumber;
  private int serviceRating;
  private String notes;


  public ServiceReport(Customer customer, String notes) {
    this.customerId = customer.getId();
    this.addressId = customer.getAddressId();
    this.contactPersonId = customer.getContactPersonId();
    this.notes = notes;
    setStatus(WorkStatus.CREATED_STATUS);
  }

  public ServiceReport(int id, String code, int customerId, int addressId, int contactPersonId,
      Date reportDate, String status, Date statusDate, String brand, String model,
      String serialNumber, int serviceRating, String notes, int deleted) {

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
}
