package com.aishtek.aishtrack.beans;

import java.util.Date;

public class Visit extends BaseBean {

  private int serviceReportId;
  private Date visitDate;
  private String complaint;
  private String findings;
  private String workDone;
  private String customerRemarks;

  public Visit(int id, int serviceReportId, Date visitDate, String complaint, String findings,
      String workDone, String customerRemarks) {
    this.id = id;
    this.serviceReportId = serviceReportId;
    this.visitDate = visitDate;
    this.complaint = complaint;
    this.findings = findings;
    this.workDone = workDone;
    this.customerRemarks = customerRemarks;
  }

  public int getServiceReportId() {
    return serviceReportId;
  }

  public void setServiceReportId(int serviceReportId) {
    this.serviceReportId = serviceReportId;
  }

  public Date getVisitDate() {
    return visitDate;
  }

  public void setVisitDate(Date visitDate) {
    this.visitDate = visitDate;
  }

  public String getComplaint() {
    return complaint;
  }

  public void setComplaint(String complaint) {
    this.complaint = complaint;
  }

  public String getFindings() {
    return findings;
  }

  public void setFindings(String findings) {
    this.findings = findings;
  }

  public String getWorkDone() {
    return workDone;
  }

  public void setWorkDone(String workDone) {
    this.workDone = workDone;
  }

  public String getCustomerRemarks() {
    return customerRemarks;
  }

  public void setCustomerRemarks(String customerRemarks) {
    this.customerRemarks = customerRemarks;
  }

}
