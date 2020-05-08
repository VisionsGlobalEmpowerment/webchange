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

(def progress {:test "test"})

(defn course-started [] {:id (.toString (java.util.UUID/randomUUID)) :created-at (jt/format (jt/offset-date-time)) :type "course-started"})
(defn activity-started [] {:id (.toString (java.util.UUID/randomUUID))
                           :created-at (jt/format (jt/offset-date-time)) :type "activity-started" :activity "volleyball" :lesson 1 :level 1})
(defn activity-finished [] {:id (.toString (java.util.UUID/randomUUID))
                            :created-at (jt/format (jt/offset-date-time)) :type "activity-finished" :activity "volleyball" :lesson 1 :level 1
                        :score {:correct 10 :mistake 5 :incorrect 2} :time-spent 100})
(defn activity-progress [] {:id (.toString (java.util.UUID/randomUUID))
                            :created-at (jt/format (jt/offset-date-time)) :type "activity-progress" :activity-progress 5})

(defn progress-with-event [event] {:events [event] :progress progress})

(deftest course-stat-created-on-start-course
  (let [{course-slug :slug} (f/course-created)
        {user-id :user-id class-id :class-id} (f/student-created)
        data (progress-with-event (course-started))
        _ (fp/save-current-progress! user-id course-slug data)
        retrieved (-> (fp/get-class-profile class-id course-slug) :body slurp (json/read-str :key-fn keyword) :stats)]
    (is (= 1 (count retrieved)))))

(deftest last-login-changed-on-start-course
  (let [{:keys [class-id course-slug user-id]} (fp/course-stat-created)
        date (-> (jt/offset-date-time) (jt/plus (jt/days 1)) jt/format)
        data (-> (course-started)
                 (assoc :created-at date)
                 progress-with-event)
        _ (fp/save-current-progress! user-id course-slug data)
        retrieved (-> (fp/get-class-profile class-id course-slug) :body slurp (json/read-str :key-fn keyword) :stats first)]
    (is (= date (-> retrieved :data :last-login)))))

(deftest latest-activity-changed-on-start-activity
  (let [{:keys [class-id course-slug user-id]} (fp/course-stat-created)
        data (progress-with-event (activity-started))
        _ (fp/save-current-progress! user-id course-slug data)
        retrieved (-> (fp/get-class-profile class-id course-slug) :body slurp (json/read-str :key-fn keyword) :stats first)]
    (is (= {:id "volleyball" :lesson 1 :level 1} (-> retrieved :data :latest-activity)))))

(deftest cumulative-score-not-summed-for-same-activity-on-finish-activity
  (let [{:keys [class-id course-slug user-id]} (fp/course-stat-created)
        data-1 (progress-with-event (activity-finished))
        data-2 (progress-with-event (activity-finished))
        _ (fp/save-current-progress! user-id course-slug data-1)
        _ (fp/save-current-progress! user-id course-slug data-2)
        retrieved (-> (fp/get-class-profile class-id course-slug) :body slurp (json/read-str :key-fn keyword) :stats first)]
    (is (= {:correct 10, :mistake 5, :incorrect 2} (-> retrieved :data :cumulative-score)))))

(deftest cumulative-score-summed-for-different-activities-on-finish-activity
  (let [{:keys [class-id course-slug user-id]} (fp/course-stat-created)
        _ (fp/save-current-progress! user-id course-slug (progress-with-event (activity-finished)))
        _ (fp/save-current-progress! user-id course-slug (-> (activity-finished) (assoc :lesson 2) progress-with-event))
        retrieved (-> (fp/get-class-profile class-id course-slug) :body slurp (json/read-str :key-fn keyword) :stats first)]
    (is (= {:correct 20 :incorrect 4 :mistake 10} (-> retrieved :data :cumulative-score)))))

(deftest activity-progress-increased-on-activity-progress
  (let [{:keys [class-id course-slug user-id]} (fp/course-stat-created)
        data (progress-with-event (activity-progress))
        _ (fp/save-current-progress! user-id course-slug data)
        retrieved (-> (fp/get-class-profile class-id course-slug) :body slurp (json/read-str :key-fn keyword) :stats first)]
    (is (= (:activity-progress (activity-progress)) (-> retrieved :data :activity-progress)))))

(deftest cumulative-time-increased-on-finish-activity
  (let [{:keys [class-id course-slug user-id]} (fp/course-stat-created)
        data (progress-with-event (activity-finished))
        _ (fp/save-current-progress! user-id course-slug data)
        retrieved (-> (fp/get-class-profile class-id course-slug) :body slurp (json/read-str :key-fn keyword) :stats first)]
    (is (= (:time-spent (activity-finished)) (-> retrieved :data :cumulative-time)))))
