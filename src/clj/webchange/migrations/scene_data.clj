(ns webchange.migrations.scene-data
  (:require
    [clojure.tools.logging :as log]
    [mount.core :as mount]
    [clojure.java.jdbc :as jdbc]
    [webchange.db.core :refer [*db*] :as db]))

(defn- add-scene-data
  [{course-name :name coures-slug :slug course-id :id lang :lang owner-id :owner-id}]
  (let [scene-list (-> (db/get-latest-course-version {:course_id course-id})
                       :data
                       :scene-list)
        use-course-name? (-> scene-list count (<= 1))]
    (doseq [[scene-slug {course-scene-name :name preview :preview}] scene-list]
      (let [{scene-id :id} (db/get-scene {:course_id course-id :name (name scene-slug)})
            scene-name (or (if use-course-name? course-name course-scene-name)
                           (name scene-slug))]
        (jdbc/execute! *db* ["UPDATE scenes SET name = ?, slug = ?, lang = ?, image_src = ?, owner_id = ? WHERE id = ?"
                             scene-name (name scene-slug) lang preview owner-id scene-id])))))

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
