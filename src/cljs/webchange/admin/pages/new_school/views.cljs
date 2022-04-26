(ns webchange.admin.pages.new-school.views
  (:require [webchange.ui-framework.components.index :as ui]))

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

    [ui/button {:on-click #()} "Save"]]])
