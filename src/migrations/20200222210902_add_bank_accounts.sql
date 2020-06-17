CREATE TABLE bank_accounts (
  id                  SERIAL PRIMARY KEY,
  name                VARCHAR(100),
  branch              VARCHAR(100),
  swift_code          VARCHAR(50),
  account_number      VARCHAR(50),
  iban                VARCHAR(50),
  other_details       VARCHAR(100),
  created_at          TIMESTAMP DEFAULT NOW(),
  deleted             INT DEFAULT 0
);

CREATE TABLE bank_account_addresses (
  id                 SERIAL PRIMARY KEY,
  bank_account_id    INT NOT NULL,
  address_id         INT NOT NULL,
  deleted       INT DEFAULT 0,
  FOREIGN KEY (address_id) REFERENCES addresses(id),
  FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id)
);
CREATE INDEX ON bank_account_addresses(bank_account_id, address_id);

CREATE TABLE supplier_bank_accounts (
  id                 SERIAL PRIMARY KEY,
  supplier_id         INT NOT NULL,
  bank_account_id    INT NOT NULL,
  deleted       INT DEFAULT 0,
  FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
  FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id)
);
CREATE INDEX ON supplier_bank_accounts(supplier_id, bank_account_id);
