(ns webchange.test.progress.class-profile
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.test.fixtures.progress :as fp]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [java-time :as jt]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest course-stat-created-on-start-course
  (let [{course-name :name course-id :id} (f/course-created)
        {user-id :user-id class-id :class-id} (f/student-created)
        data {:actions [{:created-at (jt/format (jt/local-date-time)) :type "course-started"}] :progress {:test "test"}}
        _ (fp/save-current-progress! user-id course-name data)
        retrieved (-> (fp/get-class-profile class-id course-id) :body (json/read-str :key-fn keyword) :stats)]
    (is (= 1 (count retrieved)))))