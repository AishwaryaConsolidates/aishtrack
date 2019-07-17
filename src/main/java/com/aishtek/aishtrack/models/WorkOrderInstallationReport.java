package com.aishtek.aishtrack.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({@BelongsTo(parent = WorkOrder.class, foreignKeyName = "work_order_id"),
    @BelongsTo(parent = InstallationReport.class, foreignKeyName = "installation_report_id")})

@Table("work_order_installation_reports")
public class WorkOrderInstallationReport extends Model {

}
