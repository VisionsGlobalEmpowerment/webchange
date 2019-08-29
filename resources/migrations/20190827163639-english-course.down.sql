DELETE FROM datasets WHERE course_id = (SELECT id
                                       FROM courses
                                       WHERE name='english');
--;;
DELETE FROM courses WHERE name = 'english';
--;;
DROP INDEX datasets_name;
--;;
CREATE UNIQUE INDEX datasets_name ON datasets (name);
--;;