(ns webchange.test.fixtures.core
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
            [clojure.data.json :as json]))

(defn clear-db []
  (db/clear-table :scene_versions)
  (db/clear-table :scenes)
  (db/clear-table :course_versions)
  (db/clear-table :courses)
  (db/clear-table :users))

(defn clear-db-fixture [f]
  (clear-db)
  (f))

(defn init [f]
  (mount/start #'webchange.db.core/*db*)
  (migrations/migrate ["migrate"] (select-keys env [:database-url]))
  (f))

(defn user-created []
  (if-let [user (db/find-user-by-email {:email "test@example.com"})]
    (assoc user :password "test")
    (let [options {:first-name "Test" :last-name "Test" :email "test@example.com" :password "test"}
          [{user-id :id}] (-> options auth/prepare-register-data auth/create-user!)]
      (auth/activate-user! user-id)
      (assoc options :id user-id))))

(defn user-logged-in
  [request]
  (let [user (user-created)
        session {:identity (:email user)}
        session-key (store/write-session handler/dev-store nil session)]
    (assoc request :cookies {"ring-session" {:value session-key}})))

(defn course-created []
  (let [course-name "test-course"
        [{course-id :id}] (db/create-course! {:name course-name})
        data {:initial-scene "test-scene"}]
    (db/save-course! {:course_id course-id :data data :owner_id 0 :created_at (jt/local-date-time)})
    {:id course-id
     :name course-name
     :data data}))

(defn scene-created []
  (let [{course-id :id course-name :name} (course-created)
        scene-name "test-scene"
        [{scene-id :id}] (db/create-scene! {:course_id course-id :name scene-name})
        data {:test "test"}]
    (db/save-scene! {:scene_id scene-id :data data :owner_id 0 :created_at (jt/local-date-time)})
    {:id scene-id
     :course-name course-name
     :name scene-name
     :data data}))

(defn get-course
  [course-name]
  (let [course-url (str "/api/courses/" course-name)
        request (-> (mock/request :get course-url)
                    user-logged-in)]
    (handler/dev-handler request)))

(defn save-course!
  [course-name data]
  (let [course-url (str "/api/courses/" course-name)
        request (-> (mock/request :post course-url (json/write-str data))
                    (mock/header :content-type "application/json")
                    user-logged-in)]
    (handler/dev-handler request)))

(defn get-scene
  [course-name scene-name]
  (let [scene-url (str "/api/courses/"course-name "/scenes/" scene-name)
        request (-> (mock/request :get scene-url)
                    user-logged-in)]
    (handler/dev-handler request)))

(defn save-scene!
  [course-name scene-name data]
  (let [url (str "/api/courses/" course-name "/scenes/" scene-name)
        request (-> (mock/request :post url (json/write-str data))
                    (mock/header :content-type "application/json")
                    user-logged-in)]
    (handler/dev-handler request)))