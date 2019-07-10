(ns webchange.test.progress.student-profile
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

(def progress {:test "test"})

(def course-started {:created-at (jt/format (jt/offset-date-time)) :type "course-started"})
(def activity-started {:created-at (jt/format (jt/offset-date-time)) :type "activity-started" :activity-id 3 :activity-name "volleyball" :lesson 1})
(def activity-stopped {:created-at (jt/format (jt/offset-date-time)) :type "activity-stopped" :activity-id 3 :time-spent 50})
(def activity-finished {:created-at (jt/format (jt/offset-date-time)) :type "activity-finished" :activity-id 3 :activity-name "volleyball"
                        :lesson 1 :score {:correct 10 :incorrect 2 :mistake 4} :activity-number 5  :time-spent 100})

(defn progress-with-event [event] {:events [event] :progress progress})

(deftest activity-stat-created-on-finish-activity
  (let [{:keys [course-name user-id student-id]} (fp/course-stat-created)
        data (progress-with-event activity-finished)
        _ (fp/save-current-progress! user-id course-name data)
        retrieved (-> (fp/get-individual-profile student-id course-name) :body (json/read-str :key-fn keyword) :stats (get (-> activity-finished :activity-id str keyword)))]
    (is (= (:score activity-finished) (-> retrieved :data :score)))))

(deftest activity-stat-updated-on-finish-activity
  (let [{:keys [course-name user-id student-id]} (fp/course-stat-created)
        _ (fp/save-current-progress! user-id course-name (progress-with-event activity-finished))
        updated-score {:correct 10 :incorrect 0 :mistake 0}
        _ (fp/save-current-progress! user-id course-name (-> activity-finished (assoc :score updated-score) progress-with-event))
        retrieved (-> (fp/get-individual-profile student-id course-name) :body (json/read-str :key-fn keyword) :stats (get (-> activity-finished :activity-id str keyword)))]
    (is (= updated-score (-> retrieved :data :score)))))

(deftest time-spent-increased-on-activity-stopped
  (let [{:keys [user-id student-id course-name]} (fp/activity-stat-created {:activity-id (:activity-id activity-stopped)})
        _ (fp/save-current-progress! user-id course-name (progress-with-event activity-stopped))
        retrieved (-> (fp/get-individual-profile student-id course-name) :body (json/read-str :key-fn keyword) :stats (get (-> activity-finished :activity-id str keyword)))]
    (is (= (:time-spent activity-stopped) (-> retrieved :data :time-spent)))))

(deftest time-spent-increased-on-activity-finished
  (let [{:keys [user-id student-id course-name]} (fp/activity-stat-created {:activity-id (:activity-id activity-finished)})
        _ (fp/save-current-progress! user-id course-name (progress-with-event activity-finished))
        retrieved (-> (fp/get-individual-profile student-id course-name) :body (json/read-str :key-fn keyword) :stats (get (-> activity-finished :activity-id str keyword)))]
    (is (= (:time-spent activity-finished) (-> retrieved :data :time-spent)))))