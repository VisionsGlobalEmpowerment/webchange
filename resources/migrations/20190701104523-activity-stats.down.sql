DROP TABLE activity_stats;
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