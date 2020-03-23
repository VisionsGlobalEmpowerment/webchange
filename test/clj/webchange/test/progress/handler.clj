(ns webchange.test.progress.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.test.fixtures.progress :as fp]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest current-progress-can-be-retrieved
  (let [{user-id :user-id course-name :course-name data :data} (fp/progress-created)
        retrieved (-> (fp/get-current-progress user-id course-name) :body (json/read-str :key-fn keyword) :progress)]
    (is (= data retrieved))))

(deftest progress-can-be-created
  (let [{user-id :id} (f/teacher-user-created)
        {course-name :name} (f/course-created)
        data {:actions [] :progress {:test "test"}}
        _ (fp/save-current-progress! user-id course-name data)
        retrieved (-> (fp/get-current-progress user-id course-name) :body (json/read-str :key-fn keyword) :progress)]
    (is (= (:progress data) retrieved))))

(deftest progress-can-be-updated
  (let [{user-id :user-id course-name :course-name} (fp/progress-created)
        updated-data {:actions [] :progress {:test "updated-test"}}
        _ (fp/save-current-progress! user-id course-name updated-data)
        retrieved (-> (fp/get-current-progress user-id course-name) :body (json/read-str :key-fn keyword) :progress)]
    (is (= (:progress updated-data) retrieved))))

(deftest class-profile-can-be-retrieved
  (let [{class-id :class-id course-name :course-name} (fp/course-stat-created)
        retrieved (-> (fp/get-class-profile class-id course-name) :body (json/read-str :key-fn keyword) :stats)]
    (is (= 1 (count retrieved)))))

(deftest individual-profile-can-be-retrieved
  (let [{student-id :student-id course-name :course-name data :data} (fp/activity-stat-created)
        retrieved (-> (fp/get-individual-profile student-id course-name) :body (json/read-str :key-fn keyword) :stats)]
    (is (= data (-> retrieved first :data)))))
