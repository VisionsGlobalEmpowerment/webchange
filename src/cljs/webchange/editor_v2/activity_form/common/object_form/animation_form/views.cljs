(ns webchange.editor-v2.activity-form.common.object-form.animation-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.object-form.animation-form.state :as state]
    [webchange.editor-v2.activity-form.common.object-form.animation-form.views-skin :refer [skin]]
    [webchange.ui-framework.components.index :refer [icon-button label select-image]]))

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

(defn form
  [{:keys [id objects-data objects-names]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id objects-data objects-names])]
    [:div
     [flip {:id id}]
     [skeleton {:id id}]
     [skin {:id id}]]))
