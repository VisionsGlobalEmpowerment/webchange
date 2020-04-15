(ns webchange.test.secondary.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.handler :as handler]
            [webchange.db.core :refer [*db*] :as db]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [webchange.secondary.core :as core]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest can-import-school-and-user
  (let [user (f/teacher-user-created)
        user-old (db/find-user-by-email {:email (:email user)})
        school-old (db/get-school {:id f/default-school-id})
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data (json/read-str (:body dump) :key-fn keyword))
    (let [user-new (db/find-user-by-email {:email (:email user)})
          school-new (db/get-school {:id f/default-school-id})]
      (assert (not (nil? user-new)))
      (assert (not (nil? school-new)))
      (assert (= user-old user-new))
      (assert (= school-old school-new)))))

(deftest can-import-teacher
  (let [user (f/teacher-user-created)
        _ (f/teacher-created {:user-id (:id user)})
        teacher-old (db/get-teacher-by-user {:user_id (:id user)})
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data (json/read-str (:body dump) :key-fn keyword))
    (let [teacher-new (db/get-teacher-by-user {:user_id (:id user)})]
      (assert (not (nil? teacher-old)))
      (assert (= teacher-old teacher-new)))))

(deftest can-import-student-and-class
  (let [student (f/student-created)
        student-old (db/get-student-by-user {:user_id (:user-id student)})
        class-old (db/get-class {:id (:class-id student)})
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data (json/read-str (:body dump) :key-fn keyword))
    (let [student-new (db/get-student-by-user {:user_id (:user-id student)})
          class-new (db/get-class {:id (:class-id student)})]
      (assert (= student-old student-new))
      (assert (= class-old class-new))
      (assert (not (nil? student-old)))
      (assert (not (nil? class-old)))
      )))

(deftest can-import-course-and-version
  (let [course (f/course-created)
        course-old (db/get-course {:slug (:slug course)})
        course-version-old (db/get-course-version {:id (:version-id course)})
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data (json/read-str (:body dump) :key-fn keyword))
    (let [course-new (db/get-course {:slug (:slug course)})
          course-version-new (db/get-course-version {:id (:version-id course)})]
      (assert (= course-old course-new))
      (assert (= course-version-old course-version-new))
      (assert (not (nil? course-old)))
      (assert (not (nil? course-version-old)))
      )))

(deftest can-import-course-stats
  (let [course-stat (f/course-stat-created)
        course-stats-old (db/get-user-course-stat {:user_id (:user-id course-stat)
                                                   :course_id (:course-id course-stat)})
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data (json/read-str (:body dump) :key-fn keyword))
    (let [course-stats-new (db/get-user-course-stat {:user_id (:user-id course-stat)
                                                     :course_id (:course-id course-stat)})]
      (assert (not (nil? course-stats-old)))
      (assert (= course-stats-old course-stats-new))
      )))

(deftest can-import-course-progresses
  (let [course-progresses (f/course-progresses-created)
        course-progresses-old (db/get-progress {:user_id (:user-id course-progresses)
                                                   :course_id (:course-id course-progresses)})
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data (json/read-str (:body dump) :key-fn keyword))
    (let [course-progresses-new (db/get-progress {:user_id (:user-id course-progresses)
                                                     :course_id (:course-id course-progresses)})]
      (assert (not (nil? course-progresses-old)))
      (assert (= course-progresses-old course-progresses-new))
      )))

(deftest can-import-course-events
  (let [course-events (f/course-events-created)
        course-events-old (db/find-course-events-by-id {:id (:id course-events)})
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data (json/read-str (:body dump) :key-fn keyword))
    (let [course-events-new (db/find-course-events-by-id {:id (:id course-events)})]
      (assert (not (nil? course-events-old)))
      (assert (= course-events-old course-events-new))
      )))

(deftest can-import-dataset
  (let [dataset (f/datasets-created)
        dataset-old (db/get-dataset {:id (:id dataset)})
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data (json/read-str (:body dump) :key-fn keyword))
    (let [dataset-new (db/get-dataset {:id (:id dataset)})]
      (assert (not (nil? dataset-old)))
      (assert (= dataset-old dataset-new))
      )))

(deftest can-import-dataset-item
  (let [dataset-item (f/dataset-item-created)
        dataset-item-old (db/get-dataset-item {:id (:id dataset-item)})
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data (json/read-str (:body dump) :key-fn keyword))
    (let [dataset-item-new (db/get-dataset-item {:id (:id dataset-item)})]
      (assert (not (nil? dataset-item-old)))
      (assert (= dataset-item-old dataset-item-new))
      )))

(deftest can-import-lesson-set
  (let [lesson-set (f/lesson-set-created)
        lesson-set-old (db/get-lesson-set-by-name {:dataset_id (:dataset-id lesson-set) :name (:name lesson-set)})
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data (json/read-str (:body dump) :key-fn keyword))
    (let [lesson-set-new (db/get-lesson-set-by-name  {:dataset_id (:dataset-id lesson-set) :name (:name lesson-set)})]
      (assert (not (nil? lesson-set-old)))
      (assert (= lesson-set-old lesson-set-new))
      )))

(deftest can-import-scenes-and-version
  (let [scene-data (f/scene-created)
        scene-old (db/get-scene {:course_id (:course-id scene-data) :name (:name scene-data)})
        scene-version-old (db/get-scene-version {:id (:version-id scene-data)})
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data (json/read-str (:body dump) :key-fn keyword))
    (let [scene-new (db/get-scene {:course_id (:course-id scene-data) :name (:name scene-data)})
          scene-version-new (db/get-scene-version {:id (:version-id scene-data)})]
      (assert (not (nil? scene-old)))
      (assert (not (nil? scene-version-old)))
      (assert (= scene-old scene-new))
      (assert (= scene-version-old scene-version-new))
      )))

(deftest can-import-activity-stats
  (let [activity-stat (f/activity-stat-created)
        activity-stat-old (db/get-activity-stat  activity-stat)
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data (json/read-str (:body dump) :key-fn keyword))
    (let [activity-stat-new (db/get-activity-stat  activity-stat)]
      (assert (not (nil? activity-stat-old)))
      (assert (= activity-stat-old activity-stat-new))
      )))
