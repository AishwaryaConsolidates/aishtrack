package com.aishtek.aishtrack.beans;

import java.util.Date;

public class LeaveApplication extends BaseBean {

  int technicianId;
  Date fromDate;
  Date toDate;
  String reason;
  String signature;
  Date signatureDate;
  String status;

  // for view
  String signatureDateString;
  String fromDateString;
  String toDateString;
  String technicianName;

  public LeaveApplication(int id, int technicianId, Date fromDate, Date toDate, String reason,
      String status, String signature, Date signatureDate, int deleted) {
    super();
    this.id = id;
    this.technicianId = technicianId;
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.reason = reason;
    this.status = status;
    this.signature = signature;
    this.signatureDate = signatureDate;
    this.deleted = deleted;
  }

  public int getTechnicianId() {
    return technicianId;
  }

  public void setTechnicianId(int technicianId) {
    this.technicianId = technicianId;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public void setFromDate(Date fromDate) {
    this.fromDate = fromDate;
  }

  public Date getToDate() {
    return toDate;
  }

  public void setToDate(Date toDate) {
    this.toDate = toDate;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public Date getSignatureDate() {
    return signatureDate;
  }

  public void setSignatureDate(Date signatureDate) {
    this.signatureDate = signatureDate;
  }

  public String getSignatureDateString() {
    return signatureDateString;
  }

  public void setSignatureDateString(String signatureDateString) {
    this.signatureDateString = signatureDateString;
  }

  public String getFromDateString() {
    return fromDateString;
  }

  public void setFromDateString(String fromDateString) {
    this.fromDateString = fromDateString;
  }

  public String getToDateString() {
    return toDateString;
  }

  public void setToDateString(String toDateString) {
    this.toDateString = toDateString;
  }

  public String getTechnicianName() {
    return technicianName;
  }

  public void setTechnicianName(String technicianName) {
    this.technicianName = technicianName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}
