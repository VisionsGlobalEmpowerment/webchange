-- :name create-user! :<!
-- :doc creates a new user record
INSERT INTO users
(first_name, last_name, email, password, active, created_at, last_login, website_id)
VALUES (:first_name, :last_name, :email, :password, :active, :created_at, :last_login, :website_id)
RETURNING id

-- :name update-student-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name
WHERE id = :id

-- :name update-website-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE website_id = :website_id

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

-- :name find-user-by-website-id :? :1
-- :doc retrieves a user record given the website id
SELECT * FROM users
WHERE website_id = :website_id

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE id = :id;

-- :name get-users-by-school :? :*
-- :doc retrieve all versions of given course
SELECT u.* FROM users u
LEFT JOIN teachers t ON (t.user_id=u.id)
LEFT JOIN students s ON (s.user_id=u.id)
WHERE t.school_id = :school_id or s.school_id = :school_id;

-- :name create-course! :<!
-- :doc creates a new course record
INSERT INTO courses (name, slug, lang, image_src, status, owner_id, website_user_id) VALUES (:name, :slug, :lang, :image_src, :status, :owner_id, :website_user_id) RETURNING id

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

-- :name get-course-by-id :? :1
-- :doc retrieve a course record given the id
SELECT * from courses
WHERE id = :id;

-- :name get-course :? :1
-- :doc retrieve a course record given the slug
SELECT * from courses
WHERE slug = :slug;

-- :name get-available-courses :? :*
-- :doc retrieve all available courses
SELECT * from courses
WHERE status = 'published' ORDER BY name DESC LIMIT 30;

-- :name get-courses-by-website-user :? :*
-- :doc retrieve draft courses given website user id
SELECT * from courses
WHERE status = 'draft' AND website_user_id = :website_user_id ORDER BY name DESC LIMIT 30;

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

-- :name get-courses :? :*
-- :doc retrieve a course record given the name
SELECT * from courses;


-- :name find-courses-by-name :? :*
-- :doc retrieve a course record given the name
SELECT * from courses where name=:name;

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

-- :name get-scenes :? :*
-- :doc retrieve a scene record given the course id and the name
SELECT * from scenes;

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

-- :name delete-scene-skills! :! :n
-- :doc deletes all scene skills
DELETE FROM scene_skills
WHERE scene_id = :scene_id;

-- :name create-scene-skill! :! :n
-- :doc creates a new scene-skill link
INSERT INTO scene_skills (scene_id, skill_id) VALUES (:scene_id, :skill_id);
