package com.aishtek.aishtrack.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({@BelongsTo(parent = Customer.class, foreignKeyName = "customer_id"),
    @BelongsTo(parent = Person.class, foreignKeyName = "contact_person_id"),
    @BelongsTo(parent = Address.class, foreignKeyName = "address_id"),
    @BelongsTo(parent = File.class, foreignKeyName = "rating_card_file_id")})

@Table("installation_reports")
public class InstallationReport extends Model {

}
