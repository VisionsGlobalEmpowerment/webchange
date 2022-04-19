(ns webchange.dev-templates.utils
  (:require [webchange.course.core :as core]
            [webchange.dev-templates :as t]))

(defn get-scene
  [course-slug activity-name]
  (core/get-scene-latest-version course-slug activity-name))

(defn save-scene!
  [scene-data course-slug activity-name]
  (core/save-scene! course-slug activity-name scene-data t/user-id))

(defn update-scene-object
  ([course-slug activity-name object-name data-patch]
   (update-scene-object course-slug activity-name
                        (fn [activity-data]
                          (update-in activity-data [:objects object-name] merge data-patch))))
  ([course-slug activity-name handler]
   (-> (get-scene course-slug activity-name)
       (handler)
       (save-scene! course-slug activity-name))))
