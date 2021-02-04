(ns webchange.editor-v2.components.breadcrumbs.views
  (:require
    [webchange.routes :refer [redirect-to location]]))

(defn root-breadcrumbs
  [title]
  [{:text "Courses"
    :on-click #(location :courses)}
   {:text title}])

(defn course-breadcrumbs
  [course-id title]
  [{:text "Courses"
    :on-click #(location :courses)}
   {:text     "Course"
    :on-click #(redirect-to :course-editor-v2 :id course-id)}
   {:text title}])
