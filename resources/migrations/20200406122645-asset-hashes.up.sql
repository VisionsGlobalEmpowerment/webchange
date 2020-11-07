CREATE TABLE asset_hashes
(id SERIAL,
 path_hash CHAR(32),
 path VARCHAR(1024),
 file_hash CHAR(8)
);
--;;
CREATE UNIQUE INDEX asset_hashes_idx ON asset_hashes (path_hash);
--;;