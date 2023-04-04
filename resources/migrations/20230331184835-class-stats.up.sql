CREATE TABLE class_stats
(class_id int NOT NULL,
 data json NOT NULL);
--;;
ALTER TABLE ONLY public.class_stats
    ADD CONSTRAINT class_stats_class_fkey FOREIGN KEY (class_id) REFERENCES public.classes(id);
--;;


