ALTER TABLE ONLY courses
    ADD COLUMN slug VARCHAR(30),
    ADD COLUMN lang VARCHAR(30),
    ADD COLUMN image_src VARCHAR(1024);
--;;
UPDATE courses SET slug = name;
--;;
