ALTER TABLE ONLY scenes
    ADD COLUMN created_at timestamp with time zone,
    ADD COLUMN updated_at timestamp with time zone,
    ADD COLUMN type text,
    ADD COLUMN metadata json;
--;;

UPDATE scenes s set type = 'book' where exists (select type from courses c where c.id = s.course_id and c.type = 'book');
--;;

UPDATE scenes s set type = 'activity' where exists (select type from courses c where c.id = s.course_id and c.type = 'course');
--;;

UPDATE scenes s SET created_at = (SELECT created_at from scene_versions sv where sv.scene_id = s.id ORDER by created_at ASC LIMIT 1);
--;;

UPDATE scenes s SET updated_at = (SELECT created_at from scene_versions sv where sv.scene_id = s.id ORDER by created_at DESC LIMIT 1);
--;;

UPDATE scenes s set image_src = (select image_src from courses c where c.id = s.course_id) where type = 'book';
--;;
