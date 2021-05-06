ALTER TABLE ONLY dataset_items ADD COLUMN version INT default 1;
ALTER TABLE ONLY datasets ADD COLUMN version INT default 1;
