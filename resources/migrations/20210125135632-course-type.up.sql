ALTER TABLE ONLY courses ADD COLUMN type VARCHAR(30);
--;;
UPDATE courses SET type = 'course';
