(ns webchange.common.background-selector.form.views-form
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.common.background-selector.form.state :as state]
    [webchange.ui-framework.components.index :refer [label select select-image]]))

(defn- type-selector
  [{:keys [on-change value]}]
  (let [background-type-options @(re-frame/subscribe [::state/available-background-types])]
    [:div.type-selector
     [label "Background type:"]
     [select {:value      value
              :options    background-type-options
              :on-change  on-change
              :class-name "type-input"
              :variant    "outlined"}]]))

(defn- background-select
  [{:keys [on-change type value]}]
  (let [options @(re-frame/subscribe [::state/background-options type])
        handle-change (fn [value]
                        (on-change type value))]
    [select-image {:value     value
                   :options   options
                   :on-change handle-change}]))

(defn- preview
  [{:keys [data]}]
  (let [{:keys [type single-background surface background decoration]} data
        images (->> (if (= type "background")
                      [single-background]
                      [background surface decoration])
                    (remove nil?))]
    [:div.preview-wrapper
     [:div.background-preview
      [:img.placeholder {:src (some #(and (some? %) %) images)}]
      (for [image images]
        ^{:key image}
        [:img {:src image}])]]))

(defn- init-data
  [{:keys [type] :as props}]
  (cond-> {:type type}
          (= type "layered-background") (merge {:background (get-in props [:background :src])
                                                :surface    (get-in props [:surface :src])
                                                :decoration (get-in props [:decoration :src])})
          (= type "background") (merge {:single-background (:src props)})))

(defn- data->background-props
  [{:keys [type single-background surface background decoration]}]
  (cond-> {:type type}
          (= type "background") (merge {:src single-background})
          (= type "layered-background") (merge {:surface    {:src surface}
                                                :background {:src background}
                                                :decoration {:src decoration}})))

(defn background-form
  [{:keys [on-change] :as props}]
  (r/with-let [data (r/atom (init-data props))

               handle-type-changed (fn [value]
                                     (swap! data assoc :type value)
                                     (-> @data data->background-props on-change))
               handle-background-changed (fn [type value]
                                           (swap! data assoc type value)
                                           (-> @data data->background-props on-change))

               _ (re-frame/dispatch [::state/init])]
    (let [background-type (get @data :type "")]
      [:div
       [type-selector {:value     background-type
                       :on-change handle-type-changed}]
       (into [:div.background-options]
             (->> (case background-type
                    "background" [:single-background]
                    "layered-background" [:background :surface :decoration]
                    [])
                  (map (fn [type]
                         ^{:key type}
                         [background-select {:type      type
                                             :value     (get @data type)
                                             :on-change handle-background-changed}]))))
       [preview {:data @data}]])))
