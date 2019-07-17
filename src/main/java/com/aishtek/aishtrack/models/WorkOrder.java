package com.aishtek.aishtrack.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({@BelongsTo(parent = Customer.class, foreignKeyName = "customer_id")})

@Table("work_orders")
public class WorkOrder extends Model {

    public static final String CREATED_STATUS = "created";
    public static final String ASSIGNED_STATUS = "assigned";
    public static final String IN_PROGRESS_STATUS = "in_progress";
    public static final String COMPLETED_STATUS = "completed";
    public static final String DELETED_STATUS = "deleted";

}
