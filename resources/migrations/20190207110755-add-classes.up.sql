CREATE TABLE classes
(id SERIAL,
 name VARCHAR(30) NOT NULL);
--;;
ALTER TABLE ONLY classes
    ADD CONSTRAINT classes_pkey PRIMARY KEY (id);
--;;

CREATE TABLE students
(id SERIAL,
 user_id INTEGER references users(id),
 class_id INTEGER references classes(id));
--;;
ALTER TABLE ONLY students
    ADD CONSTRAINT students_pkey PRIMARY KEY (id);

--;;
CREATE TABLE course_progresses
(id SERIAL,
 user_id INTEGER references users(id),
 course_id INTEGER references courses(id),
 data JSON NOT NULL);
