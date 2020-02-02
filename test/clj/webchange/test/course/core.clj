(ns webchange.test.course.core
  (:require [webchange.db.core :refer [*db*] :as db]
            [webchange.course.core :as course]
            [webchange.test.fixtures.core :as f]
            [clojure.test :refer :all]
            [config.core :refer [env]]
            [java-time :as jt]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest course-can-be-retrieved
    (let [{course-name :name data :data} (f/course-created)]
      (is (= data (course/get-course-data course-name)))))

(deftest scene-can-be-retrieved
  (let [{course-name :course-name scene-name :name} (f/scene-created)]
    (is (= {:test "test" :test-dash "test-dash-value" :test3 "test-3-value"} (course/get-scene-data course-name scene-name)))))