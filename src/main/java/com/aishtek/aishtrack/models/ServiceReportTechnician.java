package com.aishtek.aishtrack.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({@BelongsTo(parent = Technician.class, foreignKeyName = "technician_id"),
    @BelongsTo(parent = ServiceReport.class, foreignKeyName = "service_report_id")})

@Table("service_report_technicians")
public class ServiceReportTechnician extends Model {

}
