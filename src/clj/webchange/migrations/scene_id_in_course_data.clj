(ns webchange.migrations.scene-id-in-course-data
  (:require
    [clojure.tools.logging :as log]
    [java-time :as jt]
    [clojure.string :as str]
    [mount.core :as mount]
    [webchange.course.core :as course]
    [clojure.java.jdbc :as jdbc]
    [webchange.db.core :refer [*db*] :as db]))

(def owner-id 1)

(defn- add-scene-ids
  [{coures-slug :slug course-id :id}]
  (let [update-activity (fn [{activity-name :activity :as activity}]
                          (let [{scene-id :id} (db/get-scene {:course_id course-id :name activity-name})]
                            (assoc activity :scene-id scene-id)))
        update-lesson (fn [lesson]
                        (update lesson :activities #(map update-activity %)))
        update-level (fn [level]
                       (update level :lessons #(map update-lesson %)))
        course-data (-> (db/get-latest-course-version {:course_id course-id})
                        :data
                        (update :levels #(map update-level %)))]
    (course/save-course! coures-slug course-data owner-id)))

(defn migrate-up
  [_]
  (mount/start)
  (let [courses (db/get-courses)]
    (doseq [course courses]
      (add-scene-ids course))))

(defn migrate-down
  [_]
  (mount/start))
