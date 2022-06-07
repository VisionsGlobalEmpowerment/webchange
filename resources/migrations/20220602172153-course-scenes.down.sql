DROP TABLE course_scenes;
--;;

ALTER TABLE ONLY scenes
    DROP COLUMN slug,
    DROP COLUMN lang,
    DROP COLUMN image_src,
    DROP COLUMN status,
    DROP COLUMN owner_id;
--;;

