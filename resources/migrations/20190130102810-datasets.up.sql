CREATE TABLE datasets
(id SERIAL,
 course_id INTEGER references courses(id),
 name VARCHAR(30) NOT NULL,
 scheme JSON NOT NULL);
--;;
ALTER TABLE ONLY datasets
    ADD CONSTRAINT datasets_pkey PRIMARY KEY (id);
--;;

CREATE TABLE dataset_items
(id SERIAL,
 dataset_id INTEGER references datasets(id),
 data JSON NOT NULL);
--;;
ALTER TABLE ONLY dataset_items
    ADD CONSTRAINT dataset_items_pkey PRIMARY KEY (id);
--;;

CREATE TABLE lesson_sets
(id SERIAL,
 name VARCHAR(30) NOT NULL,
 dataset_id INTEGER references datasets(id),
 item_id INTEGER references dataset_items(id),
 item_order INTEGER NOT NULL);
--;;
ALTER TABLE ONLY lesson_sets
    ADD CONSTRAINT lesson_sets_pkey PRIMARY KEY (id);