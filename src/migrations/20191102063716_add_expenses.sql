CREATE TABLE expense_reports (
  id                   SERIAL PRIMARY KEY,
  service_report_id    INT,
  customer_id          INT,
  technician_id        INT NOT NULL,
  created_at           TIMESTAMP DEFAULT NOW(),
  updated_at           TIMESTAMP,
  deleted              INT DEFAULT 0,
  FOREIGN KEY (service_report_id) REFERENCES service_reports(id),
  FOREIGN KEY (technician_id) REFERENCES technicians(id),
  FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TRIGGER set_er_updated_at
BEFORE UPDATE ON expense_reports
FOR EACH ROW
EXECUTE PROCEDURE updated_at_trigger();

CREATE INDEX ON expense_reports (service_report_id);
CREATE INDEX ON expense_reports (customer_id);
CREATE INDEX ON expense_reports (technician_id);

CREATE TABLE expenses (
  id                   SERIAL PRIMARY KEY,
  expense_report_id    INT NOT NULL,
  expense_date         TIMESTAMP,
  expense_type         VARCHAR(20) NOT NULL,
  notes                VARCHAR(100),
  amount               money NOT NULL,
  created_at           TIMESTAMP DEFAULT NOW(),
  updated_at           TIMESTAMP,
  deleted              INT DEFAULT 0,
  FOREIGN KEY (expense_report_id) REFERENCES expense_reports(id)
);

CREATE TRIGGER set_ex_updated_at
BEFORE UPDATE ON expenses
FOR EACH ROW
EXECUTE PROCEDURE updated_at_trigger();

