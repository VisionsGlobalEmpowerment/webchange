(ns webchange.editor-v2.activity-form.common.object-form.animation-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.object-form.animation-form.state :as state]
    [webchange.editor-v2.activity-form.common.object-form.animation-form.views-skin :refer [skin]]
    [webchange.editor-v2.activity-form.common.object-form.components.scale.views :refer [scale-component]]
    [webchange.ui-framework.components.index :refer [icon-button input label select-image]]))

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
                    :show-image? false
                    :variant     "outlined"}]]))

(defn form
  [{:keys [id objects-data objects-names]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id objects-data objects-names])]
    [:div.animation-form
     [skeleton {:id id}]
     [skin {:id id}]
     [scale-component {:id         id
                       :class-name "scale-group"}]]))
