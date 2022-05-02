(ns webchange.admin.pages.new-school.views
  (:require [webchange.ui-framework.components.index :as ui]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [webchange.admin.routes :as routes]
            [webchange.admin.pages.schools.state :as schools-state]))

(defn page []
  (r/with-let [name (r/atom "")
               location (r/atom "")
               about (r/atom "")]
    [ui/card {:class-name  "school-card"}
     [:h3 "Add School !!!"]

     [:div
      "School name"
      [ui/input {:placeholder "Type School Name"
                 :value @name
                 :on-change #(reset! name %)}]

      "Location"
      [ui/input {:placeholder "My Location"
                 :value @location
                 :on-change #(reset! location %)}]
      "About"
      [ui/input {:placeholder "About this School"
                 :value @about
                 :on-change #(reset! about %)}]

      [ui/button {:on-click (fn []
                              (re-frame/dispatch [::schools-state/set-school nil
                                                  {:name @name
                                                   :location @location
                                                   :about @about
                                                   :courses 0
                                                   :students 0}])
                              (re-frame/dispatch [::routes/redirect "/admin/schools"]))}
       "Save"]]]))
