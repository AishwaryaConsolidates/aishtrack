package com.aishtek.aishtrack.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({@BelongsTo(parent = Technician.class, foreignKeyName = "technician_id"),
    @BelongsTo(parent = InstallationReport.class, foreignKeyName = "installation_report_id")})

@Table("installation_report_technicians")
public class InstallationReportTechnician extends Model {

}
