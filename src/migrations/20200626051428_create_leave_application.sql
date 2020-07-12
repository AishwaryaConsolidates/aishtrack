CREATE TABLE leave_applications (
  id                   SERIAL PRIMARY KEY,
  from_date            TIMESTAMP,
  to_date              TIMESTAMP,
  technician_id        INT NOT NULL,
  reason               TEXT,
  status               VARCHAR(25),
  signature            VARCHAR(50),
  signature_date       TIMESTAMP,
  created_at           TIMESTAMP DEFAULT NOW(),
  updated_at           TIMESTAMP,
  deleted              INT DEFAULT 0,
  FOREIGN KEY (technician_id) REFERENCES technicians(id)
);

CREATE TRIGGER set_lap_updated_at
BEFORE UPDATE ON leave_applications
FOR EACH ROW
EXECUTE PROCEDURE updated_at_trigger();