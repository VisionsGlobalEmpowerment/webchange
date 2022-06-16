UPDATE scenes SET status = 'visible';
--;;
UPDATE scenes s set metadata = (select metadata from courses c where c.id = s.course_id) where type = 'book';
--;;
