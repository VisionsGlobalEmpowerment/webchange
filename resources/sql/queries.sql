-- :name create-user! :<!
-- :doc creates a new user record
INSERT INTO users
(first_name, last_name, email, password, active, created_at, last_login)
VALUES (:first_name, :last_name, :email, :password, :active, :created_at, :last_login)
RETURNING id

-- :name update-student-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name
WHERE id = :id

-- :name activate-user! :! :n
-- :doc activates an existing user record
UPDATE users
SET active = true
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE id = :id

-- :name find-user-by-email :? :1
-- :doc retrieves a user record given the email
SELECT * FROM users
WHERE email = :email

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE id = :id

-- :name create-course! :<!
-- :doc creates a new course record
INSERT INTO courses (name, slug) VALUES (:name, :slug) RETURNING id

-- :name save-course-info! :! :n
-- :doc updates an existing course record
UPDATE courses
SET name = :name, slug = :slug, lang = :lang, image_src = :image_src
WHERE id = :id

-- :name save-course! :<!
-- :doc creates a new course version record
INSERT INTO course_versions
(course_id, data, owner_id, created_at)
VALUES (:course_id, :data, :owner_id, :created_at) RETURNING id

-- :name get-course :? :1
-- :doc retrieve a course record given the name
SELECT * from courses
WHERE slug = :slug;

-- :name get-course-version :? :1
-- :doc retrieve course version by id
SELECT * from course_versions
WHERE id = :id;

-- :name get-latest-course-version :? :1
-- :doc retrieve last version of given course
SELECT * from course_versions
WHERE course_id = :course_id ORDER BY created_at DESC LIMIT 1;

-- :name get-course-versions :? :*
-- :doc retrieve all versions of given course
SELECT * from course_versions
WHERE course_id = :course_id ORDER BY created_at DESC LIMIT 30;

-- :name create-scene! :<!
-- :doc creates a new scene record
INSERT INTO scenes (course_id, name) VALUES (:course_id, :name) RETURNING id

-- :name save-scene! :<!
-- :doc creates a new course version record
INSERT INTO scene_versions
(scene_id, data, owner_id, created_at)
VALUES (:scene_id, :data, :owner_id, :created_at) RETURNING id

-- :name get-scene :? :1
-- :doc retrieve a scene record given the course id and the name
SELECT * from scenes
WHERE course_id = :course_id AND name = :name;

-- :name get-scene-version :? :1
-- :doc retrieve scene version by id
SELECT * from scene_versions
WHERE id = :id;

-- :name get-latest-scene-version :? :1
-- :doc retrieve last version of given scene
SELECT * from scene_versions
WHERE scene_id = :scene_id ORDER BY created_at DESC LIMIT 1;

-- :name get-scene-versions :? :*
-- :doc retrieve all versions of given scene
SELECT * from scene_versions
WHERE scene_id = :scene_id ORDER BY created_at DESC LIMIT 30;
