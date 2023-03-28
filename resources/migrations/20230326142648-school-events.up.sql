ALTER TABLE ONLY course_events
ADD COLUMN scene_id int,
ADD COLUMN school_id int;
--;;
CREATE TABLE school_stats
(school_id int NOT NULL,
 data json NOT NULL);
--;;
ALTER TABLE ONLY public.school_stats
    ADD CONSTRAINT school_stats_school_fkey FOREIGN KEY (school_id) REFERENCES public.schools(id);
--;;


