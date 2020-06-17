CREATE TABLE outward_remittances (
  id                                   SERIAL PRIMARY KEY,
  from_bank_account_id                 INT NOT NULL,
  from_bank_address_id                 INT NOT NULL,
  from_address_id                      INT NOT NULL,
  supplier_id                          INT NOT NULL,
  supplier_address_id                  INT NOT NULL,
  supplier_bank_account_id             INT NOT NULL,
  supplier_bank_aaddress_id            INT NOT NULL,
  amount                               NUMERIC(10,2),
  goods                                VARCHAR(100),
  goods_classification_no              VARCHAR(100),
  country_of_origin                    VARCHAR(100),
  currency                             VARCHAR(100),
  purpose                              VARCHAR(100),
  other_info                           VARCHAR(1000),
  signature_place                      VARCHAR(100),
  signature_date                       TIMESTAMP DEFAULT NOW(),
  created_at                           TIMESTAMP DEFAULT NOW(),
  updated_at                           TIMESTAMP,
  deleted                              INT DEFAULT 0,
  FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
  FOREIGN KEY (supplier_bank_account_id) REFERENCES bank_accounts(id),
  FOREIGN KEY (supplier_bank_aaddress_id) REFERENCES addresses(id),
  FOREIGN KEY (supplier_address_id) REFERENCES addresses(id),
  FOREIGN KEY (from_bank_account_id) REFERENCES bank_accounts(id),
  FOREIGN KEY (from_bank_address_id) REFERENCES addresses(id),
  FOREIGN KEY (from_address_id) REFERENCES addresses(id)
);

CREATE INDEX ON outward_remittances(supplier_id);
CREATE INDEX ON outward_remittances(signature_date);

CREATE TRIGGER set_ors_updated_at
BEFORE UPDATE ON outward_remittances
FOR EACH ROW
EXECUTE PROCEDURE updated_at_trigger();