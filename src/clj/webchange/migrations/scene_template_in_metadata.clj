(ns webchange.migrations.scene-template-in-metadata
  (:require
    [clojure.tools.logging :as log]
    [clojure.string :as str]
    [mount.core :as mount]
    [webchange.course.core :as course]
    [clojure.java.jdbc :as jdbc]
    [webchange.db.core :refer [*db*]]))

(defn- get-scenes
  []
  (jdbc/query *db* ["SELECT s.id, s.metadata, sv.data FROM scenes s inner join scene_versions sv on s.id = sv.scene_id;"]))

(defn- set-metadata
  [scene-id metadata]
  (jdbc/execute! *db* ["UPDATE scenes SET metadata = ? WHERE id = ?" metadata scene-id]))

(defn migrate-up
  [_]
  (mount/start)
  (doseq [{:keys [id metadata data]} (get-scenes)]
    (let [metadata (assoc metadata :template-id (-> data :metadata :template-id))]
      (set-metadata id metadata))))

(defn migrate-down
  [_]
  (mount/start))

(comment
  (-> (get-scenes)
      first
      :data
      :metadata
      :template-id))
