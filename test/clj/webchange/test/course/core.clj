(ns webchange.test.course.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [webchange.course.core :as course]
            [luminus-migrations.core :as migrations]
            [clojure.test :refer :all]
            [clojure.java.jdbc :as jdbc]
            [config.core :refer [env]]
            [mount.core :as mount]
            [java-time :as jt]))

(use-fixtures
  :once
  (fn [f]
      (mount/start
        #'webchange.db.core/*db*)
      (migrations/migrate ["migrate"] (select-keys env [:database-url]))
      (f)))

(use-fixtures
  :each
  (fn [f]
    (db/clear-table :scene_versions)
    (db/clear-table :scenes)
    (db/clear-table :course_versions)
    (db/clear-table :courses)
    (f)))

(deftest course-can-be-retrieved
    (let [[{course-id :id}] (db/create-course! {:name "test-course"})]
      (is (= 1 (db/save-course! {:course_id course-id :data {:test "test"} :owner_id 0 :created_at (jt/local-date-time)})))
      (is (= {:test "test"} (course/get-course-data "test-course")))))

(deftest scene-can-be-retrieved
  (let [[{course-id :id}] (db/create-course! {:name "test-course"})
        [{scene-id :id}] (db/create-scene! {:course_id course-id :name "test-scene"})
        _ (db/save-scene! {:scene_id scene-id :data {:test "test scene data"} :owner_id 0 :created_at (jt/local-date-time)})]
    (is (= {:test "test scene data"} (course/get-scene-data "test-course" "test-scene")))))