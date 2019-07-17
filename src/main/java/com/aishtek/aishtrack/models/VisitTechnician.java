package com.aishtek.aishtrack.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({@BelongsTo(parent = Technician.class, foreignKeyName = "technician_id"),
    @BelongsTo(parent = Visit.class, foreignKeyName = "visit_id")})

@Table("visit_technicians")
public class VisitTechnician extends Model {

}
