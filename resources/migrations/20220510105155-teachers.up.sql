CREATE TABLE class_teachers
(class_id int NOT NULL,
 teacher_id int NOT NULL);
--;;
ALTER TABLE ONLY public.class_teachers
    ADD CONSTRAINT class_teachers_class_fkey FOREIGN KEY (class_id) REFERENCES public.classes(id);
--;;
ALTER TABLE ONLY public.class_teachers
    ADD CONSTRAINT class_teachers_teacher_fkey FOREIGN KEY (teacher_id) REFERENCES public.teachers(id);
--;;
ALTER TABLE ONLY teachers
    ADD COLUMN type text,
    ADD COLUMN status text;
--;;


