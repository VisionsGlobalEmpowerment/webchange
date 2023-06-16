ALTER TABLE ONLY schools
    ADD COLUMN type text;
--;;
UPDATE schools SET type = 'global';
