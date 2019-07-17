package com.aishtek.aishtrack.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({@BelongsTo(parent = Person.class, foreignKeyName = "contact_person_id"),
    @BelongsTo(parent = Customer.class, foreignKeyName = "customer_id")})

@Table("customer_persons")
public class CustomerPerson extends Model {

}
