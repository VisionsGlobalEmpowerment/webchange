(ns webchange.editor-v2.activity-form.common.object-form.text-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.object-form.text-form.state :as state]
    [webchange.editor-v2.activity-form.common.object-form.voice-over-control.views :refer [voice-over-control]]
    [webchange.ui-framework.components.index :refer [icon select text-area]]))

(defn- text-component
  [{:keys [id]}]
  (let [value @(re-frame/subscribe [::state/current-text id])
        handle-change (fn [value] (re-frame/dispatch [::state/set-current-text id value]))]
    [text-area {:value      value
                :on-change  handle-change
                :class-name "text-value-control"}]))

(defn- font-family-component
  [{:keys [id]}]
  (let [value @(re-frame/subscribe [::state/current-font-family id])
        options @(re-frame/subscribe [::state/font-family-options])
        handle-change (fn [font-family]
                        (re-frame/dispatch [::state/set-current-font-family id font-family]))]
    [:div
     [icon {:icon "font-family"}]
     [select {:value               (or value "")
              :on-change           handle-change
              :options-text-suffix "pt"
              :options             options
              :show-buttons?       true
              :with-arrow?         false
              :class-name          "font-family-selector"}]]))

(defn- font-size-component
  [{:keys [id]}]
  (let [value @(re-frame/subscribe [::state/current-font-size id])
        handle-change (fn [font-size]
                        (re-frame/dispatch [::state/set-current-font-size id (.parseInt js/Number font-size)]))
        handle-dec-click (fn [] (handle-change (dec value)))
        handle-inc-click (fn [] (handle-change (inc value)))
        options @(re-frame/subscribe [::state/font-size-options id])]
    [:div
     [icon {:icon "font-size"}]
     [select {:value               value
              :on-change           handle-change
              :options-text-suffix "pt"
              :options             options
              :show-buttons?       true
              :with-arrow?         false
              :on-arrow-up-click   handle-inc-click
              :on-arrow-down-click handle-dec-click
              :class-name          "font-size-selector"}]]))

(defn form
  [{:keys [id objects-data objects-names]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id objects-data objects-names])]
    [:div.text-form
     [:div.font-controls
      [font-family-component {:id id}]
      [font-size-component {:id id}]]
     [:div.text-control
      [text-component {:id id}]
      [voice-over-control {:id          id
                           :object-name (first objects-names)}]]]))
