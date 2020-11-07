ALTER TABLE ONLY courses
    ADD COLUMN status VARCHAR(30),
    ADD COLUMN owner_id INTEGER,
    ADD COLUMN website_user_id INTEGER;
--;;
UPDATE courses SET status = 'draft';
--;;
ALTER TABLE ONLY users
    ADD COLUMN website_id INTEGER;
--;;
