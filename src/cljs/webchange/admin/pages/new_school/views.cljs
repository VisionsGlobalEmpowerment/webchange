(ns webchange.admin.pages.new-school.views
  (:require [webchange.ui-framework.components.index :as ui]
            [re-frame.core :as re-frame]
            [webchange.admin.routes :as routes]
            [webchange.admin.pages.schools.state :as schools-state]))

(defn page []
  [ui/card {:class-name  "school-card"}
   [:h3 "Add School"]

   [:div
    "School name"
    [ui/input {:placeholder "Type School Name"
               :on-change #()}]

    "Location"
    [ui/input {:placeholder "My Location"
               :on-change #()}]
    "About"
    [ui/input {:placeholder "About this School"
               :on-change #()}]

    [ui/button {:on-click (fn []
                            (re-frame/dispatch [::schools-state/set-school :new-school-id
                                                {:name "New School" :presentation 0 :users 0}])
                            (re-frame/dispatch [::routes/redirect "/admin/schools"]))}
     "Save"]]])
