-- :name create-user! :<!
-- :doc creates a new user record
INSERT INTO users
(first_name, last_name, email, password, active, created_at, last_login)
VALUES (:first_name, :last_name, :email, :password, :active, :created_at, :last_login)
RETURNING id

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
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
INSERT INTO courses (name) VALUES (:name) RETURNING id

-- :name save-course! :<!
-- :doc creates a new course version record
INSERT INTO course_versions
(course_id, data, owner_id, created_at)
VALUES (:course_id, :data, :owner_id, :created_at) RETURNING id

-- :name get-course :? :1
-- :doc retrieve a course record given the name
SELECT * from courses
WHERE name = :name;

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

-- :name create-dataset! :<!
-- :doc creates a new dataset record
INSERT INTO datasets (course_id, name, scheme) VALUES (:course_id, :name, :scheme) RETURNING id

-- :name update-dataset! :! :n
-- :doc update an existing dataset record
UPDATE datasets
SET scheme = :scheme
WHERE id = :id

-- :name get-dataset :? :1
-- :doc retrieve dataset by id
SELECT * from datasets
WHERE id = :id;

-- :name get-datasets-by-course :? :*
-- :doc retrieve datasets given course id
SELECT * from datasets
WHERE course_id = :course_id;

-- :name create-dataset-item! :<!
-- :doc creates a new dataset item record
INSERT INTO dataset_items (dataset_id, data) VALUES (:dataset_id, :data) RETURNING id

-- :name get-dataset-item :? :1
-- :doc retrieve item given id
SELECT * from dataset_items
WHERE id = :id;

-- :name get-dataset-items :? :*
-- :doc retrieve items given dataset id
SELECT * from dataset_items
WHERE dataset_id = :dataset_id;

-- :name update-dataset-item! :! :n
-- :doc updates an existing dataset item record
UPDATE dataset_items
SET data = :data
WHERE id = :id

-- :name delete-dataset-item! :! :n
-- :doc deletes a dataset item record given the id
DELETE FROM dataset_items
WHERE id = :id

-- :name create-lesson-item! :<!
-- :doc creates a new lesson set item record
INSERT INTO lesson_sets
(name, dataset_id, item_id, item_order)
VALUES (:name, :dataset_id, :item_id, :item_order) RETURNING id

-- :name get-lesson-set :? :*
-- :doc retrieve lesson set given lesson set name
SELECT * from lesson_sets
WHERE name = :name ORDER BY item_order ASC;

-- :name update-lesson-item-order! :! :n
-- :doc updates an lesson item order
UPDATE lesson_sets
SET item_order = :item_order
WHERE id = :id

