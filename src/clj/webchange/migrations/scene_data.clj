(ns webchange.migrations.scene-data
  (:require
    [clojure.tools.logging :as log]
    [mount.core :as mount]
    [clojure.java.jdbc :as jdbc]
    [webchange.db.core :refer [*db*] :as db]))

(defn- add-scene-data
  [{coures-slug :slug course-id :id lang :lang owner-id :owner-id}]
  (let [scene-list (-> (db/get-latest-course-version {:course_id course-id})
                       :data
                       :scene-list)]
    (doseq [[scene-slug {:keys [preview name]}] scene-list]
      (let [{scene-id :id} (db/get-scene {:course-id course-id :name (name scene-slug)})]
        (log/debug "update scene" scene-id name scene-slug lang preview owner-id)
        (jdbc/execute! *db* ["UPDATE scenes SET name = ?, slug = ?, lang = ?, image_src = ?, owner_id = ? WHERE id = ?"
                             name scene-slug lang preview owner-id scene-id])))))

(defn migrate-up
  [_]
  (mount/start)
  (let [courses (db/get-courses)]
    (doseq [course courses]
      (add-scene-data course))))

(defn migrate-down
  [_]
  (mount/start)
  (jdbc/execute! *db* ["UPDATE scenes SET name = slug where slug is not null"]))
