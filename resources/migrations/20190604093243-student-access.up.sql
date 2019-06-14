ALTER TABLE users ALTER COLUMN email DROP NOT NULL;
--;;
ALTER TABLE users ALTER COLUMN password DROP NOT NULL;

--;;
CREATE TABLE schools
(id SERIAL,
 name VARCHAR(30) NOT NULL);
--;;
ALTER TABLE ONLY schools
    ADD CONSTRAINT schools_pkey PRIMARY KEY (id);
--;;
INSERT INTO schools (id, name) VALUES (1, 'default');
--;;

CREATE TABLE teachers
(id SERIAL,
 user_id INTEGER references users(id),
 school_id INTEGER references schools(id));
--;;
ALTER TABLE ONLY teachers
    ADD CONSTRAINT teachers_pkey PRIMARY KEY (id);
--;;
INSERT INTO teachers (user_id, school_id)
    SELECT 1, 1
    WHERE EXISTS (SELECT id FROM users WHERE id = 1);

--;;
ALTER TABLE ONLY classes
    ADD COLUMN school_id INTEGER references schools(id);
--;;
UPDATE classes SET school_id = 1;
--;;
ALTER TABLE ONLY students
    ADD COLUMN school_id INTEGER references schools(id);
--;;
ALTER TABLE ONLY students
    ADD COLUMN access_code VARCHAR(30);
--;;
ALTER TABLE ONLY students
    ADD COLUMN gender INTEGER;
--;;
ALTER TABLE ONLY students
    ADD COLUMN date_of_birth DATE;
--;;
UPDATE students SET school_id = 1;
--;;

ALTER TABLE students ADD CONSTRAINT school_access_code_key UNIQUE (school_id, access_code);
