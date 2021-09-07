ALTER TYPE editor_asset_type RENAME TO editor_asset_type_old;
CREATE TYPE editor_asset_type AS ENUM('single-background', 'background', 'surface', 'decoration');
ALTER TABLE editor_assets ALTER COLUMN type TYPE editor_asset_type;
DROP TYPE editor_asset_type_old;
