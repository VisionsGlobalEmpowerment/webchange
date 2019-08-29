DROP INDEX datasets_name;
--;;
CREATE UNIQUE INDEX datasets_name ON datasets (course_id, name);
--;;
INSERT INTO courses(name) VALUES ('english');
--;;

INSERT INTO datasets(name, scheme, course_id)
VALUES ('concepts', '{}',
(SELECT id
FROM courses
WHERE name='english'));
--;;