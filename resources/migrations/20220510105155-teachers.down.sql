DROP TABLE class_teachers;
--;;
ALTER TABLE ONLY teachers
    DROP COLUMN type,
    DROP COLUMN status;
--;;
