(ns webchange.lesson-builder.widgets.object-form.text-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.state-fonts :as fonts]
    [webchange.ui.index :as ui]))

(defn- text-component
  [{:keys [data on-change]}]
  (let [value (get data :text "")
        handle-change #(on-change {:text %})]
    [ui/text-area {:value       value
                   :on-change   handle-change
                   :placeholder "Enter some text"
                   :class-name  "text-component"}]))

(defn- font-family-component
  [{:keys [data on-change]}]
  (let [value (get data :font-family "")
        options @(re-frame/subscribe [::fonts/font-family-options])
        handle-change #(on-change {:font-family %})]
    [:div.font-family-component
     [ui/input-label "Font"]
     [ui/select {:value     value
                 :options   options
                 :on-change handle-change}]]))

(defn- font-size-component
  [{:keys [data on-change]}]
  (let [value (get data :font-size "")
        options @(re-frame/subscribe [::fonts/font-size-options])
        handle-change #(on-change {:font-size %})]
    [:div.font-size-component
     [ui/input-label "Size"]
     [ui/select {:value     value
                 :type      "int"
                 :options   options
                 :on-change handle-change}]]))

(defn- font-color-component
  [{:keys [data on-change]}]
  (let [value (get data :fill "")
        options @(re-frame/subscribe [::fonts/font-color-options])
        handle-change #(on-change {:fill %})]
    [:div.font-color-component
     [ui/input-label "Color"]
     [ui/select {:value     (or value "")
                 :options   options
                 :on-change handle-change}]]))


(defn- text-align-button
  [{:keys [active? on-click value]}]
  [ui/button {:icon     (str "align-" value)
              :color    (if active? "blue-1" "grey-4")
              :on-click #(on-click value)}])

(defn- text-align-component
  [{:keys [data on-change]}]
  (let [value (get data :align "")
        handle-change #(on-change {:align %})]
    [:div.text-align-controls
     [text-align-button {:value    "left"
                         :active?  (= value "left")
                         :on-click handle-change}]
     [text-align-button {:value    "center"
                         :active?  (= value "center")
                         :on-click handle-change}]
     [text-align-button {:value    "right"
                         :active?  (= value "right")
                         :on-click handle-change}]]))

(defn text-form
  [{:keys [class-name data on-change]}]
  (let [handle-change #(when (fn? on-change) (on-change %))
        component-props {:data      data
                         :on-change handle-change}]
    [:div {:class-name (ui/get-class-name {"text-form" true
                                           class-name  (some? class-name)})}
     [:div.control-group
      [text-component component-props]]
     [:div.control-group
      [font-family-component component-props]
      [font-size-component component-props]]
     [:div.control-group
      [text-align-component component-props]]
     [:div.control-group
      [font-color-component component-props]]]))
