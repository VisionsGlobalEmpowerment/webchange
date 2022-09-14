-- :name insert-code! :! :n
-- :doc creates a new user record
INSERT INTO email_codes (code, created_at, metadata)
VALUES (:code, :created_at, :metadata);

-- :name delete-code! :! :n
-- :doc deletes all scene skills
DELETE FROM email_codes
WHERE code = :code;

-- :name get-code :? :1
-- :doc get code by code value
SELECT * from email_codes WHERE code = :code;
