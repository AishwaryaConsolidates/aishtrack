ALTER TABLE marine_policies DROP exchange_rate;
ALTER TABLE marine_policies DROP duty_amount;

ALTER TABLE marine_policy_declarations ADD exchange_rate NUMERIC(10,2);
ALTER TABLE marine_policy_declarations ADD duty_amount NUMERIC(10,2);
