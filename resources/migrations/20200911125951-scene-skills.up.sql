CREATE TABLE scene_skills (
  scene_id   int REFERENCES scenes (id),
  skill_id   int,
  CONSTRAINT scene_skill_pkey PRIMARY KEY (scene_id, skill_id)
);
