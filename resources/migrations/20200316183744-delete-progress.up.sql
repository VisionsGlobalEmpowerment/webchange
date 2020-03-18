DELETE FROM course_progresses;
DELETE FROM course_actions;
DELETE FROM course_stats;

DROP TABLE activity_stats;
--;;
CREATE TABLE activity_stats
(id SERIAL,
 user_id INTEGER references users(id),
 course_id INTEGER references courses(id),
 activity_id VARCHAR(32) NOT NULL,
 data JSON NOT NULL);

--;;
ALTER TABLE ONLY activity_stats
    ADD CONSTRAINT activity_stats_pkey PRIMARY KEY (id);