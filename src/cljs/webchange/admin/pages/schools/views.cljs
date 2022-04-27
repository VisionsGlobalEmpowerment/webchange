(ns webchange.admin.pages.schools.views
  (:require [re-frame.core :as re-frame]
            [webchange.admin.pages.schools.state :as state]
            [webchange.ui-framework.components.index :as ui]
            [webchange.admin.routes :as routes]))

(defn school-item [id data]
  [:div.school-item
   (:name data)
   [:div.school-item-buttons
    [ui/button {:aria-label "Users"
                :on-click #()}
     (or (:users data) 0)
     [ui/icon {:icon "user"}]]
    [ui/button {:aria-label "Presentation"
                :on-click #()}
     (or (:presentation data) 0)
     [ui/icon {:icon "warning"}]]

    [ui/button {:aria-label "Delete"
                :on-click #()}
     [ui/icon {:icon "remove"}]]

    [ui/button {:aria-label "Edit"
                :on-click #(re-frame/dispatch [::routes/redirect (str "/admin/schools/" id)])}
     [ui/icon {:icon "edit"}]]]])

(defn page
  []
  (re-frame/dispatch [::state/init])
  (let [schools @(re-frame/subscribe [::state/schools-list])]
    [ui/card {:class-name "school-card"}
     [:h3 "Schools"]
     [:div
      [ui/button {:aria-label "New School"
                  :on-click #(re-frame/dispatch [::routes/redirect "/admin/new-school"])}
       [:span "New School"]
       [ui/icon {:icon "add"}]]

      (for [[id school] schools]
        ^{:key id}
        [school-item id school])]]))
