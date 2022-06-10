ALTER TABLE ONLY scenes
    ADD COLUMN type text,
    ADD COLUMN metadata json;
--;;

UPDATE scenes s set type = 'book' where exists (select type from courses c where c.id = s.course_id and c.type = 'book');
--;;

UPDATE scenes s set type = 'activity' where exists (select type from courses c where c.id = s.course_id and c.type = 'course');
--;;
