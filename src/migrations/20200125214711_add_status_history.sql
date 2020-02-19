CREATE TABLE service_report_status_histories (
  id                   SERIAL PRIMARY KEY,
  service_report_id    INT,
  status               VARCHAR(15),
  status_date          TIMESTAMP DEFAULT NOW(),
  FOREIGN KEY (service_report_id) REFERENCES service_reports(id)
);

CREATE TABLE work_order_status_histories (
  id                   SERIAL PRIMARY KEY,
  work_order_id        INT,
  status               VARCHAR(15),
  status_date          TIMESTAMP DEFAULT NOW(),
  FOREIGN KEY (work_order_id) REFERENCES work_orders(id)
);