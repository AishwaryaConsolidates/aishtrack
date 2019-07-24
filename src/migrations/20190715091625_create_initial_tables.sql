create extension "uuid-ossp";

CREATE TABLE persons (
  id            SERIAL PRIMARY KEY,
  first_name    VARCHAR(50),
  last_name     VARCHAR(50),
  designation   VARCHAR(50),
  phone         VARCHAR(20),
  email         VARCHAR(50),
  created_at    TIMESTAMP,
  deleted       INT DEFAULT 0
);

CREATE TABLE addresses (
  id            SERIAL PRIMARY KEY,
  street        VARCHAR(100),
  area          VARCHAR(50),
  city          VARCHAR(25),
  state         VARCHAR(25),
  pincode       VARCHAR(6),
  created_at    TIMESTAMP,
  deleted       INT DEFAULT 0
);

CREATE TABLE customers (
  id                     SERIAL PRIMARY KEY,
  name                   VARCHAR(50),
  nick_name              VARCHAR(50) UNIQUE,
  address_id             INT NOT NULL,
  contact_person_id      INT NOT NULL,
  created_at             TIMESTAMP,
  updated_at             TIMESTAMP,
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
  FOREIGN KEY (person_id) REFERENCES persons(id)
);
CREATE INDEX ON technicians(person_id);

CREATE TABLE work_orders (
  id             SERIAL PRIMARY KEY,
  customer_id    INT NOT NULL,
  type           VARCHAR(20),
  status         VARCHAR(15),
  status_date    TIMESTAMP,
  notes          TEXT,
  created_at     TIMESTAMP,
  updated_at     TIMESTAMP,
  deleted        INT DEFAULT 0,
  FOREIGN KEY (customer_id) REFERENCES customers(id)
);
CREATE INDEX ON work_orders(customer_id);

CREATE TABLE service_reports (
  id                     SERIAL PRIMARY KEY,
  code                   UUID DEFAULT uuid_generate_v4(),
  customer_id            INT NOT NULL,
  address_id             INT NOT NULL,
  contact_person_id      INT NOT NULL,
  report_date            TIMESTAMP,
  status                 VARCHAR(15),
  status_date            TIMESTAMP,
  brand                  VARCHAR(30),
  model                  VARCHAR(30),
  serial_number          VARCHAR(30),
  service_rating         INT,
  notes                  TEXT,
  created_at             TIMESTAMP,
  updated_at             TIMESTAMP,
  deleted                INT DEFAULT 0,
  FOREIGN KEY (customer_id) REFERENCES customers(id),
  FOREIGN KEY (contact_person_id) REFERENCES persons(id),
  FOREIGN KEY (address_id) REFERENCES addresses(id)
);
CREATE INDEX ON service_reports(customer_id);

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
  visit_date               TIMESTAMP,
  complaint                TEXT,
  findings                 TEXT,
  work_done                TEXT,
  customer_remarks         TEXT,
  deleted                  INT DEFAULT 0 
);

CREATE TABLE service_report_visits (
  id                   SERIAL PRIMARY KEY,
  visit_id             INT NOT NULL,
  service_report_id    INT NOT NULL,
  FOREIGN KEY (service_report_id) REFERENCES service_reports(id),
  FOREIGN KEY (visit_id) REFERENCES visits(id)
);
CREATE INDEX ON service_report_visits (service_report_id, visit_id);

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
  name                     VARCHAR(30),
  location                 VARCHAR(100),
  uploaded_by_person_id    INT,
  upload_date              TIMESTAMP
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
  created_at             TIMESTAMP,
  updated_at             TIMESTAMP,
  deleted                INT DEFAULT 0,
  FOREIGN KEY (customer_id) REFERENCES customers(id),
  FOREIGN KEY (contact_person_id) REFERENCES persons(id),
  FOREIGN KEY (address_id) REFERENCES addresses(id),
  FOREIGN KEY (rating_card_file_id) REFERENCES files(id)
);
CREATE INDEX ON installation_reports(customer_id);

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