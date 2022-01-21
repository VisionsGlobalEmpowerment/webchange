(ns webchange.parent-dashboard.views
  (:require
    [webchange.parent-dashboard.add-student.views :as add-student]
    [webchange.parent-dashboard.students-list.views :as students-list]))

(defn dashboard
  []
  [:div.parent-page
   [students-list/students-list-page]])

(defn add-student
  []
  [:div.parent-page
   [add-student/add-student-page]])

(defn help-page
  [])
