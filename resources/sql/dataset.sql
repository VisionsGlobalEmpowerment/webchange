-- :name create-dataset! :<!
-- :doc creates a new dataset record
INSERT INTO datasets (course_id, name, scheme) VALUES (:course_id, :name, :scheme) RETURNING id

-- :name update-dataset! :! :n
-- :doc update an existing dataset record
UPDATE datasets
SET scheme = :scheme, version = version + 1
WHERE id = :id

-- :name update-dataset-with-version! :! :n
-- :doc update an existing dataset record with version check
UPDATE datasets
SET scheme = :scheme, version = version + 1
WHERE id = :id AND version = :version

-- :name get-dataset :? :1
-- :doc retrieve dataset by id
SELECT * from datasets
WHERE id = :id;

-- :name get-datasets :? :*
-- :doc retrieve dataset by id
SELECT * from datasets;

-- :name get-datasets-by-course :? :*
-- :doc retrieve datasets given course id
SELECT * from datasets
WHERE course_id = :course_id;

-- :name create-dataset-item! :<!
-- :doc creates a new dataset item record
INSERT INTO dataset_items (dataset_id, name, data) VALUES (:dataset_id, :name, :data) RETURNING id

-- :name create-dataset-item-with-id! :! :n
-- :doc creates a new dataset item record
INSERT INTO dataset_items (id, dataset_id, name, data) VALUES (:id, :dataset_id, :name, :data)

-- :name get-dataset-item :? :1
-- :doc retrieve item given id
SELECT * from dataset_items
WHERE id = :id;

-- :name get-dataset-item-by-name :? :1
-- :doc retrieve item given id
SELECT * from dataset_items
WHERE dataset_id = :dataset_id AND name = :name;

-- :name get-dataset-items :? :*
-- :doc retrieve items given dataset id
SELECT * from dataset_items
WHERE dataset_id = :dataset_id
ORDER BY name;


-- :name get-dataset-items-by-school :? :*
-- :doc retrieve items given dataset id
SELECT * from dataset_items;

-- :name update-dataset-item! :! :n
-- :doc updates an existing dataset item record
UPDATE dataset_items
SET data = :data, name = :name, version = version + 1
WHERE id = :id

-- :name update-dataset-item-with-version! :! :n
-- :doc updates an existing dataset item record with version check
UPDATE dataset_items
SET data = :data, name = :name, version = version + 1
WHERE id = :id AND version = :version

-- :name delete-dataset-item! :! :n
-- :doc deletes a dataset item record given the id
DELETE FROM dataset_items
WHERE id = :id

-- :name create-lesson-set! :<!
-- :doc creates a new lesson set item record
INSERT INTO lesson_sets
(name, dataset_id, data)
VALUES (:name, :dataset_id, :data) RETURNING id

-- :name get-lesson-set-by-name :? :1
-- :doc retrieve lesson set given name
SELECT * from lesson_sets
WHERE dataset_id = :dataset_id AND name = :name;

-- :name get-lesson-sets :? :*
-- :doc retrieve lesson set given name
SELECT * from lesson_sets;

-- :name get-dataset-lessons :? :*
-- :doc retrieve lesson sets given dataset id
SELECT * from lesson_sets
WHERE dataset_id = :dataset_id
ORDER BY name;

-- :name update-lesson-set! :! :n
-- :doc updates an existing lesson set record
UPDATE lesson_sets
SET data = :data
WHERE id = :id

-- :name delete-lesson-set! :! :n
-- :doc deletes lesson set
DELETE FROM lesson_sets
WHERE id = :id

-- :name get-course-items :? :*
-- :doc retrieve dataset items given course id
SELECT di.* FROM dataset_items di INNER JOIN datasets d ON di.dataset_id = d.id
WHERE d.course_id = :course_id;

-- :name get-course-lessons :? :*
-- :doc retrieve lesson sets given course id
SELECT ls.* FROM lesson_sets ls INNER JOIN datasets d ON ls.dataset_id = d.id
WHERE d.course_id = :course_id;
