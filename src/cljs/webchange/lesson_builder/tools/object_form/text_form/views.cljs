(ns webchange.lesson-builder.tools.object-form.text-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.object-form.text-form.state :as state]
    [webchange.state.state-fonts :as fonts]
    [webchange.ui.index :as ui]))

(defn- text-component
  [target]
  (let [value @(re-frame/subscribe [::state/text target])
        handle-change (fn [value] (re-frame/dispatch [::state/set-text target value]))]
    [:div.text-component
     [ui/text-area {:value      value
                    :on-change  handle-change}]]))

(defn- font-family-component
  [target]
  (let [value @(re-frame/subscribe [::state/font-family target])
        options @(re-frame/subscribe [::fonts/font-family-options])]
    [:div.font-family-component
     [ui/input-label "Font"] 
     [ui/select {:value               (or value "")
                 :on-change           #(re-frame/dispatch [::state/set-font-family target %])
                 :options             options}]]))


(defn- font-size-component
  [target]
  (let [value @(re-frame/subscribe [::state/font-size target])
        options @(re-frame/subscribe [::fonts/font-size-options target])]
    [:div.font-size-component
     [ui/input-label "Size"]
     [ui/select {:value               value
                 :options             options
                 :on-change           #(re-frame/dispatch [::state/set-font-size target %])
                 :on-arrow-up-click   #(re-frame/dispatch [::state/set-font-size target (inc value)])
                 :on-arrow-down-click #(re-frame/dispatch [::state/set-font-size target (dec value)])}]]))

(defn- font-color-component
  [target]
  (let [value @(re-frame/subscribe [::state/font-color target])
        options @(re-frame/subscribe [::fonts/font-color-options])]
    [:div.font-color-component
     [ui/input-label "Color"]
     [ui/select {:value         (or value "")
                 :options options
                 :on-change     #(re-frame/dispatch [::state/set-font-color target %])}]]))


(defn- text-align-button
  [{:keys [active? on-click value]}]
  [ui/button {:icon     (str "align-" value)
              :color    (if active? "blue-1" "grey-4")
              :on-click #(on-click value)}])

(defn- text-align-component
  [target]
  (let [value @(re-frame/subscribe [::state/text-align target])
        handle-click (fn [align]
                       (re-frame/dispatch [::state/set-text-align target align]))]
    [:div.text-align-controls
     [text-align-button {:value    "left"
                         :active?  (= value "left")
                         :on-click handle-click}]
     [text-align-button {:value    "center"
                         :active?  (= value "center")
                         :on-click handle-click}]
     [text-align-button {:value    "right"
                         :active?  (= value "right")
                         :on-click handle-click}]]))

(defn fields
  [target]
  (re-frame/dispatch [::state/init target])
  (fn [target]
    [:div.text-form-fields
     [:div.text-control
      [text-component target]]
     [:div.font-controls
      [font-family-component target]
      [font-size-component target]]
     [text-align-component target]
     [font-color-component target]]))


