(ns webchange.admin.pages.school-profile.views
  (:require
    [webchange.admin.widgets.header.views :refer [header]]))

(defn page
  [{:keys [school-id]}]
  [:div
   [header]
   "School Profile: " school-id])
