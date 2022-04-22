(ns webchange.admin.pages.schools.views
  (:require
    [webchange.admin.widgets.header.views :refer [header]]))

(defn page
  []
  [:div
   [header]
   "Schools"])
