-- :name create-asset-hash! :<!
-- :doc creates a new asset hash record
INSERT INTO asset_hashes (path_hash, path, file_hash) VALUES (:path_hash, :path, :file_hash) RETURNING id;

-- :name clear-asset-hash! :! :<!
-- :doc truncate table
TRUNCATE TABLE asset_hashes;

-- :name get-asset-hash :? :1
-- :doc retrieve class record
SELECT * from asset_hashes WHERE path_hash = :path_hash;

-- :name update-asset-hash! :! :n
-- :doc retrieve class record
UPDATE  asset_hashes SET file_hash=:file_hash
WHERE path_hash = :path_hash;

-- :name get-all-asset-hash :? :*
-- :doc retrieve class record
SELECT * from asset_hashes;

-- :name remove-asset-hash! :! :n
-- :doc remove record
DELETE from asset_hashes WHERE path_hash = :path_hash;