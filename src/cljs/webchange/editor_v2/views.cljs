(ns webchange.editor-v2.views
  (:require
    [webchange.editor-v2.fix-lodash]
    [webchange.editor-v2.concepts.views :refer [add-dataset-item-form edit-dataset-item-form]]
    [webchange.editor-v2.course-dashboard.views :refer [course-dashboard]]
    [webchange.editor-v2.lessons.views :refer [add-lesson-form edit-lesson-form]]
    [webchange.editor-v2.components.page-layout.views :refer [layout]]
    [webchange.editor-v2.layout.views :refer [activity-edit-form]]
    [webchange.editor-v2.components.breadcrumbs.views :refer [root-breadcrumbs course-breadcrumbs]]))

(def course-view course-dashboard)

(defn add-lesson-view
  [course-id level]
  [layout {:breadcrumbs (course-breadcrumbs course-id "Add lesson")}
   [add-lesson-form course-id level]])

(defn lesson-view
  [course-id level lesson]
  [layout {:breadcrumbs (course-breadcrumbs course-id "Edit lesson")}
   [edit-lesson-form course-id level lesson]])

(defn add-concept-view
  [course-id]
  [layout {:breadcrumbs (course-breadcrumbs course-id "Add dataset item")}
   [add-dataset-item-form course-id]])

(defn concept-view
  [course-id concept-id]
  [layout {:breadcrumbs (course-breadcrumbs course-id "Edit dataset item")}
   [edit-dataset-item-form course-id concept-id]])

(defn scene-view
  [course-id _]
  [layout {:breadcrumbs (course-breadcrumbs course-id "Scene")}
   [activity-edit-form {:course-id course-id}]])
