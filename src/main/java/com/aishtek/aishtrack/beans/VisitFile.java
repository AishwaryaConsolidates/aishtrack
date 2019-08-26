package com.aishtek.aishtrack.beans;

public class VisitFile extends BaseBean {

  private int id;
  private int visitId;
  private int fileId;
  private String note;

  public VisitFile(int visitId, int fileId) {
    this.visitId = visitId;
    this.fileId = fileId;
  }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }

  public int getVisitId() {
    return visitId;
  }

  public void setVisitId(int visitId) {
    this.visitId = visitId;
  }

  public int getFileId() {
    return fileId;
  }

  public void setFileId(int fileId) {
    this.fileId = fileId;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
