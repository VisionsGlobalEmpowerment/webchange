ALTER TABLE ONLY courses
    DROP COLUMN status,
    DROP COLUMN owner_id,
    DROP COLUMN website_user_id;
--;;
ALTER TABLE ONLY users
    DROP COLUMN website_id;
--;;
