ALTER TABLE activity_stats DROP COLUMN activity_id;
--;;
ALTER TABLE activity_stats
ADD CONSTRAINT activity_stats_unique UNIQUE (user_id, course_id, unique_id);
--;;
