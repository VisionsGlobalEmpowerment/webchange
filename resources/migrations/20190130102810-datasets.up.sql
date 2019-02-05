CREATE TABLE datasets
(id SERIAL,
 course_id INTEGER references courses(id),
 name VARCHAR(30) NOT NULL,
 scheme JSON NOT NULL);
--;;
ALTER TABLE ONLY datasets
    ADD CONSTRAINT datasets_pkey PRIMARY KEY (id);
--;;
CREATE UNIQUE INDEX datasets_name ON datasets (name);
--;;

CREATE TABLE dataset_items
(id SERIAL,
 name VARCHAR(30) NOT NULL,
 dataset_id INTEGER references datasets(id),
 data JSON NOT NULL);
--;;
ALTER TABLE ONLY dataset_items
    ADD CONSTRAINT dataset_items_pkey PRIMARY KEY (id);
--;;
CREATE UNIQUE INDEX dataset_items_name ON dataset_items (dataset_id, name);
--;;

CREATE TABLE lesson_sets
(id SERIAL,
 name VARCHAR(30) NOT NULL,
 dataset_id INTEGER references datasets(id),
 data JSON NOT NULL);
--;;
ALTER TABLE ONLY lesson_sets
    ADD CONSTRAINT lesson_sets_pkey PRIMARY KEY (id);