-- :name create-class! :<!
-- :doc creates a new class record
INSERT INTO classes
(name)
VALUES (:name) RETURNING id

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
(class_id, user_id)
VALUES (:class_id, :user_id) RETURNING id

-- :name update-student! :! :n
-- :doc updates an existing student record
UPDATE students
SET class_id = :class_id
WHERE id = :id

-- :name delete-student! :! :n
-- :doc deletes student
DELETE from students
WHERE id = :id

-- :name get-classes :? :*
-- :doc retrieve all classes
SELECT * from classes

-- :name get-class :? :1
-- :doc retrieve class record
SELECT * from classes
WHERE id = :id

-- :name get-students-by-class :? :*
-- :doc retrieve students given class id
SELECT * from students
WHERE class_id = :class_id

-- :name get-student :? :1
-- :doc retrieve students by id
SELECT * from students
WHERE id = :id

-- :name get-user-class :? :1
-- :doc retrieve student by user id
SELECT * from students
WHERE user_id = :user_id