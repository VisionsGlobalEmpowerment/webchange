ALTER TABLE ONLY schools
    ADD COLUMN archived boolean NOT NULL default false;
--;;

ALTER TABLE ONLY classes
    ADD COLUMN archived boolean NOT NULL default false;
--;;

ALTER TABLE ONLY teachers
    ADD COLUMN archived boolean NOT NULL default false;
--;;

ALTER TABLE ONLY students
    ADD COLUMN archived boolean NOT NULL default false;
--;;

