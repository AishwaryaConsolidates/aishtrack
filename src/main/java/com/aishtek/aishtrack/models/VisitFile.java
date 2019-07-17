package com.aishtek.aishtrack.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({@BelongsTo(parent = File.class, foreignKeyName = "file_id"),
    @BelongsTo(parent = Visit.class, foreignKeyName = "visit_id")})

@Table("visit_files")
public class VisitFile extends Model {

}
