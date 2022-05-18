(ns webchange.test.fixtures.progress
  (:require [clojure.test :refer :all]
            [webchange.db.core :refer [*db*] :as db]
            [webchange.auth.core :as auth]
            [webchange.handler :as handler]
            [config.core :refer [env]]
            [mount.core :as mount]
            [luminus-migrations.core :as migrations]
            [ring.middleware.session.store :as store]
            [java-time :as jt]
            [ring.mock.request :as mock]
            [clojure.data.json :as json]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [camel-snake-kebab.core :refer [->snake_case_keyword ->kebab-case-keyword]]
            [webchange.test.fixtures.core :as f]
            [clojure.tools.logging :as log]))

(defn progress-created
  ([] (progress-created {}))
  ([{:keys [user-id] :as options}]
   (let [{user-id :user-id} (if user-id {:user-id user-id} (f/student-created))
         {course-slug :slug course-id :id} (f/course-created)
         defaults {:user-id user-id :course-id course-id :data {:test "test"}}
         data (->> options
                   (merge defaults)
                   (transform-keys ->snake_case_keyword))
         [{id :id}] (db/create-progress! data)
         prepared-data (-> data
                           (assoc :id id)
                           (assoc :course-slug course-slug))]
     (transform-keys ->kebab-case-keyword prepared-data))))

(defn course-stat-created
  ([] (course-stat-created {}))
  ([options]
   (let [{student-id :id user-id :user-id class-id :class-id} (f/student-created)
         {course-id :id course-slug :slug} (f/course-created)
         defaults {:user-id user-id :class-id class-id :course-id course-id :data {:test "test"}}
         data (->> options
                   (merge defaults)
                   (transform-keys ->snake_case_keyword))
         [{id :id}] (db/create-course-stat! data)]
     (->> (assoc data :id id :course-slug course-slug :student-id student-id)
          (transform-keys ->kebab-case-keyword)))))

(defn activity-stat-created
  ([] (activity-stat-created {}))
  ([options]
   (let [{student-id :id user-id :user-id} (f/student-created)
         {course-id :id course-slug :slug} (f/course-created)
         defaults {:user-id user-id :course-id course-id :unique-id 11 :activity-id "1-1-1-test" :data {:activity-name "test" :activity 1 :lesson 1 :level 1 :test "test"}}
         data (->> options
                   (merge defaults)
                   (transform-keys ->snake_case_keyword))
         [{id :id}] (db/create-activity-stat! data)]
     (->> (assoc data :id id :course-slug course-slug :student-id student-id)
          (transform-keys ->kebab-case-keyword)))))

(defn get-current-progress
  [user-id course-slug]
  (let [url (str "/api/courses/" course-slug "/current-progress")
        request (-> (mock/request :get url)
                    (f/student-logged-in user-id))]
    (handler/dev-handler request)))

(defn save-current-progress!
  [user-id course-slug data]
  (let [url (str "/api/courses/" course-slug "/current-progress")
        request (-> (mock/request :post url (json/write-str data))
                    (mock/header :content-type "application/json")
                    (f/student-logged-in user-id))]
    (-> (handler/dev-handler request)
        :body
        slurp
        (json/read-str :key-fn keyword))))

(defn get-class-profile
  [class-id course-slug]
  (let [url (str "/api/class-profile/" class-id "/course/" course-slug)
        request (-> (mock/request :get url)
                    f/teacher-logged-in)]
    (handler/dev-handler request)))

(defn get-individual-profile
  [user-id course-id]
  (let [url (str "/api/individual-profile/" user-id "/course/" course-id)
        request (-> (mock/request :get url)
                    f/teacher-logged-in)]
    (handler/dev-handler request)))
