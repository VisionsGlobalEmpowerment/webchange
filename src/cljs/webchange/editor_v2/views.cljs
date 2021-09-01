(ns webchange.editor-v2.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.views :refer [activity-form]]
    [webchange.editor-v2.concepts.views :refer [add-dataset-item-form edit-dataset-item-form]]
    [webchange.editor-v2.course-dashboard.views :refer [course-dashboard]]
    [webchange.editor-v2.lessons.views :refer [add-lesson-form edit-lesson-form]]
    [webchange.editor-v2.components.page-layout.views :as current-layout]
    [webchange.editor-v2.components.breadcrumbs.views :refer [course-breadcrumbs]]
    [webchange.subs :as subs]))

(def course-view course-dashboard)

(defn add-lesson-view
  [course-id level]
  [current-layout/layout {:breadcrumbs (course-breadcrumbs course-id "Add lesson")}
   [add-lesson-form course-id level]])

(defn lesson-view
  [course-id level lesson]
  [current-layout/layout {:breadcrumbs (course-breadcrumbs course-id "Edit lesson")}
   [edit-lesson-form course-id level lesson]])

(defn add-concept-view
  [course-id]
  [current-layout/layout {:breadcrumbs (course-breadcrumbs course-id "Add dataset item")}
   [add-dataset-item-form course-id]])

(defn concept-view
  [course-id concept-id]
  [current-layout/layout {:breadcrumbs (course-breadcrumbs course-id "Edit dataset item")}
   [edit-dataset-item-form course-id concept-id]])

(defn scene-view
  [course-id _]
  (let [scene-data @(re-frame/subscribe [::subs/current-scene-data])]
    [activity-form {:course-id  course-id
                    :scene-data scene-data}]))
