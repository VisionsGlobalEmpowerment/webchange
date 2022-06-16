-- :name create-user! :<!
-- :doc creates a new user record
INSERT INTO users
(first_name, last_name, email, password, active, created_at, last_login, website_id, type)
VALUES (:first_name, :last_name, :email, :password, :active, :created_at, :last_login, :website_id, :type)
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

-- :name set-account-status! :! :n
-- :doc activates an existing user record
UPDATE users
SET active = :active
WHERE id = :id

-- :name change-password! :! :n
-- :doc user user password
UPDATE users
SET password = :password
WHERE id = :id

-- :name edit-teacher-user! :! :n
-- :doc edit user
UPDATE users
SET first_name = :first_name, last_name = :last_name
WHERE id = :id

-- :name edit-account! :! :n
-- :doc edit user
UPDATE users
SET first_name = :first_name, last_name = :last_name, type = :type
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
INSERT INTO courses (name, slug, lang, image_src, status, owner_id, website_user_id, type) VALUES (:name, :slug, :lang, :image_src, :status, :owner_id, :website_user_id, :type) RETURNING id

-- :name save-course-info! :! :n
-- :doc updates an existing course record
UPDATE courses
SET name = :name, slug = :slug, lang = :lang, image_src = :image_src, metadata = :metadata
WHERE id = :id

-- :name save-course-data! :! :n
-- :doc updates an existing course record
UPDATE course_versions
SET data = :data
WHERE id = :id

-- :name update-course-status! :! :n
-- :doc updates an existing course record status
UPDATE courses
SET status = :status
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
SELECT c.*,t.updated_at from courses c
JOIN (SELECT course_id, max(created_at) as updated_at from course_versions group by course_id) t on(c.id=t.course_id)
WHERE status = 'published'
ORDER BY updated_at DESC, name DESC
LIMIT 130;

-- :name get-courses-by-status-and-type :? :*
-- :doc retrieve all available courses
SELECT c.*, t.updated_at from courses c
JOIN (SELECT course_id, max(created_at) as updated_at from course_versions group by course_id) t on(c.id=t.course_id)
WHERE status = :status AND type = :type
ORDER BY updated_at DESC, name DESC
LIMIT 130;

-- :name get-courses-by-website-user :? :*
-- :doc retrieve draft courses given website user id
SELECT c.*,t.updated_at from courses c
JOIN (SELECT course_id, max(created_at) as updated_at from course_versions group by course_id) t on(c.id=t.course_id)
WHERE status != 'archived' AND type = :type AND website_user_id = :website_user_id
ORDER BY updated_at DESC, name DESC
LIMIT 250;

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
INSERT INTO scenes (course_id, name, slug) VALUES (:course_id, :name, :name) RETURNING id

-- :name save-scene! :<!
-- :doc creates a new course version record
INSERT INTO scene_versions
(scene_id, data, owner_id, created_at, description)
VALUES (:scene_id, :data, :owner_id, :created_at, :description) RETURNING id

-- :name get-scene :? :1
-- :doc retrieve a scene record given the course id and the name
SELECT * from scenes
WHERE course_id = :course_id AND name = :name
ORDER BY id ASC LIMIT 1;

-- :name get-course-scene :? :1
-- :doc retrieve a scene record given the course id and the name
SELECT s.* from scenes s
INNER JOIN course_scenes cs
ON s.id = cs.scene_id
WHERE cs.course_id = :course_id AND s.slug = :slug
ORDER BY s.id ASC LIMIT 1;

-- :name get-scene-by-id :? :1
-- :doc retrieve a scene record given the id
SELECT * from scenes
WHERE id = :id;

-- :name get-scenes :? :*
-- :doc retrieve all scene records
SELECT * from scenes;

-- :name get-scenes-by-type :? :*
-- :doc retrieve all scene records
SELECT * from scenes
WHERE type = :type AND status != 'archived';

-- :name get-scenes-by-type-and-status :? :*
-- :doc retrieve all scene records
SELECT * from scenes
WHERE type = :type AND status = :status;

-- :name get-scenes-by-course-id :? :*
-- :doc retrieve scenes by course id
SELECT s.* from scenes s
INNER JOIN course_scenes cs ON s.id = cs.scene_id
WHERE cs.course_id = :course_id;

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

-- :name get-scene-skills :? :*
-- :doc retrieve all scene skill records
SELECT * FROM scene_skills;

-- :name get-scene-skills-by-scene :? :*
-- :doc retrieve all scene skill records
SELECT * FROM scene_skills WHERE scene_id=:scene_id;

-- :name add-collaborator! :! :n
-- :doc creates a new course-collaborator link
INSERT INTO collaborators (course_id, user_id) VALUES (:course_id, :user_id);

-- :name is-collaborator? :? :1
-- :doc check if user is collaborator on given course
SELECT true as result from collaborators WHERE course_id = :course_id AND user_id = :user_id

-- :name delete-collaborators-by-course-id! :! :n
-- :doc deletes course collaborators
DELETE from collaborators where course_id=:course_id;

-- :name is-admin? :? :1
-- :doc check if user is admin
SELECT true as result from users WHERE id = :id AND type = 'admin';

-- :name accounts-by-type :? :*
-- :doc retrieves all user record with given type
SELECT * FROM users
WHERE type = :type
LIMIT :limit OFFSET :offset

-- :name count-accounts-by-type :? :1
-- :doc count records
SELECT count(*) as result FROM users
WHERE type = :type

-- :name insert-course-scenes :! :n
-- :doc Batch insert into course scenes
INSERT INTO course_scenes (course_id, scene_id)
VALUES :tuple*:course_scenes

-- :name delete-course-scenes :! :n
-- :doc Delete course scenes
DELETE FROM course_scenes
WHERE course_id = :course_id
AND scene_id IN (:v*:scene_ids)

-- :name delete-course-scenes-by-scene-id! :! :n
-- :doc Delete course scenes
DELETE FROM course_scenes
WHERE scene_id = :scene_id

-- :name course-scenes :? :*
-- :doc retrieve course scenes by course id
SELECT * FROM course_scenes
WHERE course_id = :course_id

-- :name scene-list :? :*
-- :doc retrieve course scenes by course id
SELECT s.* FROM scenes s
INNER JOIN course_scenes cs ON s.id = cs.scene_id
WHERE cs.course_id = :course_id

-- :name update-scene-image! :! :n
-- :doc updates an existing scene record image
UPDATE scenes
SET image_src = :image_src
WHERE id = :id

-- :name edit-scene! :! :n
-- :doc updates an existing scene record
UPDATE scenes
SET name = :name, lang = :lang, metadata = :metadata
WHERE id = :id

-- :name update-scene-status! :! :n
-- :doc updates an existing scene record status
UPDATE scenes
SET status = :status
WHERE id = :id
