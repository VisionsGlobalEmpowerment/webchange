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
  [course-slug activity-name object-name data-patch]
  (-> (get-scene course-slug activity-name)
      (update-in [:objects object-name] merge data-patch)
      (save-scene! course-slug activity-name)))
