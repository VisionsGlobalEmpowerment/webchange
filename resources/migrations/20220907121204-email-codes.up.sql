CREATE TABLE email_codes
(code uuid,
 created_at timestamp with time zone,
 metadata json);
--;;

CREATE UNIQUE INDEX email_codes_idx ON email_codes (code);
--;;
