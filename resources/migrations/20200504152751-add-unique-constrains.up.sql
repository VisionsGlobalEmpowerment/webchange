ALTER TABLE teachers
ADD CONSTRAINT teacher_unique UNIQUE (user_id, school_id);
--;;
ALTER TABLE students
ADD CONSTRAINT student_unique UNIQUE (user_id, class_id);
--;;
ALTER TABLE course_stats
ADD CONSTRAINT course_stats_unique UNIQUE (user_id, class_id, course_id);
--;;
ALTER TABLE course_progresses
ADD CONSTRAINT course_progresses_unique UNIQUE (user_id, course_id);
--;;
ALTER TABLE ONLY course_events ADD COLUMN guid uuid;
--;;
ALTER TABLE course_events
ADD CONSTRAINT course_events_unique UNIQUE (guid);
--;;
UPDATE course_events SET guid=UUID(data->>'id');
--;;
ALTER TABLE course_events ALTER COLUMN guid SET NOT NULL;
--;;
ALTER TABLE activity_stats
ADD CONSTRAINT activity_stats_unique UNIQUE (user_id, course_id, activity_id);
--;;