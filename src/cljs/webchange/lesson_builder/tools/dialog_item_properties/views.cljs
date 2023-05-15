(ns webchange.lesson-builder.tools.dialog-item-properties.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.lesson-builder.tools.dialog-item-properties.state :as state]
    [webchange.ui.index :as ui]))

(defn- delay-field
  []
  (let [action-path @(re-frame/subscribe [::state/selected-action])
        value @(re-frame/subscribe [::state/delay])
        handle-delay-change #(re-frame/dispatch [::state/set-delay action-path %])]
    [:div
     [ui/input-label "Delay"]
     [ui/input {:value value
                :on-change handle-delay-change}]]))

(defn dialog-item-properties
  []
  (re-frame/dispatch [::state/init])
  (fn []
    [:div.widget--dialog-item-properties
     [:h1 "Properties"]
     [delay-field]
     [ui/button {:class-name "dialog-item-properties-apply"
                 :on-click #(re-frame/dispatch [::state/apply])} "Apply"]]))
