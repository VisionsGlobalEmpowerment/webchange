-- :name create-or-update-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, password, active, created_at, last_login)
VALUES (:id, :first_name, :last_name, :email, :password, :active, :created_at, :last_login)
ON CONFLICT ON CONSTRAINT users_pkey
DO UPDATE SET first_name=:first_name, last_name=:last_name, email=:email, password=:password,
active=:active, created_at=:created_at, last_login=:last_login WHERE users.id=:id

-- :name create-or-update-teacher! :! :n
-- :doc creates a new teacher record
INSERT INTO teachers
(id, user_id, school_id)
VALUES (:id, :user_id, :school_id)
ON CONFLICT ON CONSTRAINT teachers_pkey
DO UPDATE SET user_id=:user_id, school_id=:school_id WHERE teachers.id=:id


-- :name clear-teachers! :! :n
-- :doc deletes teachers
DELETE from teachers;

-- :name create-or-update-student! :! :n
-- :doc creates a new student record
INSERT INTO students
(id, class_id, user_id, school_id, access_code, gender, date_of_birth)
VALUES (:id, :class_id, :user_id, :school_id, :access_code, :gender, :date_of_birth)
ON CONFLICT ON CONSTRAINT students_pkey
DO UPDATE SET class_id=:class_id, user_id=:user_id, school_id=:school_id, access_code=:access_code,
gender=:gender, date_of_birth=:date_of_birth WHERE students.id=:id

-- :name create-or-update-class! :! :n
-- :doc creates a new student record
INSERT INTO classes
(id, name, school_id)
VALUES (:id, :name, :school_id)
ON CONFLICT ON CONSTRAINT classes_pkey
DO UPDATE SET name=:name, school_id=:school_id WHERE classes.id=:id

-- :name create-or-update-courses! :! :n
-- :doc creates a new student record
INSERT INTO courses (id, name, slug) VALUES (:id, :name, :slug)
ON CONFLICT ON CONSTRAINT courses_pkey
DO UPDATE SET name=:name, slug=:slug WHERE courses.id=:id

-- :name create-or-update-course-versions! :! :n
-- :doc creates a new student record
INSERT INTO course_versions
(id, course_id, data, owner_id, created_at)
VALUES (:id, :course_id, :data, :owner_id, :created_at)
ON CONFLICT ON CONSTRAINT course_versions_pkey
DO UPDATE SET course_id=:course_id, data=:data,
owner_id=:owner_id, created_at=:created_at WHERE course_versions.id=:id

-- :name get-course-versions-by-school :? :*
-- :doc retrieve course version by id
SELECT a.* FROM course_versions a
INNER JOIN (
    SELECT course_id, MAX(created_at) created_at
    FROM course_versions
    GROUP BY course_id
) b ON a.course_id = b.course_id AND a.created_at = b.created_at;

-- :name create-or-update-course-stat! :! :n
-- :doc creates a new course stat record
INSERT INTO course_stats
(id, user_id, class_id, course_id, data)
VALUES (:id, :user_id, :class_id, :course_id, :data)
ON CONFLICT ON CONSTRAINT course_stats_pkey
DO UPDATE SET user_id=:user_id, class_id=:class_id, course_id=:course_id, data=:data
WHERE course_stats.id=:id

-- :name create-or-update-progress! :! :n
-- :doc creates a new course progress record
INSERT INTO course_progresses
(id, user_id, course_id, data)
VALUES (:id, :user_id, :course_id, :data)
ON CONFLICT ON CONSTRAINT course_progresses_pkey
DO UPDATE SET user_id=:user_id, course_id=:course_id, data=:data
WHERE course_progresses.id=:id

-- :name create-or-update-event! :! :n
-- :doc creates a new course event record
INSERT INTO course_events
(id, user_id, course_id, created_at, type, data)
VALUES (:id, :user_id, :course_id, :created_at, :type, :data)
ON CONFLICT ON CONSTRAINT course_actions_pkey
DO UPDATE SET user_id=:user_id, course_id=:course_id, created_at=:created_at, type=:type, data=:data
WHERE course_events.id=:id

-- :name create-or-update-dataset! :! :n
-- :doc creates a new dataset record
INSERT INTO datasets (id, course_id, name, scheme) VALUES (:id, :course_id, :name, :scheme)
ON CONFLICT ON CONSTRAINT datasets_pkey
DO UPDATE SET course_id=:course_id, name=:name, scheme=:scheme
WHERE datasets.id=:id


-- :name create-or-update-dataset-item-with-id! :! :n
-- :doc creates a new dataset item record
INSERT INTO dataset_items (id, dataset_id, name, data) VALUES (:id, :dataset_id, :name, :data)
ON CONFLICT ON CONSTRAINT dataset_items_pkey
DO UPDATE SET dataset_id=:dataset_id, name=:name, data=:data
WHERE dataset_items.id=:id

-- :name create-or-update-lesson-set! :! :n
-- :doc creates a new lesson set item record
INSERT INTO lesson_sets
(id, name, dataset_id, data)
VALUES (:id, :name, :dataset_id, :data)
ON CONFLICT ON CONSTRAINT lesson_sets_pkey
DO UPDATE SET dataset_id=:dataset_id, name=:name, data=:data
WHERE lesson_sets.id=:id

-- :name create-or-update-scene! :! :n
-- :doc creates a new scene record
INSERT INTO scenes (id, course_id, name) VALUES (:id, :course_id, :name)
ON CONFLICT ON CONSTRAINT scenes_pkey
DO UPDATE SET course_id=:course_id, name=:name
WHERE scenes.id=:id

-- :name create-or-update-scene-version! :! :n
-- :doc creates a new course version record
INSERT INTO scene_versions
(id, scene_id, data, owner_id, created_at)
VALUES (:id, :scene_id, :data, :owner_id, :created_at)
ON CONFLICT ON CONSTRAINT scene_versions_pkey
DO UPDATE SET scene_id=:scene_id, data=:data, owner_id=:owner_id, created_at=:created_at
WHERE scene_versions.id=:id

-- :name get-scene-versions-by-school :? :*
-- :doc retrieve scene version by id
SELECT a.*
FROM scene_versions a
INNER JOIN (
    SELECT scene_id, MAX(created_at) created_at
    FROM scene_versions
    GROUP BY scene_id
) b ON a.scene_id = b.scene_id AND a.created_at = b.created_at;

-- :name create-or-update-activity-stat! :! :n
-- :doc creates a new activity stat record
INSERT INTO activity_stats
(id, user_id, course_id, activity_id, data)
VALUES (:id, :user_id, :course_id, :activity_id, :data)
ON CONFLICT ON CONSTRAINT activity_stats_pkey
DO UPDATE SET user_id=:user_id, course_id=:course_id, activity_id=:activity_id, data=:data
WHERE activity_stats.id=:id

