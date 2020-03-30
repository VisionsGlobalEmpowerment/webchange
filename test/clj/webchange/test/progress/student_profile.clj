(ns webchange.test.progress.student-profile
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.test.fixtures.progress :as fp]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [java-time :as jt]
            [webchange.progress.student-profile :as sp]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(def progress {:test "test"})

(def course-started {:created-at (jt/format (jt/offset-date-time)) :type "course-started"})
(def activity-started {:created-at (jt/format (jt/offset-date-time)) :type "activity-started" :activity "volleyball" :lesson 1 :level 1})
(def activity-stopped {:created-at (jt/format (jt/offset-date-time)) :type "activity-stopped" :activity "volleyball" :lesson 1 :level 1 :time-spent 50})
(def activity-finished {:created-at (jt/format (jt/offset-date-time)) :type "activity-finished" :activity "volleyball" :lesson 1 :level 1
                        :score {:correct 10 :mistake 4 :incorrect 2} :time-spent 100})
(def activity-progress {:created-at (jt/format (jt/offset-date-time)) :type "activity-progress" :activity-progress 5})


(defn event->activity-id
  [event]
  (sp/activity->id (:level event) (:lesson event) (:activity event)))

(defn stats-for
  [stats event]
  (let [activity-id (event->activity-id event)]
    (->> stats
         (filter #(= activity-id (:activity-id %)))
         first)))

(defn progress-with-event [event] {:events [event] :progress progress})

(deftest activity-stat-created-on-finish-activity
  (let [{:keys [course-slug user-id student-id]} (fp/course-stat-created)
        data (progress-with-event activity-finished)
        _ (fp/save-current-progress! user-id course-slug data)
        retrieved (-> (fp/get-individual-profile student-id course-slug) :body (json/read-str :key-fn keyword) :stats (stats-for activity-finished))]
    (is (= (:score activity-finished) (-> retrieved :data :score)))))

(deftest activity-stat-updated-on-finish-activity
  (let [{:keys [course-slug user-id student-id]} (fp/course-stat-created)
        _ (fp/save-current-progress! user-id course-slug (progress-with-event activity-finished))
        updated-score {:correct 10 :incorrect 0 :mistake 0}
        _ (fp/save-current-progress! user-id course-slug (-> activity-finished (assoc :score updated-score) progress-with-event))
        retrieved (-> (fp/get-individual-profile student-id course-slug) :body (json/read-str :key-fn keyword) :stats (stats-for activity-finished))]
    (is (= updated-score (-> retrieved :data :score)))))

(deftest time-spent-increased-on-activity-stopped
  (let [{:keys [user-id student-id course-slug]} (fp/activity-stat-created (select-keys activity-stopped [:activity :lesson :level]))
        _ (fp/save-current-progress! user-id course-slug (progress-with-event activity-stopped))
        retrieved (-> (fp/get-individual-profile student-id course-slug) :body (json/read-str :key-fn keyword) :stats (stats-for activity-stopped))]
    (is (= (:time-spent activity-stopped) (-> retrieved :data :time-spent)))))

(deftest time-spent-increased-on-activity-finished
  (let [{:keys [user-id student-id course-slug]} (fp/activity-stat-created (select-keys activity-finished [:activity :lesson :level]))
        _ (fp/save-current-progress! user-id course-slug (progress-with-event activity-finished))
        retrieved (-> (fp/get-individual-profile student-id course-slug) :body (json/read-str :key-fn keyword) :stats (stats-for activity-finished))]
    (is (= (:time-spent activity-finished) (-> retrieved :data :time-spent)))))
