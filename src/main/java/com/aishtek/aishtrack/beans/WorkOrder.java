package com.aishtek.aishtrack.beans;

import java.util.Date;
import com.aishtek.aishtrack.utils.WorkStatus;

public class WorkOrder extends BaseBean {

  private int customerId;
  private int addressId;
  private int contactPersonId;
  private String type;
  private String status;
  private Date statusDate;
  private String notes;
  private int categoryId;
  private int equipmentId;
  private String brand;
  private String model;
  private String serialNumber;
  private String partNumber;

  public WorkOrder(int customerId, int contactPersonId, String type, String notes, int categoryId,
      int equipmentId,
      String brand, String model, String serialNumber, String partNumber, int addressId) {
    this.customerId = customerId;
    this.contactPersonId = contactPersonId;
    this.type = type;
    this.categoryId = categoryId;
    this.equipmentId = equipmentId;
    this.brand = brand;
    this.model = model;
    this.serialNumber = serialNumber;
    this.partNumber = partNumber;
    this.notes = notes;
    this.addressId = addressId;
    setStatus(WorkStatus.CREATED_STATUS);
  }

  public WorkOrder(int id, int customerId, int addressId, int contactPersonId, String type,
      String status,
      Date statusDate,
      String notes, int deleted, int categoryId, int equipmentId, String brand, String model,
      String serialNumber, String partNumber) {
    this.id = id;
    this.customerId = customerId;
    this.addressId = addressId;
    this.contactPersonId = contactPersonId;
    this.type = type;
    this.notes = notes;
    this.status = status;
    this.statusDate = statusDate;
    this.deleted = deleted;
    this.categoryId = categoryId;
    this.equipmentId = equipmentId;
    this.brand = brand;
    this.model = model;
    this.serialNumber = serialNumber;
    this.partNumber = partNumber;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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

  public String getPartNumber() {
    return partNumber;
  }

  public void setPartNumber(String partNumber) {
    this.partNumber = partNumber;
  }

  public int getContactPersonId() {
    return contactPersonId;
  }

  public void setContactPersonId(int contactPersonId) {
    this.contactPersonId = contactPersonId;
  }

  public int getAddressId() {
    return addressId;
  }

  public void setAddressId(int addressId) {
    this.addressId = addressId;
  }
}
