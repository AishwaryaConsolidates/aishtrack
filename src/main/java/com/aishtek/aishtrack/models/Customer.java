package com.aishtek.aishtrack.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({@BelongsTo(parent = Person.class, foreignKeyName = "contact_person_id"),
    @BelongsTo(parent = Address.class, foreignKeyName = "address_id")})

@Table("customers")
public class Customer extends Model {

}
