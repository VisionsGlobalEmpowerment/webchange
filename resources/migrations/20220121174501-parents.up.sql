CREATE TABLE children
(child_id int NOT NULL,
 parent_id int NOT NULL,
 data json NOT NULL);
--;;
ALTER TABLE ONLY public.children
    ADD CONSTRAINT children_child_fkey FOREIGN KEY (child_id) REFERENCES public.users(id);
--;;
ALTER TABLE ONLY public.children
    ADD CONSTRAINT children_parent_fkey FOREIGN KEY (parent_id) REFERENCES public.users(id);

