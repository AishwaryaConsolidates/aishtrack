CREATE TABLE marine_policies (
  id                 SERIAL PRIMARY KEY,
  address_id         INT NOT NULL,
  contact_person_id  INT NOT NULL,
  provider           VARCHAR(200),
  amount             NUMERIC(10,2),
  start_date         TIMESTAMP,
  end_date           TIMESTAMP,
  FOREIGN KEY (address_id) REFERENCES addresses(id),
  FOREIGN KEY (contact_person_id) REFERENCES persons(id)
);

CREATE TABLE inland_policies (
  id                 SERIAL PRIMARY KEY,
  address_id         INT NOT NULL,
  contact_person_id  INT NOT NULL,
  provider           VARCHAR(200),
  amount             NUMERIC(10,2),
  start_date         TIMESTAMP,
  end_date           TIMESTAMP,
  FOREIGN KEY (address_id) REFERENCES addresses(id),
  FOREIGN KEY (contact_person_id) REFERENCES persons(id)
);
 
CREATE TABLE marine_policy_declarations (
  id                   SERIAL PRIMARY KEY,
  marine_policy_id     INT NOT NULL,
  supplier_id          INT NOT NULL,
  supplier_address_id  INT NOT NULL,
  invoice_number       VARCHAR(100),
  invoice_date         TIMESTAMP,
  description          VARCHAR(200),
  amount               NUMERIC(10,2),
  currency             VARCHAR(50),
  quantity             INT DEFAULT 0,
  from_location        VARCHAR(100),
  to_location          VARCHAR(100),
  receipt_number       VARCHAR(100),
  receipt_date         TIMESTAMP,
  FOREIGN KEY (marine_policy_id) REFERENCES marine_policies(id),
  FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
  FOREIGN KEY (supplier_address_id) REFERENCES addresses(id)
);

CREATE TABLE inland_policy_declarations (
  id                   SERIAL PRIMARY KEY,
  inland_policy_id     INT NOT NULL,
  customer_id          INT NOT NULL,
  invoice_number       VARCHAR(100),
  invoice_date         TIMESTAMP,
  description          VARCHAR(200),
  amount               NUMERIC(10,2),
  quantity             INT DEFAULT 0,
  from_location        VARCHAR(100),
  to_location          VARCHAR(100),
  receipt_number       VARCHAR(100),
  receipt_date         TIMESTAMP,
  FOREIGN KEY (inland_policy_id) REFERENCES inland_policies(id),
  FOREIGN KEY (customer_id) REFERENCES customers(id)
);