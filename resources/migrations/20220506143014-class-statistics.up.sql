ALTER TABLE ONLY classes
    ADD COLUMN created_at date default CURRENT_DATE,
    ADD COLUMN stats json;
--;;
