(ns webchange.admin.pages.school-profile.views
  (:require [webchange.ui-framework.components.index :as ui]
            [webchange.admin.pages.school-profile.state :as state]
            [reagent.core :as r]
            [re-frame.core :as re-frame]
            [webchange.admin.routes :as routes]
            [webchange.admin.pages.schools.state :as schools-state]))

(defn page
  [{:keys [school-id]}]
  (re-frame/dispatch [::state/init school-id])
  (let [school @(re-frame/subscribe [::state/current-school])]
    (r/with-let [name (r/atom (:name school))]
    [ui/card {:class-name  "school-card"}
     [:h3 "School profile"]
     [:div
      "School name"
      [ui/input {:value @name
                 :on-change #(reset! name %)}]
      [ui/button
       {:on-click (fn []
                    (re-frame/dispatch [::schools-state/set-school
                                        school-id (assoc school :name @name)])
                    (re-frame/dispatch [::routes/redirect "/admin/schools"]))}
       "Save"]]])))
