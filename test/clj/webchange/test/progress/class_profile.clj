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

(def course-started {:created-at (jt/format (jt/offset-date-time)) :type "course-started"})
(def activity-started {:created-at (jt/format (jt/offset-date-time)) :type "activity-started" :activity-id 3 :activity-name "volleyball" :lesson 1})
(def activity-finished {:created-at (jt/format (jt/offset-date-time)) :type "activity-finished" :activity-id 3 :activity-name "volleyball"
                        :lesson 1 :score {:correct 10 :mistake 5 :incorrect 2} :activity-number 5 :time-spent 100})

(defn progress-with-event [event] {:events [event] :progress progress})

(deftest course-stat-created-on-start-course
  (let [{course-name :name} (f/course-created)
        {user-id :user-id class-id :class-id} (f/student-created)
        data (progress-with-event course-started)
        _ (fp/save-current-progress! user-id course-name data)
        retrieved (-> (fp/get-class-profile class-id course-name) :body (json/read-str :key-fn keyword) :stats)]
    (is (= 1 (count retrieved)))))

(deftest last-login-changed-on-start-course
  (let [{:keys [class-id course-name user-id]} (fp/course-stat-created)
        date (-> (jt/offset-date-time) (jt/plus (jt/days 1)) jt/format)
        data (-> course-started
                 (assoc :created-at date)
                 progress-with-event)
        _ (fp/save-current-progress! user-id course-name data)
        retrieved (-> (fp/get-class-profile class-id course-name) :body (json/read-str :key-fn keyword) :stats first)]
    (is (= date (-> retrieved :data :last-login)))))

(deftest latest-activity-changed-on-start-activity
  (let [{:keys [class-id course-name user-id]} (fp/course-stat-created)
        data (progress-with-event activity-started)
        _ (fp/save-current-progress! user-id course-name data)
        retrieved (-> (fp/get-class-profile class-id course-name) :body (json/read-str :key-fn keyword) :stats first)]
    (is (= {:id "volleyball" :lesson 1} (-> retrieved :data :latest-activity)))))

(deftest cumulative-score-not-summed-for-same-activity-on-finish-activity
  (let [{:keys [class-id course-name user-id]} (fp/course-stat-created)
        data (progress-with-event activity-finished)
        _ (fp/save-current-progress! user-id course-name data)
        _ (fp/save-current-progress! user-id course-name data)
        retrieved (-> (fp/get-class-profile class-id course-name) :body (json/read-str :key-fn keyword) :stats first)]
    (is (= {:correct 10, :mistake 5, :incorrect 2} (-> retrieved :data :cumulative-score)))))

(deftest cumulative-score-summed-for-different-activities-on-finish-activity
  (let [{:keys [class-id course-name user-id]} (fp/course-stat-created)
        _ (fp/save-current-progress! user-id course-name (progress-with-event activity-finished))
        _ (fp/save-current-progress! user-id course-name (-> activity-finished (assoc :activity-id 5) progress-with-event))
        retrieved (-> (fp/get-class-profile class-id course-name) :body (json/read-str :key-fn keyword) :stats first)]
    (is (= {:correct 20 :incorrect 4 :mistake 10} (-> retrieved :data :cumulative-score)))))

(deftest activity-progress-increased-on-finish-activity
  (let [{:keys [class-id course-name user-id]} (fp/course-stat-created)
        data (progress-with-event activity-finished)
        _ (fp/save-current-progress! user-id course-name data)
        retrieved (-> (fp/get-class-profile class-id course-name) :body (json/read-str :key-fn keyword) :stats first)]
    (is (= (:activity-number activity-finished) (-> retrieved :data :activity-progress)))))

(deftest cumulative-time-increased-on-finish-activity
  (let [{:keys [class-id course-name user-id]} (fp/course-stat-created)
        data (progress-with-event activity-finished)
        _ (fp/save-current-progress! user-id course-name data)
        retrieved (-> (fp/get-class-profile class-id course-name) :body (json/read-str :key-fn keyword) :stats first)]
    (is (= (:time-spent activity-finished) (-> retrieved :data :cumulative-time)))))