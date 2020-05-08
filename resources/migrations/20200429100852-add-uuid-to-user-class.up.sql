CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
--;;
ALTER TABLE ONLY users
    ADD COLUMN guid uuid NOT NULL DEFAULT uuid_generate_v1mc();
--;;
ALTER TABLE ONLY classes
    ADD COLUMN guid uuid NOT NULL DEFAULT uuid_generate_v1mc();
--;;
ALTER TABLE users
ADD CONSTRAINT user_unique UNIQUE (guid);
--;;
ALTER TABLE classes
ADD CONSTRAINT class_unique UNIQUE (guid);
--;;
