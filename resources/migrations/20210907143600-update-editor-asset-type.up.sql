DROP TABLE editor_assets_tags;
--;;
DROP TABLE editor_assets;
--;;
DROP type editor_asset_type;
--;;
DROP TABLE editor_tags;
--;;
create type editor_asset_type as enum('single-background', 'background', 'surface', 'decoration', 'etc');
--;;
CREATE TABLE editor_assets
(id SERIAL,
 path VARCHAR(1024),
 thumbnail_path VARCHAR(1024),
 type editor_asset_type
);
--;;
CREATE TABLE editor_tags
(id SERIAL,
 name VARCHAR(1024));
--;;
CREATE TABLE editor_assets_tags
(editor_tag_id int,
editor_asset_id int);
--;;
ALTER TABLE editor_tags
ADD CONSTRAINT editor_tags_name_unique UNIQUE (name);
--;;
ALTER TABLE editor_assets
ADD CONSTRAINT editor_assets_path_unique UNIQUE (path);
--;;
ALTER TABLE ONLY public.editor_assets
    ADD CONSTRAINT editor_assets_id_key UNIQUE (id);
--;;
ALTER TABLE ONLY public.editor_tags
    ADD CONSTRAINT editor_tags_id_key UNIQUE (id);
--;;
ALTER TABLE ONLY public.editor_assets_tags
    ADD CONSTRAINT editor_assets_fkey FOREIGN KEY (editor_asset_id) REFERENCES public.editor_assets(id);
--;;
ALTER TABLE ONLY public.editor_assets_tags
    ADD CONSTRAINT editor_assets_tags_fkey FOREIGN KEY (editor_tag_id) REFERENCES public.editor_tags(id);
--;;
ALTER TABLE ONLY public.editor_assets_tags
    ADD CONSTRAINT editor_assets_tags_unique UNIQUE (editor_asset_id, editor_tag_id);
--;;
