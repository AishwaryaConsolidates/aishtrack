CREATE TABLE aishwarya_addresses (
  id                 SERIAL PRIMARY KEY,
  address_id         INT NOT NULL,
  created_at          TIMESTAMP DEFAULT NOW(),
  start_date          TIMESTAMP,
  end_date          TIMESTAMP,
  FOREIGN KEY (address_id) REFERENCES addresses(id)
);

CREATE TABLE aishwarya_bank_accounts (
  id                 SERIAL PRIMARY KEY,
  bank_account_id    INT NOT NULL,
  created_at          TIMESTAMP DEFAULT NOW(),
  start_date          TIMESTAMP,
  end_date          TIMESTAMP,
  FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id)
);

