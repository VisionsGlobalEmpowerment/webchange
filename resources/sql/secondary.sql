-- :name create-or-update-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, password, active, created_at, last_login, guid)
VALUES (:id, :first_name, :last_name, :email, :password, :active, :created_at, :last_login, :guid)
ON CONFLICT ON CONSTRAINT users_pkey
DO UPDATE SET first_name=:first_name, last_name=:last_name, email=:email, password=:password,
active=:active, created_at=:created_at, last_login=:last_login WHERE users.guid=:guid

-- :name reset-users-seq! :? :1
SELECT pg_catalog.setval('public.users_id_seq', (SELECT MAX(id) FROM public.users), true);

-- :name create-or-update-user-by-guid! :! :n
-- :doc creates a new user record
INSERT INTO users
(guid, first_name, last_name, email, password, active, created_at, last_login)
VALUES (:guid, :first_name, :last_name, :email, :password, :active, :created_at, :last_login)
ON CONFLICT ON CONSTRAINT user_unique
DO UPDATE SET first_name=:first_name, last_name=:last_name, email=:email, password=:password,
active=:active, created_at=:created_at, last_login=:last_login WHERE users.guid=:guid


-- :name create-or-update-teacher! :! :n
-- :doc creates a new teacher record
INSERT INTO teachers
(user_id, school_id)
VALUES (:user_id, :school_id)
ON CONFLICT ON CONSTRAINT teacher_unique
DO NOTHING;

-- :name clear-teachers! :! :n
-- :doc deletes teachers
DELETE from teachers;

-- :name create-or-update-student! :! :n
-- :doc creates a new student record
INSERT INTO students
(class_id, user_id, school_id, access_code, gender, date_of_birth)
VALUES (:class_id, :user_id, :school_id, :access_code, :gender, :date_of_birth)
ON CONFLICT ON CONSTRAINT student_unique
DO UPDATE SET school_id=:school_id, access_code=:access_code,
gender=:gender, date_of_birth=:date_of_birth, class_id=:class_id WHERE students.user_id=:user_id

-- :name create-or-update-class! :! :n
-- :doc creates a new student record
INSERT INTO classes
(id, name, school_id, guid)
VALUES (:id, :name, :school_id, :guid)
ON CONFLICT ON CONSTRAINT classes_pkey
DO UPDATE SET name=:name, school_id=:school_id WHERE classes.id=:id

-- :name reset-classes-seq! :? :1
SELECT pg_catalog.setval('public.classes_id_seq', (SELECT MAX(id) FROM public.classes), true);

-- :name create-or-update-class-by-guid! :! :n
-- :doc creates a new student record
INSERT INTO classes
(guid, name, school_id)
VALUES (:guid, :name, :school_id)
ON CONFLICT ON CONSTRAINT class_unique
DO UPDATE SET name=:name, school_id=:school_id WHERE classes.guid=:guid

-- :name create-or-update-courses! :! :n
-- :doc creates a new course record
INSERT INTO courses (id, name, slug, lang, image_src, status, owner_id, website_user_id, type)
VALUES (:id, :name, :slug, :lang, :image_src, :status, :owner_id, :website_user_id, :type)
ON CONFLICT ON CONSTRAINT courses_pkey
DO UPDATE SET name=:name, slug=:slug, lang=:lang, image_src=:image_src, status=:status,
owner_id=:owner_id, website_user_id=:website_user_id, type=:type
WHERE courses.id=:id

-- :name reset-courses-seq! :? :1
SELECT pg_catalog.setval('public.courses_id_seq', (SELECT MAX(id) FROM public.courses), true);

-- :name create-or-update-scene-skills!  :! :n
INSERT INTO scene_skills (scene_id, skill_id)
VALUES (:scene_id, :skill_id)
ON CONFLICT ON CONSTRAINT scene_skill_pkey
DO NOTHING;

-- :name create-or-update-course-versions! :! :n
-- :doc creates a new student record
INSERT INTO course_versions
(id, course_id, data, owner_id, created_at)
VALUES (:id, :course_id, :data, :owner_id, :created_at)
ON CONFLICT ON CONSTRAINT course_versions_pkey
DO UPDATE SET course_id=:course_id, data=:data,
owner_id=:owner_id, created_at=:created_at WHERE course_versions.id=:id

-- :name reset-course-versions-seq! :? :1
SELECT pg_catalog.setval('public.course_versions_id_seq', (SELECT MAX(id) FROM public.course_versions), true);

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
(user_id, class_id, course_id, data)
VALUES (:user_id, :class_id, :course_id, :data)
ON CONFLICT ON CONSTRAINT course_stats_unique
DO UPDATE SET  data=:data
WHERE course_stats.user_id=:user_id and course_stats.class_id=:class_id and course_stats.course_id=:course_id;

-- :name create-or-update-progress! :! :n
-- :doc creates a new course progress record
INSERT INTO course_progresses
(user_id, course_id, data)
VALUES (:user_id, :course_id, :data)
ON CONFLICT ON CONSTRAINT course_progresses_unique
DO UPDATE SET data=:data
WHERE course_progresses.user_id=:user_id and course_progresses.course_id=:course_id;

-- :name create-or-update-event! :! :n
-- :doc creates a new course event record
INSERT INTO course_events
(user_id, course_id, created_at, type, guid, data)
VALUES (:user_id, :course_id, :created_at, :type, :guid, :data)
ON CONFLICT ON CONSTRAINT course_events_unique
DO UPDATE SET data=:data WHERE course_events.guid=:guid;

-- :name find-course-events-by-id :? :1
-- :doc retrieves a progress record given the user id and course id
SELECT * FROM course_events
WHERE id = :id

-- :name create-or-update-dataset! :! :n
-- :doc creates a new dataset record
INSERT INTO datasets (id, course_id, name, scheme) VALUES (:id, :course_id, :name, :scheme)
ON CONFLICT ON CONSTRAINT datasets_pkey
DO UPDATE SET course_id=:course_id, name=:name, scheme=:scheme
WHERE datasets.id=:id

-- :name reset-dataset-seq! :? :1
-- :doc retrieves a user record given the id
SELECT pg_catalog.setval('public.datasets_id_seq', (SELECT MAX(id) FROM public.datasets), true);

-- :name clear-dataset-items! :! :<!
-- :doc truncate table
TRUNCATE TABLE dataset_items;

-- :name create-or-update-dataset-item-with-id! :! :n
-- :doc creates a new dataset item record
INSERT INTO dataset_items (id, dataset_id, name, data) VALUES (:id, :dataset_id, :name, :data)
ON CONFLICT ON CONSTRAINT dataset_items_pkey
DO UPDATE SET dataset_id=:dataset_id, name=:name, data=:data
WHERE dataset_items.id=:id

-- :name reset-dataset-items-seq! :? :1
SELECT pg_catalog.setval('public.dataset_items_id_seq', (SELECT MAX(id) FROM public.dataset_items), true);

-- :name create-or-update-lesson-set! :! :n
-- :doc creates a new lesson set item record
INSERT INTO lesson_sets
(id, name, dataset_id, data)
VALUES (:id, :name, :dataset_id, :data)
ON CONFLICT ON CONSTRAINT lesson_sets_pkey
DO UPDATE SET dataset_id=:dataset_id, name=:name, data=:data
WHERE lesson_sets.id=:id

-- :name reset-lesson-sets-seq! :? :1
SELECT pg_catalog.setval('public.lesson_sets_id_seq', (SELECT MAX(id) FROM public.lesson_sets), true);

-- :name create-or-update-scene! :! :n
-- :doc creates a new scene record
INSERT INTO scenes (id, course_id, name, licensed) VALUES (:id, :course_id, :name, :licensed)
ON CONFLICT ON CONSTRAINT scenes_pkey
DO UPDATE SET course_id=:course_id, name=:name, licensed=:licensed
WHERE scenes.id=:id

-- :name reset-scenes-seq! :? :1
SELECT pg_catalog.setval('public.scenes_id_seq', (SELECT MAX(id) FROM public.scenes), true);

-- :name create-or-update-scene-version! :! :n
-- :doc creates a new course version record
INSERT INTO scene_versions
(id, scene_id, data, owner_id, created_at)
VALUES (:id, :scene_id, :data, :owner_id, :created_at)
ON CONFLICT ON CONSTRAINT scene_versions_pkey
DO UPDATE SET scene_id=:scene_id, data=:data, owner_id=:owner_id, created_at=:created_at
WHERE scene_versions.id=:id

-- :name reset-scene-versions-seq! :? :1
SELECT pg_catalog.setval('public.scene_versions_id_seq', (SELECT MAX(id) FROM public.scene_versions), true);

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
(user_id, course_id, activity_id, data)
VALUES (:user_id, :course_id, :activity_id, :data)
ON CONFLICT ON CONSTRAINT activity_stats_unique
DO UPDATE SET data=:data
WHERE activity_stats.user_id=:user_id and activity_stats.course_id=:course_id and activity_stats.activity_id=:activity_id;

-- :name find-users-by-guid :? :*
-- :doc Characters with returned columns specified
select * from users where guid in (:v*:guids);

-- :name delete-activity-stats-by-user-id! :! :n
-- :doc deletes activity stats
DELETE from activity_stats where user_id=:user_id;

-- :name delete-activity-stats-by-id! :! :n
-- :doc deletes activity stats
DELETE from activity_stats where id=:id;

-- :name delete-course-events-by-user-id! :! :n
-- :doc deletes course events
DELETE from course_events where user_id=:user_id;

-- :name delete-course-events-by-id! :! :n
-- :doc deletes course events
DELETE from course_events where id=:id;

-- :name delete-course-events-by-course-id! :! :n
-- :doc deletes course events
DELETE from course_events where course_id=:course_id;

-- :name delete-course-progresses-by-user-id! :! :n
-- :doc deletes course progresses
DELETE from course_progresses where user_id=:user_id;

-- :name delete-course-progresses-by-id! :! :n
-- :doc deletes course progresses
DELETE from course_progresses where id=:id;

-- :name delete-course-stats-by-class-id! :! :n
-- :doc deletes course progresses
DELETE from course_stats where class_id=:class_id;

-- :name delete-course-stats-by-user-id! :! :n
-- :doc deletes course progresses
DELETE from course_stats where user_id=:user_id;

-- :name delete-course-stats-by-id! :! :n
-- :doc deletes course progresses
DELETE from course_stats where id=:id;

-- :name delete-course-stats-by-course-id! :! :n
-- :doc deletes course progresses
DELETE from course_stats where course_id=:course_id;


-- :name delete-teachers-by-user-id! :! :n
-- :doc deletes teachers
DELETE from teachers where user_id=:user_id;

-- :name delete-student-by-class-id! :! :n
-- :doc deletes student
DELETE from students
WHERE class_id = :class_id

-- :name delete-student-by-id! :! :n
-- :doc deletes student
DELETE from students
WHERE id = :id

-- :name delete-teacher-by-id! :! :n
-- :doc deletes teacher by id
DELETE from teachers WHERE id=:id;

-- :name delete-course-version-by-course-id! :! :n
-- :doc deletes course versions by course id
DELETE from course_versions WHERE course_id=:course_id;

-- :name delete-course-progresses-by-course-id! :! :n
-- :doc deletes course progresses
DELETE from course_progresses where course_id=:course_id;

-- :name delete-course-by-id! :! :n
-- :doc deletes courses by id
DELETE from courses WHERE id=:id;

-- :name delete-course-version-by-id! :! :n
-- :doc deletes course-version by id
DELETE from course_versions WHERE id=:id;

-- :name delete-dataset-by-id! :! :n
-- :doc deletes dataset by id
DELETE from datasets WHERE id=:id;

-- :name delete-dataset-item-by-id! :! :n
-- :doc deletes dataset-item by id
DELETE from dataset_items WHERE id=:id;

-- :name delete-lesson-set-by-id! :! :n
-- :doc deletes lesson-set by id
DELETE from lesson_sets WHERE id=:id;

-- :name delete-scene-by-id! :! :n
-- :doc deletes scene by id
DELETE from scenes WHERE id=:id;

-- :name delete-scene-version-by-id! :! :n
-- :doc deletes scene-version by id
DELETE from scene_versions WHERE id=:id;

-- :name delete-scene-version-by-scene-id! :! :n
-- :doc deletes scene versions by scene id
DELETE from scene_versions WHERE scene_id=:scene_id;

-- :name delete-scene-skills-by-scene-skill! :! :n
-- :doc deletes all scene skills
DELETE FROM scene_skills
WHERE scene_id = :scene_id and skill_id = :skill_id;
