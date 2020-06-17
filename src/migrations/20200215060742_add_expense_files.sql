ALTER TABLE expenses ADD file_id INT;

ALTER TABLE expenses
ADD FOREIGN KEY (file_id) 
REFERENCES files(id)
