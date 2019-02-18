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

-- :name create-action! :<!
-- :doc creates a new course action record
INSERT INTO course_actions
(user_id, course_id, created_at, type, data)
VALUES (:user_id, :course_id, :created_at, :type, :data)
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

-- :name get-user-course-stat :? :1
-- :doc retrieves course stats records for given user id and course id
SELECT * FROM course_stats
WHERE user_id = :user_id and course_id = :course_id

-- :name create-activity-stat! :<!
-- :doc creates a new activity stat record
INSERT INTO activity_stats
(user_id, course_id, level_number, activity_number, data)
VALUES (:user_id, :course_id, :level_number, :activity_number, :data)
RETURNING id

-- :name get-activity-stats :? :*
-- :doc retrieves activity stats records for given user id and course id
SELECT * FROM activity_stats
WHERE user_id = :user_id and course_id = :course_id