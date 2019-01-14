CREATE TABLE users
(id SERIAL,
 first_name VARCHAR(30),
 last_name VARCHAR(30),
 email VARCHAR(30),
 password VARCHAR(300),
 active boolean DEFAULT false NOT NULL,
 created_at timestamp with time zone NOT NULL,
 last_login timestamp with time zone NOT NULL);
--;;
ALTER TABLE ONLY users
    ADD CONSTRAINT users_email_unique UNIQUE (email);
--;;
ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
