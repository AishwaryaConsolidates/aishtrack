ALTER TABLE expense_reports ADD advance_amount NUMERIC(10,2);
ALTER TABLE expense_reports ADD settled INT DEFAULT 0;