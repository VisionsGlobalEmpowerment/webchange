(ns webchange.editor-v2.activity-form.common.object-form.animation-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.object-form.animation-form.state :as state]
    [webchange.editor-v2.activity-form.common.object-form.animation-form.views-skin :refer [skin]]
    [webchange.ui-framework.components.index :refer [icon-button input label select-image]]))

(defn- flip
  [{:keys [id]}]
  (let [handle-click #(re-frame/dispatch [::state/flip-animation id])]
    [icon-button {:icon     "flip"
                  :on-click handle-click}
     "Flip"]))

(defn- skeleton
  [{:keys [class-name id]}]
  (let [value @(re-frame/subscribe [::state/current-skeleton id])
        options @(re-frame/subscribe [::state/skeletons-options id])
        handle-change (fn [skeleton]
                        (re-frame/dispatch [::state/set-skeleton id skeleton]))]
    [:div (cond-> {} (some? class-name) (assoc :class-name class-name))
     [label "Select character:"]
     [select-image {:value       (or value "")
                    :on-change   handle-change
                    :options     options
                    :with-arrow? false
                    :show-image? false}]]))

(defn- scale-control
  [{:keys [on-change title value]}]
  (let [handle-change (fn [new-value]
                        (when-not (= new-value 0)
                          (on-change new-value)))]
    [:div.scale-group
     [label title]
     [input {:value      value
             :on-change  handle-change
             :type       "float"
             :step       0.1
             :class-name "scale-input"}]]))

(defn- scale
  [{:keys [id]}]
  (let [current-scale @(re-frame/subscribe [::state/current-scale id])
        handle-change (fn [scale-key new-value]
                        (re-frame/dispatch [::state/set-scale id scale-key new-value]))]
    [:div.scale-control
     [scale-control {:value     (:x current-scale)
                     :on-change #(handle-change :x %)
                     :title     "Scale X:"}]
     [scale-control {:value     (:y current-scale)
                     :on-change #(handle-change :y %)
                     :title     "Scale Y:"}]]))

(defn form
  [{:keys [id objects-data objects-names]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id objects-data objects-names])]
    [:div.animation-form
     [skeleton {:id id}]
     [skin {:id id}]
     [flip {:id id}]
     [scale {:id id}]]))
