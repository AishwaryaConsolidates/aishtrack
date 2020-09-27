ALTER TABLE domestic_remittances ADD cheque_number VARCHAR(50);
ALTER TABLE domestic_remittances ADD cheque_date TIMESTAMP DEFAULT NOW();

