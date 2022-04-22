(ns webchange.admin.pages.school-profile.views)

(defn page
  [{:keys [school-id]}]
  [:div "School Profile: " school-id])
