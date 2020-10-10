ALTER TABLE aishwarya_bank_accounts ADD logo_url VARCHAR(255);

update aishwarya_bank_accounts set logo_url = 'https://aishtrack.s3.ap-south-1.amazonaws.com/artifacts/sbi_logo.jpg'
where id = 3;

update aishwarya_bank_accounts set logo_url = 'https://aishtrack.s3.ap-south-1.amazonaws.com/artifacts/union_bank_of_india_logo.jpg'
where id = 1;

update aishwarya_bank_accounts set logo_url = 'https://aishtrack.s3.ap-south-1.amazonaws.com/artifacts/union_bank_of_india_logo.jpg'
where id = 2;