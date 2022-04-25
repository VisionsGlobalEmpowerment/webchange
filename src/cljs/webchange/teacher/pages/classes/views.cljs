(ns webchange.teacher.pages.classes.views
  (:require
    [webchange.teacher.widgets.header.views :refer [header]]))

(defn page
  []
  [:div
   [header]
   "Teacher Classes"])
