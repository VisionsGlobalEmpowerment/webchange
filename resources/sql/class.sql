-- :name create-school! :<!
-- :doc creates a new school record
INSERT INTO schools
(id, name)
VALUES (:id, :name) RETURNING id

-- :name create-class! :<!
-- :doc creates a new class record
INSERT INTO classes
(name, course_id, school_id)
VALUES (:name, :course_id, :school_id) RETURNING id

-- :name update-class! :! :n
-- :doc updates an existing class record
UPDATE classes
SET name = :name, course_id = :course_id
WHERE id = :id

-- :name delete-class! :! :n
-- :doc deletes class
DELETE from classes
WHERE id = :id

-- :name create-student! :<!
-- :doc creates a new student record
INSERT INTO students
(class_id, user_id, school_id, access_code, gender, date_of_birth)
VALUES (:class_id, :user_id, :school_id, :access_code, :gender, :date_of_birth) RETURNING id

-- :name update-student! :! :n
-- :doc updates an existing student record class
UPDATE students
SET class_id = :class_id, gender = :gender, date_of_birth = :date_of_birth
WHERE id = :id

-- :name update-student-access-code! :! :n
-- :doc updates an existing student record access-code
UPDATE students
SET access_code = :access_code
WHERE id = :id

-- :name unassign-student! :! :n
-- :doc unassigns student
UPDATE students
SET class_id = null
WHERE user_id = :user_id

-- :name delete-student! :! :n
-- :doc deletes student
DELETE from students
WHERE user_id = :user_id

-- :name get-classes :? :*
-- :doc retrieve all classes by school
SELECT cl.*, cr.slug as course_slug
FROM classes cl
LEFT JOIN courses cr ON cl.course_id = cr.id
WHERE school_id = :school_id

-- :name get-class :? :1
-- :doc retrieve class record
SELECT cl.*, cr.slug as course_slug
FROM classes cl
LEFT JOIN courses cr ON cl.course_id = cr.id
WHERE cl.id = :id;

-- :name get-students-by-class :? :*
-- :doc retrieve students given class id
SELECT * from students
WHERE class_id = :class_id

-- :name get-students-by-school :? :*
-- :doc retrieve students given class id
SELECT * from students
WHERE school_id = :school_id

-- :name get-students-unassigned :? :*
-- :doc retrieve students without a class
SELECT * from students
WHERE class_id is null

-- :name get-student :? :1
-- :doc retrieve students by id
SELECT * from students
WHERE id = :id

-- :name get-student-by-user :? :1
-- :doc retrieve student by user id
SELECT * from students
WHERE user_id = :user_id

-- :name find-student-by-code :? :1
-- :doc retrieve student by access code and school id
SELECT * from students
WHERE school_id = :school_id AND access_code = :access_code

-- :name create-teacher! :<!
-- :doc creates a new teacher record
INSERT INTO teachers
(user_id, school_id, type, status)
VALUES (:user_id, :school_id, :type, :status) RETURNING id

-- :name edit-teacher! :! :n
-- :doc edit teacher
UPDATE teachers
SET type = :type, status = :status
WHERE id = :id

-- :name get-teacher :? :1
-- :doc retrieve teacher by id
SELECT * from teachers
WHERE id = :id

-- :name get-teacher-by-user :? :1
-- :doc retrieve teacher by user id
SELECT * from teachers
WHERE user_id = :user_id

-- :name teachers-by-school :? :*
-- :doc retrieve teachers by school id
SELECT * from teachers
WHERE school_id = :school_id

-- :name teachers-by-class :? :*
-- :doc retrieve teacher by class id
SELECT * from teachers t
INNER JOIN class_teachers ct ON ct.teacher.id = t.id
WHERE class_id = :class_id

-- :name get-first-school :? :1
-- :doc retrieve first school record
SELECT * from schools ORDER BY ID ASC LIMIT 1

-- :name access-code-exists? :? :1
-- :doc retrieve first school record
SELECT true as result from students WHERE school_id = :school_id AND access_code = :access_code

-- :name create-new-school! :<!
-- :doc creates a new school record
INSERT INTO schools
(name, location, about)
VALUES (:name, :location, :about) RETURNING id

-- :name get-school :? :1
-- :doc retrieve school record
SELECT * from schools
WHERE id = :id

-- :name get-school-students :? :*
-- :doc retrieve school students
SELECT * from students
WHERE school_id = :id

-- :name get-schools :? :*
-- :doc retrieve school records
SELECT * from schools

-- :name update-school! :! :n
-- :doc updates an existing school record
UPDATE schools
SET name = :name, location = :location, about = :about
WHERE id = :id

-- :name delete-school! :! :n
-- :doc deletes school
DELETE from schools
WHERE id = :id

-- :name update-school-stats! :! :n
-- :doc updates an existing school stats
UPDATE schools
SET stats = :stats
WHERE id = :id

-- :name calculate-school-stats :? :1
-- :doc retrieve school statistics
SELECT
(SELECT count(*) FROM teachers WHERE school_id = :id) as teachers,
(SELECT count(*) FROM students WHERE school_id = :id) as students,
(SELECT count(*) FROM school_courses WHERE school_id = :id) as courses,
(SELECT count(*) FROM classes WHERE school_id = :id) as classes

-- :name is-school-teacher? :? :1
-- :doc check if user is a teacher in given school
SELECT true as result from teachers
WHERE
school_id = :school_id AND user_id = :user_id

-- :name is-school-admin? :? :1
-- :doc check if user is an admin teacher in given school
SELECT true as result from teachers
WHERE
school_id = :school_id AND user_id = :user_id AND type = 'admin'

-- :name assign-school-course! :! :n
-- :doc creates a new school_course record
INSERT INTO school_courses
(school_id, course_id)
VALUES (:school_id, :course_id)

-- :name get-courses-by-school :? :*
-- :doc retrieve all courses by school
SELECT c.*
FROM courses c
INNER JOIN school_courses sc ON sc.course_id = c.id
WHERE sc.school_id = :school_id

-- :name calculate-class-stats :? :1
-- :doc retrieve class statistics
SELECT
0 as teachers,
(SELECT count(*) FROM students WHERE class_id = :id) as students

-- :name update-class-stats! :! :n
-- :doc updates an existing class stats
UPDATE classes
SET stats = :stats
WHERE id = :id
