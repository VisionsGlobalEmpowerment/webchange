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

(def progress  {:test "test"})
(def scene-id 1)

(defn activity-stopped [] {:id            (.toString (java.util.UUID/randomUUID))
                           :created-at    (jt/format (jt/offset-date-time)) :type "activity-stopped"
                           :unique-id 12
                           :scene-id scene-id
                           :activity-name "volleyball" :activity 1 :lesson 1 :level 1 :time-spent 50})
(defn activity-finished [] {:id            (.toString (java.util.UUID/randomUUID))
                            :created-at    (jt/format (jt/offset-date-time)) :type "activity-finished"
                            :unique-id 12
                            :scene-id scene-id
                            :activity-name "volleyball" :activity 1 :lesson 1 :level 1
                            :score         {:correct 10 :mistake 4 :incorrect 2} :time-spent 100})

(defn stats-for
  [stats event]
  (->> stats
       (filter #(= (:unique-id event) (:unique-id %)))
       first))

(defn progress-with-event [event] {:school-id f/default-school-id
                                   :scene-id 1
                                   :events [event]
                                   :progress progress})

(deftest activity-stat-created-on-finish-activity
  (let [{:keys [course-slug user-id student-id]} (fp/course-stat-created)
        activity (activity-finished)
        data (progress-with-event activity)
        _ (fp/save-current-progress! user-id course-slug data)
        retrieved (-> (fp/get-class-student-progress student-id) :body slurp (json/read-str :key-fn keyword) :activity-stats (stats-for activity))]
    (is (= (:score activity) (-> retrieved :data :score)))))

(deftest activity-stat-updated-on-finish-activity
  (let [{:keys [course-slug user-id student-id]} (fp/course-stat-created)
        activity (activity-finished)
        _ (fp/save-current-progress! user-id course-slug (progress-with-event activity))
        updated-score {:correct 10 :incorrect 0 :mistake 0}
        _ (fp/save-current-progress! user-id course-slug (-> (activity-finished) (assoc :score updated-score) progress-with-event))
        retrieved (-> (fp/get-class-student-progress student-id) :body slurp (json/read-str :key-fn keyword) :activity-stats (stats-for activity))]
    (is (= updated-score (-> retrieved :data :score)))))

(deftest time-spent-increased-on-activity-stopped
  (let [{:keys [user-id student-id course-slug]} (fp/activity-stat-created (select-keys (activity-stopped) [:activity :lesson :level]))
        _ (fp/save-current-progress! user-id course-slug (progress-with-event (activity-stopped)))
        retrieved (-> (fp/get-class-student-progress student-id) :body slurp (json/read-str :key-fn keyword) :activity-stats (stats-for (activity-stopped)))]
    (is (= (:time-spent (activity-stopped)) (-> retrieved :data :time-spent)))))

(deftest time-spent-increased-on-activity-finished
  (let [{:keys [user-id student-id course-slug]} (fp/activity-stat-created (select-keys (activity-finished) [:activity :lesson :level]))
        _ (fp/save-current-progress! user-id course-slug (progress-with-event (activity-finished)))
        retrieved (-> (fp/get-class-student-progress student-id) :body slurp (json/read-str :key-fn keyword) :activity-stats (stats-for (activity-finished)))]
    (is (= (:time-spent (activity-finished)) (-> retrieved :data :time-spent)))))
