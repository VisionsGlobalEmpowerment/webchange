(ns webchange.editor-v2.activity-form.common.object-form.components.scale.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.object-form.components.scale.state :as state]
    [webchange.ui-framework.components.index :refer [icon-button input label]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- flip
  [{:keys [id]}]
  (let [handle-click #(re-frame/dispatch [::state/flip-animation id])]
    [icon-button {:icon     "flip"
                  :on-click handle-click
                  :variant  "outlined"}
     "Flip"]))

(defn- scale
  [{:keys [id]}]
  (let [current-scale @(re-frame/subscribe [::state/current-scale id])
        handle-change (fn [new-value]
                        (when (> new-value 0)
                          (re-frame/dispatch [::state/set-scale id new-value])))]
    [:div.scale-control
     [label "Scale:"]
     [input {:value      current-scale
             :on-change  handle-change
             :type       "float"
             :step       0.05
             :class-name "scale-input"}]]))

(defn scale-component
  [{:keys [id class-name] :as props}]
  (let [show-scale? @(re-frame/subscribe [::state/show-scale-control? id])
        show-flip? @(re-frame/subscribe [::state/show-flip-control? id])]
    [:div {:class-name (get-class-name {"scale-component" true
                                        class-name        (some? class-name)})}
     (when show-scale? [scale props])
     (when show-flip? [flip props])]))
