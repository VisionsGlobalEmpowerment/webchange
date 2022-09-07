-- :name insert-code! :! :n
-- :doc creates a new user record
INSERT INTO email_codes (code, created_at, metadata)
VALUES (:code, :created_at, :metadata);
