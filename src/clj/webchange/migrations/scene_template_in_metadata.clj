(ns webchange.migrations.scene-template-in-metadata
  (:require
    [clojure.tools.logging :as log]
    [clojure.string :as str]
    [mount.core :as mount]
    [webchange.course.core :as course]
    [clojure.java.jdbc :as jdbc]
    [webchange.db.core :refer [*db*]]))

(defn- update-scenes
  [update-fn]
  (jdbc/query *db* ["SELECT * FROM scenes"] {:row-fn update-fn}))

(defn- set-metadata
  [scene-id metadata]
  (jdbc/execute! *db* ["UPDATE scenes SET metadata = ? WHERE id = ?" metadata scene-id]))

(defn- latest-version
  [scene-id]
  (jdbc/query *db* ["SELECT * from scene_versions WHERE scene_id = ? ORDER BY created_at DESC LIMIT 1;" scene-id]))

(defn- update-scene-metadata
  [{:keys [id metadata]}]
  (let [data (-> (latest-version id) first (get :data))
        metadata (assoc metadata :template-id (-> data :metadata :template-id))]
    (set-metadata id metadata)))

(defn migrate-up
  [_]
  (mount/start)
  (update-scenes update-scene-metadata))

(defn migrate-down
  [_]
  (mount/start))
