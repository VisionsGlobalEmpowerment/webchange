(ns webchange.dashboard.students.views
  (:require
    [webchange.dashboard.students.views-dashboard :as dashboard]
    [webchange.dashboard.students.views-list-menu :as list-menu]
    [webchange.dashboard.students.student-profile.views :as profile]))

(defn students-dashboard
  [& args]
  (apply dashboard/students-dashboard args))

(defn students-list-menu
  [& args]
  (apply list-menu/students-list-menu args))

(defn student-profile
  [& args]
  (apply profile/student-profile args))
