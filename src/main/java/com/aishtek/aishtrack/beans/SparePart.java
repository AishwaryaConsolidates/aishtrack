package com.aishtek.aishtrack.beans;

public class SparePart extends BaseBean {

  private int visitId;
  private String partNumber;
  private String description;
  private int quantity;

  public SparePart(int id, int visitId, String partNumber, String description, int quantity) {
    this.id = id;
    this.visitId = visitId;
    this.partNumber = partNumber;
    this.description = description;
    this.quantity = quantity;
  }

  public SparePart(int visitId, String partNumber, String description, int quantity) {
    this.visitId = visitId;
    this.partNumber = partNumber;
    this.description = description;
    this.quantity = quantity;
  }

  public int getVisitId() {
    return visitId;
  }

  public void setVisitId(int visitId) {
    this.visitId = visitId;
  }

  public String getPartNumber() {
    return partNumber;
  }

  public void setPartNumber(String partNumber) {
    this.partNumber = partNumber;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

}
