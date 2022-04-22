(ns webchange.admin-app.school-profile.views)

(defn page
  [{:keys [school-id]}]
  [:div "School Profile: " school-id])
