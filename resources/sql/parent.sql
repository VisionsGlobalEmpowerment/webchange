-- :name create-child! :! :<!
-- :doc creates a new parent-child record
INSERT INTO children
(child_id, parent_id, data)
VALUES (:child_id, :parent_id, :data);

-- :name find-users-by-parent :? :*
-- :doc retrieve users by parent
SELECT u.*, c.data FROM users u INNER JOIN children c ON u.id = c.child_id
WHERE c.parent_id = :parent_id;

-- :name is-parent? :? :1
-- :doc check if user is a parent of given child
SELECT true as result from children WHERE child_id = :child_id AND parent_id = :parent_id;

-- :name get-child-by-id :? :1
-- :doc retrieve a scene record given the course id and the name
SELECT * from children
WHERE child_id = :child_id
LIMIT 1;

-- :name delete-child! :! :n
-- :doc deletes course stat
DELETE from children
WHERE child_id = :child_id
