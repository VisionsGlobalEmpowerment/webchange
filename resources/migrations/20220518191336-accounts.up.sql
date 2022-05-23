ALTER TABLE ONLY users
    ADD COLUMN type text;
--;;

UPDATE users SET type = 'live'
WHERE website_id is NOT null;
--;;
