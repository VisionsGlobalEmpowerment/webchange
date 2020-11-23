CREATE TABLE collaborators (
  course_id   int REFERENCES courses (id),
  user_id   int REFERENCES users (id),
  CONSTRAINT collaborators_pkey PRIMARY KEY (course_id, user_id)
);
