CREATE TABLE domestic_remittances (
  id                                   SERIAL PRIMARY KEY,
  from_bank_account_id                 INT NOT NULL,
  from_bank_address_id                 INT NOT NULL,
  supplier_id                          INT NOT NULL,
  supplier_bank_account_id             INT NOT NULL,
  amount                               NUMERIC(10,2),
  purpose                              VARCHAR(100),
  signature_date                       TIMESTAMP DEFAULT NOW(),
  created_at                           TIMESTAMP DEFAULT NOW(),
  updated_at                           TIMESTAMP,
  deleted                              INT DEFAULT 0,
  FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
  FOREIGN KEY (supplier_bank_account_id) REFERENCES bank_accounts(id),
  FOREIGN KEY (from_bank_account_id) REFERENCES bank_accounts(id),
  FOREIGN KEY (from_bank_address_id) REFERENCES addresses(id)
);

CREATE INDEX ON domestic_remittances(supplier_id);
CREATE INDEX ON domestic_remittances(signature_date);

CREATE TRIGGER set_drs_updated_at
BEFORE UPDATE ON domestic_remittances
FOR EACH ROW
EXECUTE PROCEDURE updated_at_trigger();