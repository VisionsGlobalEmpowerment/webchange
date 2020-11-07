CREATE TABLE character_skins
(id SERIAL,
 name VARCHAR(1024),
 data json NOT NULL);
 --;;
create type editor_asset_type as enum('background', 'surface', 'details');
--;;
CREATE TABLE editor_tags
(id SERIAL,
 name VARCHAR(1024));
--;;
CREATE TABLE editor_assets
(id SERIAL,
 path VARCHAR(1024),
 type editor_asset_type
);
--;;
CREATE TABLE editor_assets_tags
(editor_tag_id int,
editor_asset_id int);
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
