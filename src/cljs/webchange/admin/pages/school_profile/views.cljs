(ns webchange.admin.pages.school-profile.views
  (:require))

(defn page
  [{:keys [school-id]}]
  [:div
   "School Profile: " school-id])
