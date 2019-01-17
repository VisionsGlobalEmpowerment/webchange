(ns webchange.test.course.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [webchange.course.core :as course]
            [webchange.test.fixtures.core :as f]
            [clojure.test :refer :all]
            [config.core :refer [env]]
            [java-time :as jt]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture)

(deftest course-can-be-retrieved
    (let [[{course-id :id}] (db/create-course! {:name "test-course"})]
      (is (= 1 (db/save-course! {:course_id course-id :data {:test "test"} :owner_id 0 :created_at (jt/local-date-time)})))
      (is (= {:test "test"} (course/get-course-data "test-course")))))

(deftest scene-can-be-retrieved
  (let [[{course-id :id}] (db/create-course! {:name "test-course"})
        [{scene-id :id}] (db/create-scene! {:course_id course-id :name "test-scene"})
        _ (db/save-scene! {:scene_id scene-id :data {:test "test scene data"} :owner_id 0 :created_at (jt/local-date-time)})]
    (is (= {:test "test scene data"} (course/get-scene-data "test-course" "test-scene")))))