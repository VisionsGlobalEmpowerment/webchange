CREATE TABLE courses
(id SERIAL,
 name VARCHAR(30) NOT NULL);
--;;
ALTER TABLE ONLY courses
    ADD CONSTRAINT courses_pkey PRIMARY KEY (id);
--;;

CREATE TABLE course_versions
(id SERIAL,
 course_id INTEGER references courses(id),
 data JSON NOT NULL,
 owner_id INTEGER NOT NULL,
 created_at TIMESTAMP with time zone NOT NULL);
--;;
ALTER TABLE ONLY course_versions
    ADD CONSTRAINT course_versions_pkey PRIMARY KEY (id);
--;;

CREATE TABLE scenes
(id SERIAL,
 course_id INTEGER references courses(id),
 name VARCHAR(30) NOT NULL);
--;;
ALTER TABLE ONLY scenes
    ADD CONSTRAINT scenes_pkey PRIMARY KEY (id);
--;;

CREATE TABLE scene_versions
(id SERIAL,
 scene_id INTEGER references scenes(id),
 data JSON NOT NULL,
 owner_id INTEGER NOT NULL,
 created_at TIMESTAMP with time zone NOT NULL);
--;;
ALTER TABLE ONLY scene_versions
    ADD CONSTRAINT scene_versions_pkey PRIMARY KEY (id);
