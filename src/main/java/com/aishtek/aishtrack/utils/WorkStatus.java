package com.aishtek.aishtrack.utils;

public class WorkStatus {
    public static final String CREATED_STATUS = "created";
    public static final String ASSIGNED_STATUS = "assigned";
    public static final String IN_PROGRESS_STATUS = "in_progress";
    public static final String COMPLETED_STATUS = "completed";
    public static final String DELETED_STATUS = "deleted";

  public static String [] openStatuses() {
    String[] statuses = {ASSIGNED_STATUS, IN_PROGRESS_STATUS, CREATED_STATUS};
      return statuses;
    }
}
