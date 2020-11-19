(ns webchange.test.progress.handler
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [webchange.test.fixtures.core :as f]
            [webchange.test.fixtures.progress :as fp]
            [webchange.handler :as handler]
            [mount.core :as mount]
            [clojure.data.json :as json]
            [java-time :as jt]
            [webchange.progress.tags :as tags]
            [clojure.tools.logging :as log]))

(use-fixtures :once f/init)
(use-fixtures :each f/clear-db-fixture f/with-default-school)

(deftest current-progress-can-be-retrieved
  (let [{user-id :user-id course-slug :course-slug data :data} (fp/progress-created)
        retrieved (-> (fp/get-current-progress user-id course-slug) :body slurp (json/read-str :key-fn keyword) :progress
                      (dissoc :current-tags)
                      )]
    (is (= data retrieved))))

(deftest progress-can-be-created
  (let [{user-id :user-id} (f/student-created)
        {course-slug :slug} (f/course-created)
        data {:actions [] :progress {:test "test"}}
        _ (fp/save-current-progress! user-id course-slug data)
        retrieved (-> (fp/get-current-progress user-id course-slug) :body slurp (json/read-str :key-fn keyword) :progress)]
    (is (= (get-in data [:progress :test]) (:test retrieved)))))

(deftest check-default-tags
  (let [{user-id :user-id} (f/student-created)
        {course-slug :slug} (f/course-created)
        data {:actions [] :progress {:test "test"}}
        _ (fp/save-current-progress! user-id course-slug data)
        current-tags (-> (fp/get-current-progress user-id course-slug) :body slurp (json/read-str :key-fn keyword) :progress :current-tags)]
    (is (= [tags/age-less-4 tags/advanced] current-tags))))

(deftest check-default-tags-above-4
  (let [{user-id :user-id} (f/student-created {:date-of-birth (jt/local-date 2010 10)})
        {course-slug :slug} (f/course-created)
        data {:actions [] :progress {:test "test"}}
        _ (fp/save-current-progress! user-id course-slug data)
        current-tags (-> (fp/get-current-progress user-id course-slug) :body slurp (json/read-str :key-fn keyword) :progress :current-tags)]
    (is (= [tags/age-above-or-equal-4 tags/advanced] current-tags))))

(deftest check-default-tags-not-override-learning-tag
  (let [{user-id :user-id} (f/student-created)
        {course-slug :slug} (f/course-created)
        data {:actions [] :progress {:test "check-default-tags-not-override-learning-tag" :current-tags [tags/beginner]}}
        _ (fp/save-current-progress! user-id course-slug data)
        current-tags (-> (fp/get-current-progress user-id course-slug) :body slurp (json/read-str :key-fn keyword) :progress :current-tags)]
    (is (= [tags/beginner tags/age-less-4] current-tags))))

(deftest progress-can-be-updated
  (let [{user-id :user-id course-slug :course-slug} (fp/progress-created)
        updated-data {:actions [] :progress {:test "updated-test"}}
        _ (fp/save-current-progress! user-id course-slug updated-data)
        retrieved (-> (fp/get-current-progress user-id course-slug) :body slurp (json/read-str :key-fn keyword) :progress
                      (dissoc :current-tags))]
    (is (= (:progress updated-data) retrieved))))

(deftest class-profile-can-be-retrieved
  (let [{class-id :class-id course-slug :course-slug} (fp/course-stat-created)
        retrieved (-> (fp/get-class-profile class-id course-slug) :body slurp (json/read-str :key-fn keyword) :stats)]
    (is (= 1 (count retrieved)))))

(deftest individual-profile-can-be-retrieved
  (let [{student-id :student-id course-slug :course-slug data :data} (fp/activity-stat-created)
        retrieved (-> (fp/get-individual-profile student-id course-slug) :body slurp (json/read-str :key-fn keyword) :stats)]
    (is (= data (-> retrieved first :data)))))
