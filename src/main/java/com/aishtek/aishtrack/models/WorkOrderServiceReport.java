package com.aishtek.aishtrack.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({@BelongsTo(parent = WorkOrder.class, foreignKeyName = "work_order_id"),
    @BelongsTo(parent = ServiceReport.class, foreignKeyName = "service_report_id")})

@Table("work_order_service_requests")
public class WorkOrderServiceReport extends Model {

}
