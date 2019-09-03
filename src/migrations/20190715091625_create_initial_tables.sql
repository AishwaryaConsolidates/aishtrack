create extension "uuid-ossp";

CREATE OR REPLACE FUNCTION updated_at_trigger() RETURNS TRIGGER AS $$ BEGIN NEW.updated_at = NOW(); RETURN NEW; END; $$ LANGUAGE plpgsql;

CREATE TABLE categories (
  id            SERIAL PRIMARY KEY,
  name          VARCHAR(30)
);

CREATE TABLE equipments (
  id            SERIAL PRIMARY KEY,
  category_id   INT NOT NULL,
  name          VARCHAR(30)
);

CREATE TABLE persons (
  id            SERIAL PRIMARY KEY,
  first_name    VARCHAR(50),
  last_name     VARCHAR(50),
  designation   VARCHAR(50),
  phone         VARCHAR(20),
  email         VARCHAR(50),
  created_at    TIMESTAMP DEFAULT NOW(),
  deleted       INT DEFAULT 0
);

CREATE TABLE addresses (
  id            SERIAL PRIMARY KEY,
  street        VARCHAR(100),
  area          VARCHAR(50),
  city          VARCHAR(25),
  state         VARCHAR(25),
  pincode       VARCHAR(6),
  created_at    TIMESTAMP DEFAULT NOW(),
  deleted       INT DEFAULT 0
);

CREATE TABLE customers (
  id                     SERIAL PRIMARY KEY,
  name                   VARCHAR(50),
  nick_name              VARCHAR(50) UNIQUE,
  address_id             INT NOT NULL,
  contact_person_id      INT NOT NULL,
  created_at             TIMESTAMP DEFAULT NOW(),
  deleted                INT DEFAULT 0,
  FOREIGN KEY (contact_person_id) REFERENCES persons(id),
  FOREIGN KEY (address_id) REFERENCES addresses(id)
);

CREATE TABLE customer_persons (
  id             SERIAL PRIMARY KEY,
  customer_id    INT NOT NULL,
  person_id      INT NOT NULL,
  FOREIGN KEY (person_id) REFERENCES persons(id),
  FOREIGN KEY (customer_id) REFERENCES customers(id)
);
CREATE INDEX ON customer_persons(customer_id, person_id);

CREATE TABLE technicians (
  id             SERIAL PRIMARY KEY,
  person_id      INT NOT NULL,
  created_at     TIMESTAMP DEFAULT NOW(),
  updated_at     TIMESTAMP,
  deleted        INT DEFAULT 0,
  FOREIGN KEY (person_id) REFERENCES persons(id)
);
CREATE INDEX ON technicians(person_id);
CREATE TRIGGER set_tech_updated_at
BEFORE UPDATE ON technicians
FOR EACH ROW
EXECUTE PROCEDURE updated_at_trigger();

CREATE TABLE work_orders (
  id             SERIAL PRIMARY KEY,
  customer_id    INT NOT NULL,
  contact_person_id      INT NOT NULL,
  type           VARCHAR(20),
  category_id    INT,
  equipment_id   INT,
  brand          VARCHAR(30),
  model          VARCHAR(30),
  serial_number  VARCHAR(30),
  part_number    VARCHAR(30),
  status         VARCHAR(15),
  status_date    TIMESTAMP,
  notes          TEXT,
  created_at     TIMESTAMP DEFAULT NOW(),
  updated_at     TIMESTAMP,
  deleted        INT DEFAULT 0,
  FOREIGN KEY (customer_id) REFERENCES customers(id),
  FOREIGN KEY (category_id) REFERENCES categories(id),
  FOREIGN KEY (equipment_id) REFERENCES equipments(id)
);
CREATE INDEX ON work_orders(customer_id);

CREATE TRIGGER set_wo_updated_at
BEFORE UPDATE ON work_orders
FOR EACH ROW
EXECUTE PROCEDURE updated_at_trigger();

CREATE TABLE service_reports (
  id                     SERIAL PRIMARY KEY,
  code                   UUID DEFAULT uuid_generate_v4(),
  customer_id            INT NOT NULL,
  address_id             INT NOT NULL,
  contact_person_id      INT NOT NULL,
  report_date            TIMESTAMP,
  status                 VARCHAR(15),
  status_date            TIMESTAMP,
  category_id            INT,
  equipment_id           INT,
  brand                  VARCHAR(30),
  model                  VARCHAR(30),
  serial_number          VARCHAR(30),
  part_number            VARCHAR(30),
  signed_by              VARCHAR(30),
  service_rating         INT,
  notes                  TEXT,
  created_at             TIMESTAMP DEFAULT NOW(),
  updated_at             TIMESTAMP,
  deleted                INT DEFAULT 0,
  FOREIGN KEY (customer_id) REFERENCES customers(id),
  FOREIGN KEY (contact_person_id) REFERENCES persons(id),
  FOREIGN KEY (address_id) REFERENCES addresses(id),
  FOREIGN KEY (category_id) REFERENCES categories(id),
  FOREIGN KEY (equipment_id) REFERENCES equipments(id)
);
CREATE INDEX ON service_reports(customer_id);

CREATE TRIGGER set_sr_updated_at
BEFORE UPDATE ON service_reports
FOR EACH ROW
EXECUTE PROCEDURE updated_at_trigger();

CREATE TABLE work_order_service_reports (
  id                   SERIAL PRIMARY KEY,
  work_order_id        INT NOT NULL,
  service_report_id    INT NOT NULL,
  FOREIGN KEY (service_report_id) REFERENCES service_reports(id),
  FOREIGN KEY (work_order_id) REFERENCES work_orders(id)
);
CREATE INDEX ON work_order_service_reports (work_order_id, service_report_id);

CREATE TABLE service_report_technicians (
  id                   SERIAL PRIMARY KEY,
  service_report_id    INT NOT NULL,
  technician_id        INT NOT NULL,
  FOREIGN KEY (service_report_id) REFERENCES service_reports(id),
  FOREIGN KEY (technician_id) REFERENCES technicians(id)
);
CREATE INDEX ON service_report_technicians (service_report_id, technician_id);

CREATE TABLE visits (
  id                       SERIAL PRIMARY KEY,
  service_report_id    INT NOT NULL,
  visit_date               TIMESTAMP,
  complaint                TEXT,
  findings                 TEXT,
  work_done                TEXT,
  customer_remarks         TEXT,
  created_at     TIMESTAMP DEFAULT NOW(),
  updated_at     TIMESTAMP,
  deleted        INT DEFAULT 0,
  FOREIGN KEY (service_report_id) REFERENCES service_reports(id)
);

CREATE TRIGGER set_v_updated_at
BEFORE UPDATE ON visits
FOR EACH ROW
EXECUTE PROCEDURE updated_at_trigger();

CREATE TABLE visit_technicians (
  id                   SERIAL PRIMARY KEY,
  visit_id             INT NOT NULL,
  technician_id        INT NOT NULL,
  FOREIGN KEY (visit_id) REFERENCES visits(id),
  FOREIGN KEY (technician_id) REFERENCES technicians(id)
);
CREATE INDEX ON visit_technicians (visit_id, technician_id);

CREATE TABLE visit_replaced_spare_parts (
  id                   SERIAL PRIMARY KEY,
  visit_id             INT NOT NULL,
  part_number          VARCHAR(30),
  description          TEXT,
  quantity             INT DEFAULT 0,
  FOREIGN KEY (visit_id) REFERENCES visits(id)
);
CREATE INDEX ON visit_replaced_spare_parts (visit_id);

CREATE TABLE visit_recommended_spare_parts (
  id                   SERIAL PRIMARY KEY,
  visit_id             INT NOT NULL,
  part_number          VARCHAR(30),
  description          TEXT,
  quantity             INT DEFAULT 0,
  FOREIGN KEY (visit_id) REFERENCES visits(id)
);
CREATE INDEX ON visit_recommended_spare_parts (visit_id);

CREATE TABLE files (
  id                       SERIAL PRIMARY KEY,
  name                     VARCHAR(50),
  location                 VARCHAR(100),
  upload_date              TIMESTAMP DEFAULT NOW()
);

CREATE TABLE visit_files (
  id                  SERIAL PRIMARY KEY,
  visit_id            INT NOT NULL,
  file_id             INT NOT NULL,
  note                TEXT,
  FOREIGN KEY (visit_id) REFERENCES visits(id),
  FOREIGN KEY (file_id) REFERENCES files(id)
);
CREATE INDEX ON visit_files (visit_id, file_id);

CREATE TABLE installation_reports (
  id                     SERIAL PRIMARY KEY,
  code                   UUID DEFAULT uuid_generate_v4(),
  customer_id            INT NOT NULL,
  address_id             INT NOT NULL,
  contact_person_id      INT NOT NULL,
  installation_date      TIMESTAMP,
  status                 VARCHAR(15),
  status_date            TIMESTAMP,
  brand                  VARCHAR(30),
  model                  VARCHAR(30),
  serial_number          VARCHAR(30),
  equipment_damaged      BOOLEAN,
  rating_card_file_id    INT,
  installation_details   JSON,
  service_rating         INT,
  notes                  TEXT,
  created_at             TIMESTAMP DEFAULT NOW(),
  updated_at             TIMESTAMP,
  deleted                INT DEFAULT 0,
  FOREIGN KEY (customer_id) REFERENCES customers(id),
  FOREIGN KEY (contact_person_id) REFERENCES persons(id),
  FOREIGN KEY (address_id) REFERENCES addresses(id),
  FOREIGN KEY (rating_card_file_id) REFERENCES files(id)
);
CREATE INDEX ON installation_reports(customer_id);

CREATE TRIGGER set_ir_updated_at
BEFORE UPDATE ON installation_reports
FOR EACH ROW
EXECUTE PROCEDURE updated_at_trigger();

CREATE TABLE work_order_installation_reports (
  id                        SERIAL PRIMARY KEY,
  work_order_id             INT NOT NULL,
  installation_report_id    INT NOT NULL,
  FOREIGN KEY (installation_report_id) REFERENCES installation_reports(id),
  FOREIGN KEY (work_order_id) REFERENCES work_orders(id)
);
CREATE INDEX ON work_order_installation_reports(work_order_id, installation_report_id);

CREATE TABLE installation_report_technicians (
  id                        SERIAL PRIMARY KEY,
  installation_report_id    INT NOT NULL,
  technician_id             INT NOT NULL,
  FOREIGN KEY (installation_report_id) REFERENCES installation_reports(id),
  FOREIGN KEY (technician_id) REFERENCES technicians(id)
);
CREATE INDEX ON installation_report_technicians (installation_report_id, technician_id);

CREATE TABLE installation_report_visits (
  id                            SERIAL PRIMARY KEY,
  visit_id                      INT NOT NULL,
  installation_report_id        INT NOT NULL,
  FOREIGN KEY (installation_report_id) REFERENCES installation_reports(id),
  FOREIGN KEY (visit_id) REFERENCES visits(id)
);
CREATE INDEX ON installation_report_visits (installation_report_id, visit_id);
