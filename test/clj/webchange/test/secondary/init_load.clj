(ns webchange.test.secondary.init-load
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
    (core/process-data dump)
    (let [user-new (db/find-user-by-email {:email (:email user)})
          school-new (db/get-school {:id f/default-school-id})]
      (assert (not (nil? user-new)))
      (assert (not (nil? school-new)))
      (assert (= user-old user-new))
      (assert (= school-old school-new)))
    ))

(deftest can-import-teacher
  (let [user (f/teacher-user-created)
        _ (f/teacher-created {:user-id (:id user)})
        teacher-old (dissoc (db/get-teacher-by-user {:user_id (:id user)}) :id)
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data dump)
    (let [teacher-new (dissoc (db/get-teacher-by-user {:user_id (:id user)}) :id)]
      (assert (not (nil? teacher-old)))
      (assert (= teacher-old teacher-new)))))

(deftest can-import-student-and-class
  (let [student (f/student-created)
        student-old (dissoc (db/get-student-by-user {:user_id (:user-id student)}) :id)
        class-old (dissoc (db/get-class {:id (:class-id student)}) :id)
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data dump)
    (let [student-new (dissoc (db/get-student-by-user {:user_id (:user-id student)}) :id)
          class-new (dissoc (db/get-class {:id (:class-id student)}) :id)]
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
    (core/process-data dump)
    (let [course-new (db/get-course {:slug (:slug course)})
          course-version-new (db/get-latest-course-version {:course_id (:id course)})]
      (assert (= course-old course-new))
      (assert (= (dissoc course-version-old :id) (dissoc course-version-new :id)))
      (assert (not (nil? course-old)))
      (assert (not (nil? course-version-old)))
      )))

(deftest can-import-course-stats
  (let [course (f/course-created)
        course-stat (f/course-stat-created course)
        course-stats-old (dissoc (db/get-user-course-stat {:user_id (:user-id course-stat)
                                                   :course_id (:course-id course-stat)}) :id)
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data dump)
    (let [course-stats-new (dissoc (db/get-user-course-stat {:user_id (:user-id course-stat)
                                                     :course_id (:course-id course-stat)}) :id)]
      (assert (not (nil? course-stats-old)))
      (assert (= course-stats-old course-stats-new))
      )))

(deftest can-import-course-progresses
  (let [user (f/student-created)
        course (f/course-created)
        course-progresses (f/course-progresses-created (assoc user :course-id (:id course)))
        course-progresses-old (dissoc (db/get-progress {:user_id (:user-id course-progresses)
                                                   :course_id (:course-id course-progresses)}) :id)
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data dump)
    (let [course-progresses-new (dissoc (db/get-progress {:user_id (:user-id course-progresses)
                                                     :course_id (:course-id course-progresses)}) :id)]
      (assert (not (nil? course-progresses-old)))
      (assert (= course-progresses-old course-progresses-new))
      )))

(deftest can-import-course-events
  (let [user (f/student-created)
        course-events (f/course-events-created user)
        course-events-old (map #(dissoc % :id) (db/get-course-events-by-school {:school_id f/default-school-id}))
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data dump)
    (let [course-events-new (map #(dissoc % :id) (db/get-course-events-by-school {:school_id f/default-school-id}))]
      (assert (not (nil? course-events-old)))
      (assert (= course-events-old course-events-new))
      )))

(deftest can-import-dataset
  (let [dataset (f/datasets-created)
        dataset-old (map #(dissoc % :id) (db/get-datasets-by-course  {:course_id (:course-id dataset)}))
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data dump)
    (let [dataset-new (map #(dissoc % :id) (db/get-datasets-by-course {:course_id (:course-id dataset)}))]
      (assert (not (nil? dataset-old)))
      (assert (= dataset-old dataset-new))
      )))

(deftest can-import-dataset-item
  (let [dataset-item (f/dataset-item-created)
        dataset-item-old (db/get-dataset-item {:id (:id dataset-item)})
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data dump)
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
    (core/process-data dump)
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
    (core/process-data dump)
    (let [scene-new (db/get-scene {:course_id (:course-id scene-data) :name (:name scene-data)})
          scene-version-new (db/get-latest-scene-version {:scene_id (:id scene-data)})]
      (assert (not (nil? scene-old)))
      (assert (not (nil? scene-version-old)))
      (assert (= scene-old scene-new))
      (assert (= (dissoc scene-version-old :id) (dissoc  scene-version-new :id)))
      )))

(deftest can-import-activity-stats
  (let [user (f/student-created)
        activity-stat (f/activity-stat-created {:id (:user-id user)})
        activity-stat-old (dissoc (db/get-activity-stat  activity-stat) :id)
        dump (f/get-school-dump f/default-school-id)]
    (f/clear-db-fixture #())
    (f/with-default-school #())
    (core/process-data dump)
    (let [activity-stat-new (dissoc (db/get-activity-stat  activity-stat) :id)]
      (assert (not (nil? activity-stat-old)))
      (assert (= activity-stat-old activity-stat-new))
      )))
