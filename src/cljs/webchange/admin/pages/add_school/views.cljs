(ns webchange.admin.pages.add-school.views
  (:require [webchange.ui-framework.components.index :as ui]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [webchange.admin.pages.add-school.state :as state]
            [webchange.admin.widgets.page.views :as page]))

(defn- school-form
  []
  (r/with-let [data (r/atom {})]
    (let [errors @(re-frame/subscribe [::state/errors])
          handle-save-click #(re-frame/dispatch [::state/create-school @data])]
      [:<>
       [ui/input {:label "School Name"
                  :error (when (:name errors)
                           (:name errors))
                  :value (:name @data)
                  :on-change #(swap! data assoc :name %)}]
       [ui/input {:label "Location"
                  :error (when (:location errors)
                           (:location errors))
                  :value (:location @data)
                  :on-change #(swap! data assoc :location %)}]
       [ui/input {:label "About"
                  :error (when (:about errors)
                           (:about errors))
                  :value (:about @data)
                  :on-change #(swap! data assoc :about %)}]
       [ui/button {:on-click  handle-save-click}
        "Save"]])))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [props]
    [page/page
     [page/main-content {:title "Add School"}
      [school-form]]]))
