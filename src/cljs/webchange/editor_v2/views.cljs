(ns webchange.editor-v2.views
  (:require
    [webchange.editor-v2.fix-lodash]
    [webchange.editor-v2.concepts.views :refer [add-dataset-item-form edit-dataset-item-form]]
    [webchange.editor-v2.course-dashboard.views :refer [course-dashboard]]
    [webchange.editor-v2.lessons.views :refer [add-lesson-form edit-lesson-form]]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.scene.views :refer [scene-translate]]
    [webchange.routes :refer [redirect-to]]))

(def course-view course-dashboard)

(defn add-lesson-view
  [course-id level]
  [layout {:breadcrumbs [{:text     "Course"
                          :on-click #(redirect-to :course-editor-v2 :id course-id)}
                         {:text "Add lesson"}]}
   [add-lesson-form course-id level]])

(defn lesson-view
  [course-id level lesson]
  [layout {:breadcrumbs [{:text     "Course"
                          :on-click #(redirect-to :course-editor-v2 :id course-id)}
                         {:text "Edit lesson"}]}
   [edit-lesson-form course-id level lesson]])

(defn add-concept-view
  [course-id]
  [layout {:breadcrumbs [{:text     "Course"
                          :on-click #(redirect-to :course-editor-v2 :id course-id)}
                         {:text "Add dataset item"}]}
   [add-dataset-item-form course-id]])

(defn concept-view
  [course-id concept-id]
  [layout {:breadcrumbs [{:text     "Course"
                          :on-click #(redirect-to :course-editor-v2 :id course-id)}
                         {:text "Edit dataset item"}]}
   [edit-dataset-item-form course-id concept-id]])

(defn scene-view
  [course-id _]
  [layout {:breadcrumbs [{:text     "Course"
                          :on-click #(redirect-to :course-editor-v2 :id course-id)}
                         {:text "Scene"}]}
   [scene-translate]])
