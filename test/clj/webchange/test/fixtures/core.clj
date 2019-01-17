(ns webchange.test.fixtures.core
  (:require [clojure.test :refer :all]
            [webchange.db.core :refer [*db*] :as db]
            [webchange.auth.core :as auth]
            [webchange.handler :as handler]
            [config.core :refer [env]]
            [mount.core :as mount]
            [luminus-migrations.core :as migrations]
            [ring.middleware.session.store :as store]))

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
  (let [options {:first-name "Test" :last-name "Test" :email "test@example.com" :password "test"}
        [{user-id :id}] (-> options auth/prepare-register-data auth/create-user!)]
    (auth/activate-user! user-id)
    (assoc options :id user-id)))

(defn user-logged-in
  [request]
  (let [user (user-created)
        session {:identity (:email user)}
        session-key (store/write-session handler/dev-store nil session)]
    (assoc request :cookies {"ring-session" {:value session-key}})))