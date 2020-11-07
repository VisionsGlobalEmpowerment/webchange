-- :name find-editor-assets :? :*
-- :doc retrieve class record
SELECT DISTINCT  ea.* from editor_assets ea
LEFT JOIN editor_assets_tags eat ON (eat.editor_asset_id=ea.id)
WHERE true
--~ (when (and (contains? params :tag) (some? (:tag params))) " and eat.editor_tag_id = :tag")
--~ (when (and (contains? params :type) (some? (:type params)))" and type = :type::editor_asset_type")
;

-- :name find-all-tags :? :*
-- :doc retrieve all tags
SELECT * from editor_tags;

-- :name find-editor-tag-by-name :? :1
-- :doc retrieve tag by name
SELECT * from editor_tags where name=:name;

-- :name create-editor-tag! :<!
-- :doc creates a new dataset record
INSERT INTO editor_tags (name) VALUES (:name) RETURNING id;

-- :name create-editor-assets! :<!
-- :doc creates a new dataset record
INSERT INTO editor_assets (path, thumbnail_path, type) VALUES (:path, :thumbnail_path, :type::editor_asset_type) RETURNING id;

-- :name link-editor-asset-tag! :! :<!
-- :doc creates a new dataset record
INSERT INTO editor_assets_tags (editor_tag_id, editor_asset_id) VALUES (:tag_id, :asset_id);

-- :name create-character-skins! :! :<!
-- :doc creates a new dataset record
INSERT INTO character_skins (name, data) VALUES (:name, :data);

-- :name clear-character-skins! :! :<!
-- :doc truncate table
TRUNCATE TABLE character_skins;

-- :name find-character-skins :? :*
-- :doc retrieve character skins
SELECT * from character_skins;

-- :name create-asset-tags! :! :n
-- :doc creates a new user record
INSERT INTO editor_tags (name) VALUES (:name)
ON CONFLICT ON CONSTRAINT editor_tags_name_unique
DO nothing;

-- :name create-or-update-editor-assets! :! :n
-- :doc creates a new user record
INSERT INTO editor_assets (path, thumbnail_path, type)
VALUES (:path, :thumbnail_path, :type::editor_asset_type)
ON CONFLICT ON CONSTRAINT editor_assets_path_unique
DO UPDATE SET thumbnail_path=:thumbnail_path, type=:type::editor_asset_type
WHERE editor_assets.path=:path;

-- :name find-editor-assets-by-path :? :1
-- :doc retrieve asset by path
SELECT * from editor_assets where path=:path;

-- :name create-editor-asset-tag! :! :<!
-- :doc creates a new dataset record
INSERT INTO editor_assets_tags (editor_tag_id, editor_asset_id)
VALUES (:tag_id, :asset_id)
ON CONFLICT ON CONSTRAINT editor_assets_tags_unique
DO nothing;

-- :name truncate-editor-assets-tags! :! :<!
-- :doc truncate table
TRUNCATE TABLE editor_assets_tags;

-- :name truncate-editor-assets! :! :<!
-- :doc truncate table
TRUNCATE TABLE editor_assets cascade ;

-- :name truncate-editor-tags! :! :<!
-- :doc truncate table
TRUNCATE TABLE editor_tags cascade ;
