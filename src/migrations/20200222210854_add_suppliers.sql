CREATE TABLE suppliers (
  id                     SERIAL PRIMARY KEY,
  name                   VARCHAR(50),
  created_at             TIMESTAMP DEFAULT NOW(),
  deleted                INT DEFAULT 0
);

CREATE TABLE supplier_addresses (
  id              SERIAL PRIMARY KEY,
  supplier_id     INT NOT NULL,
  address_id      INT NOT NULL,
  deleted         INT DEFAULT 0,
  FOREIGN KEY (address_id) REFERENCES addresses(id),
  FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);
CREATE INDEX ON supplier_addresses(supplier_id, address_id);
