(ns webchange.admin.pages.schools.views
  (:require [re-frame.core :as re-frame]
            [webchange.admin.pages.schools.state :as state]
            [webchange.ui-framework.components.index :as ui]))

(defn school-item [data]
  [:div.school-item
   (:name data)
   [:div.school-item-buttons
    [ui/button {:aria-label "Users"
                :on-click   #()}
     (:users data)
     [ui/icon {:icon "user"}]]
    [ui/button {:aria-label "Presentation"
                :on-click   #()}
     (:presentation data)
     [ui/icon {:icon "warning"}]]

    [ui/button {:aria-label "Delete"
                :on-click   #()}
     [ui/icon {:icon "remove"}]]

    [ui/button {:aria-label "Edit"
                :on-click   #()}
     [ui/icon {:icon "edit"}]]]])

(defn page []
  (let [schools @(re-frame/subscribe [::state/schools-list])]
    [ui/card {:class-name "school-card"}
     [:h3 "Schools"]
     [:div
      [ui/button {:aria-label "New School"
                  :on-click   #()}
       [:span "New School"]
       [ui/icon {:icon "add"}]]

      (for [{:keys [id] :as school-data} schools]
        ^{:key id}
        [school-item school-data])]]))
