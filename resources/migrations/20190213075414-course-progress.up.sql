CREATE TABLE course_progresses
(id SERIAL,
 user_id INTEGER references users(id),
 course_id INTEGER references courses(id),
 data JSON NOT NULL);
--;;
ALTER TABLE ONLY course_progresses
    ADD CONSTRAINT course_progresses_pkey PRIMARY KEY (id);
--;;

CREATE TABLE course_actions
(id SERIAL,
 user_id INTEGER references users(id),
 course_id INTEGER references courses(id),
 created_at timestamp with time zone NOT NULL,
 type VARCHAR(30) NOT NULL,
 data JSON NOT NULL);
--;;
ALTER TABLE ONLY course_actions
    ADD CONSTRAINT course_actions_pkey PRIMARY KEY (id);
--;;

CREATE TABLE course_stats
(id SERIAL,
 user_id INTEGER references users(id),
 class_id INTEGER references classes(id),
 course_id INTEGER references courses(id),
 data JSON NOT NULL);
--;;
ALTER TABLE ONLY course_stats
    ADD CONSTRAINT course_stats_pkey PRIMARY KEY (id);
--;;

CREATE TABLE activity_stats
(id SERIAL,
 user_id INTEGER references users(id),
 course_id INTEGER references courses(id),
 level_number INTEGER NOT NULL,
 activity_number INTEGER NOT NULL,
 data JSON NOT NULL);
--;;
ALTER TABLE ONLY activity_stats
    ADD CONSTRAINT activity_stats_pkey PRIMARY KEY (id);