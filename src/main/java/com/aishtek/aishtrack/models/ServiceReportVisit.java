package com.aishtek.aishtrack.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({@BelongsTo(parent = Visit.class, foreignKeyName = "visit_id"),
    @BelongsTo(parent = ServiceReport.class, foreignKeyName = "service_report_id")})

@Table("service_report_visits")
public class ServiceReportVisit extends Model {

}
