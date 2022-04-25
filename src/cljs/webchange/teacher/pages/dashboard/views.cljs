(ns webchange.teacher.pages.dashboard.views
  (:require
    [webchange.teacher.widgets.header.views :refer [header]]))

(defn page
  []
  [:div
   [header]
   "Teacher Dashboard"])
