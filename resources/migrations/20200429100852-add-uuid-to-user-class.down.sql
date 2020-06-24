ALTER TABLE users DROP CONSTRAINT user_unique;
--;;
ALTER TABLE classes DROP CONSTRAINT class_unique;
--;;
ALTER TABLE ONLY users
    DROP COLUMN guid;
--;;
ALTER TABLE ONLY classes
    DROP COLUMN guid;
--;;