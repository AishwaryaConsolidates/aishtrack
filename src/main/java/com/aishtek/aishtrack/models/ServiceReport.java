package com.aishtek.aishtrack.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({@BelongsTo(parent = Customer.class, foreignKeyName = "customer_id"),
    @BelongsTo(parent = Person.class, foreignKeyName = "contact_person_id"),
    @BelongsTo(parent = Address.class, foreignKeyName = "address_id")})

@Table("service_reports")
public class ServiceReport extends Model {

}
