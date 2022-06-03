CREATE TABLE course_scenes (
  course_id int REFERENCES courses (id),
  scene_id int REFERENCES scenes (id),
  CONSTRAINT course_scenes_pkey PRIMARY KEY (course_id, scene_id)
);
--;;

ALTER TABLE ONLY scenes
    ADD COLUMN slug text,
    ADD COLUMN lang text,
    ADD COLUMN image_src text,
    ADD COLUMN status text,
    ADD COLUMN owner_id int REFERENCES users (id);
--;;
