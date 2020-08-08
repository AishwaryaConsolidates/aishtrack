ALTER TABLE bank_accounts DROP encrypted_bank_account_number;

ALTER TABLE bank_accounts ADD encrypted_account_number VARCHAR(128);