insert into categories (name) values ('Cooking Equipment');
insert into categories (name) values ('Bakery Equipment');
insert into categories (name) values ('Refrigeration Systems');
insert into categories (name) values ('Blenders and Juicers');

insert into sub_categories (category_id, name) 
select id, 'Bratt Pans' from categories where name = 'Cooking Equipment';

insert into sub_categories (category_id, name) 
select id, 'Boiling Pans' from categories where name = 'Cooking Equipment';

insert into sub_categories (category_id, name) 
select id, 'Mixing Kettles' from categories where name = 'Cooking Equipment';

insert into sub_categories (category_id, name) 
select id, 'Burner Ranges' from categories where name = 'Cooking Equipment';

insert into sub_categories (category_id, name) 
select id, 'Pressure Fryers' from categories where name = 'Cooking Equipment';

insert into persons (first_name, last_name, designation, phone, email) 
values ('Count', 'Dracula', 'Owner', '9880482508', 'madadarsh@hotmail.com');

insert into persons (first_name, last_name, designation, phone, email) 
values ('Mavis', 'Dracula', 'Technician', '9880482508', 'simpleboy007@hotmail.com');

insert into persons (first_name, last_name, designation, phone, email) 
values ('Johnny', 'Human', 'Technician', '9880482508', 'adarshadarsh@live.com');

insert into persons (first_name, last_name, designation, phone, email) 
values ('Soup', 'Chef', 'Cook', '9880482508', 'adarshadarsh@live.com');

insert into addresses (street, area, city, state, pincode) 
values ('Ghoul Street', 'Forrest', 'Transylvania', 'Night', '60611');

insert into addresses (street, area, city, state, pincode) 
values ('South End Road', 'Basavanagudi', 'Bangalore', 'Karnataka', '560004');

insert into customers (name, nick_name, address_id, contact_person_id) 
values ('Hotel Transylvania', 'Transylvania', 1, 1);

insert into customer_persons (customer_id, person_id)
values (1, 1);
insert into customer_persons (customer_id, person_id)
values (1, 2);
insert into customer_persons (customer_id, person_id)
values (1, 3);

insert into customers (name, nick_name, address_id, contact_person_id) 
values ('Military Hotel', 'Military', 2, 1);

insert into customer_persons (customer_id, person_id) values (2, 1);
insert into customer_persons (customer_id, person_id) values (2, 4);