ALTER TABLE editor_assets DROP COLUMN thumbnail_path;
--;;
ALTER TABLE editor_tags DROP CONSTRAINT editor_tags_name_unique;
--;;
ALTER TABLE editor_assets DROP CONSTRAINT editor_assets_path_unique;
--;;