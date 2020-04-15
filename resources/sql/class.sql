-- :name create-school! :<!
-- :doc creates a new school record
INSERT INTO schools
(id, name)
VALUES (:id, :name) RETURNING id

-- :name create-class! :<!
-- :doc creates a new class record
INSERT INTO classes
(name, school_id)
VALUES (:name, :school_id) RETURNING id

-- :name update-class! :! :n
-- :doc updates an existing class record
UPDATE classes
SET name = :name
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
SELECT * from classes
WHERE school_id = :school_id

-- :name get-class :? :1
-- :doc retrieve class record
SELECT * from classes
WHERE id = :id

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
(user_id, school_id)
VALUES (:user_id, :school_id) RETURNING id

-- :name get-teacher-by-user :? :1
-- :doc retrieve teacher by user id
SELECT * from teachers
WHERE user_id = :user_id

-- :name get-teacher-by-school :? :*
-- :doc retrieve teacher by user id
SELECT * from teachers
WHERE school_id = :school_id

-- :name get-first-school :? :1
-- :doc retrieve first school record
SELECT * from schools LIMIT 1

-- :name access-code-exists? :? :1
-- :doc retrieve first school record
SELECT true as result from students WHERE school_id = :school_id AND access_code = :access_code

-- :name create-new-school! :<!
-- :doc creates a new school record
INSERT INTO schools
(name)
VALUES (:name) RETURNING id

-- :name get-school :? :1
-- :doc retrieve school record
SELECT * from schools
WHERE id = :id

-- :name get-schools :? :*
-- :doc retrieve school records
SELECT * from schools

-- :name update-school! :! :n
-- :doc updates an existing school record
UPDATE schools
SET name = :name
WHERE id = :id

-- :name delete-school! :! :n
-- :doc deletes school
DELETE from schools
WHERE id = :id