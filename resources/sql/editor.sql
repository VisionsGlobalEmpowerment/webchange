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

-- :name create-editor-tag! :<!
-- :doc creates a new dataset record
INSERT INTO editor_tags (name) VALUES (:name) RETURNING id;

-- :name create-editor-assets! :<!
-- :doc creates a new dataset record
INSERT INTO editor_assets (path, type) VALUES (:path, :type::editor_asset_type) RETURNING id;

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