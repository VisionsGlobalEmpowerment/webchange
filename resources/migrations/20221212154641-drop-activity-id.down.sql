ALTER TABLE activity_stats ADD COLUMN activity_id varchar(200);
--;;
ALTER TABLE activity_stats
ADD CONSTRAINT activity_stats_unique UNIQUE (user_id, course_id, activity_id);
--;;
