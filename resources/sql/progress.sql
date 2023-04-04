-- :name create-progress! :<!
-- :doc creates a new course progress record
INSERT INTO course_progresses
(user_id, course_id, data)
VALUES (:user_id, :course_id, :data)
RETURNING id

-- :name save-progress! :! :n
-- :doc updates an existing course progress record
UPDATE course_progresses
SET data = :data
WHERE id = :id

-- :name get-progress :? :1
-- :doc retrieves a progress record given the user id and course id
SELECT * FROM course_progresses
WHERE user_id = :user_id and course_id = :course_id

-- :name get-progress-for-course :? :*
-- :doc get all the users associated with course id
-- :result many
SELECT * FROM course_progresses
WHERE course_id = :course_id

-- :name get-course-progresses-by-school :? :*
-- :doc retrieves a progress record given the user id and course id
SELECT cp.* FROM course_progresses cp
JOIN users u ON (cp.user_id=u.id)
JOIN students s ON (s.user_id=u.id)
WHERE s.school_id = :school_id

-- :name get-course-events-by-school :? :*
-- :doc retrieves a progress record given the user id and course id
SELECT ce.* FROM course_events ce
JOIN users u ON (ce.user_id=u.id)
JOIN students s ON (s.user_id=u.id)
WHERE s.school_id = :school_id

-- :name create-event! :<!
-- :doc creates a new course event record
INSERT INTO course_events
(user_id, course_id, created_at, type, guid, data)
VALUES (:user_id, :course_id, :created_at, :type, :guid, :data)
RETURNING id

-- :name create-course-stat! :<!
-- :doc creates a new course stat record
INSERT INTO course_stats
(user_id, class_id, course_id, data)
VALUES (:user_id, :class_id, :course_id, :data)
RETURNING id

-- :name get-course-stats :? :*
-- :doc retrieves course stats records for given class id and course id
SELECT * FROM course_stats
WHERE class_id = :class_id and course_id = :course_id

-- :name get-course-stats-by-school :? :*
-- :doc retrieves course stats records for given user id and course id
SELECT cs.* FROM course_stats cs
JOIN users u ON (cs.user_id=u.id)
JOIN students s ON (s.user_id=u.id)
WHERE s.school_id = :school_id

-- :name get-user-course-stat :? :1
-- :doc retrieves course stats records for given user id and course id
SELECT * FROM course_stats
WHERE user_id = :user_id and course_id = :course_id

-- :name save-course-stat! :! :n
-- :doc updates an existing course stat record
UPDATE course_stats
SET data = :data
WHERE id = :id

-- :name update-course-stat-class! :! :n
-- :doc updates a course stat class by user
UPDATE course_stats
SET class_id = :class_id
WHERE user_id = :user_id

-- :name unassign-course-stat! :! :n
-- :doc unassigns course stat from class
UPDATE course_stats
SET class_id = null
WHERE user_id = :user_id

-- :name delete-course-stat! :! :n
-- :doc deletes course stat
DELETE from course_stats
WHERE user_id = :user_id

-- :name create-activity-stat! :<!
-- :doc creates a new activity stat record
INSERT INTO activity_stats
(user_id, course_id, unique_id, data)
VALUES (:user_id, :course_id, :unique_id, :data)
RETURNING id

-- :name get-user-activity-stats :? :*
-- :doc retrieves activity stats records for given user id and course id
SELECT * FROM activity_stats
WHERE user_id = :user_id and course_id = :course_id

-- :name get-activity-stat :? :1
-- :doc retrieves activity stat record for given user id, course id and unique activity id
SELECT * FROM activity_stats
WHERE user_id = :user_id and course_id = :course_id and unique_id = :unique_id

-- :name save-activity-stat! :! :n
-- :doc updates an existing activity stat record
UPDATE activity_stats
SET data = :data
WHERE id = :id

-- :name get-activity-stats-by-school :? :*
-- :doc retrieves activity stats records for given school
SELECT ast.* FROM activity_stats ast
JOIN users u ON (ast.user_id=u.id)
JOIN students s ON (s.user_id=u.id)
WHERE s.school_id = :school_id

-- :name get-class-students-progress :? :*
-- :doc retrieves activity stats records for given school
SELECT ast.* FROM activity_stats ast
INNER JOIN users u ON (ast.user_id=u.id)
INNER JOIN students s ON (s.user_id=u.id)
WHERE s.class_id = :class_id

-- :name get-school-stat :? :1
-- :doc retrieves school stats records for given school id
SELECT * FROM school_stats
WHERE school_id = :school_id

-- :name create-school-stat! :! :n
-- :doc creates a new school stat record
INSERT INTO school_stats
(school_id, data)
VALUES (:school_id, :data)

-- :name save-school-stat! :! :n
-- :doc updates an existing school stat record
UPDATE school_stats
SET data = :data
WHERE school_id = :school_id

-- :name get-class-stat :? :1
-- :doc retrieves class stats records for given class id
SELECT * FROM class_stats
WHERE class_id = :class_id

-- :name create-class-stat! :! :n
-- :doc creates a new class stat record
INSERT INTO class_stats
(class_id, data)
VALUES (:class_id, :data)

-- :name save-class-stat! :! :n
-- :doc updates an existing class stat record
UPDATE class_stats
SET data = :data
WHERE class_id = :class_id
