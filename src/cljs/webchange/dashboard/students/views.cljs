(ns webchange.dashboard.students.views
  (:require
    [webchange.dashboard.students.views-dashboard :as dashboard]
    [webchange.dashboard.students.views-list-menu :as list-menu]
    ))

(defn students-dashboard
  [& args]
  (apply dashboard/students-dashboard args))

(defn students-list-menu
  [& args]
  (apply list-menu/students-list-menu args))
