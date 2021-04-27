(ns webchange.editor-v2.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-creator.course-status.views :refer [review-status]]
    [webchange.editor-v2.fix-lodash]
    [webchange.editor-v2.concepts.views :refer [add-dataset-item-form edit-dataset-item-form]]
    [webchange.editor-v2.course-dashboard.views :refer [course-dashboard]]
    [webchange.editor-v2.lessons.views :refer [add-lesson-form edit-lesson-form]]
    [webchange.editor-v2.components.page-layout.views :as current-layout]
    [webchange.editor-v2.layout.views :refer [activity-edit-form]]
    [webchange.editor-v2.layout.utils :refer [get-activity-type]]
    [webchange.editor-v2.components.breadcrumbs.views :refer [root-breadcrumbs course-breadcrumbs]]
    [webchange.editor-v2.sandbox.views :refer [share-button]]
    [webchange.subs :as subs]
    [webchange.sync-status.views :refer [sync-status]]
    [webchange.ui-framework.layout.views :as ui]))

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
  (let [scene-data @(re-frame/subscribe [::subs/current-scene-data])
        activity-type (get-activity-type scene-data)
        layout (case activity-type
                 "flipbook" ui/layout
                 current-layout/layout)]
    (when (some? scene-data)
      [layout {:breadcrumbs (course-breadcrumbs course-id "Scene")
               :actions     [[sync-status {:class-name "sync-status"}]
                             [review-status]
                             [share-button]]}
       [activity-edit-form {:course-id  course-id
                            :scene-data scene-data}]])))
