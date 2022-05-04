CREATE TABLE school_courses
(school_id int NOT NULL,
 course_id int NOT NULL);
--;;
ALTER TABLE ONLY public.school_courses
    ADD CONSTRAINT school_courses_school_fkey FOREIGN KEY (school_id) REFERENCES public.schools(id);
--;;
ALTER TABLE ONLY public.school_courses
    ADD CONSTRAINT school_courses_course_fkey FOREIGN KEY (course_id) REFERENCES public.courses(id);

