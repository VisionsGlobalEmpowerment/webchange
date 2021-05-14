(ns webchange.editor-v2.activity-form.generic.views-skeleton
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.state :as state]))

(defn skeleton
  [{:keys [top-left-component top-right-component bottom-component modals]}]
  (let [fullscreen? @(re-frame/subscribe [::state/diagram-fullscreen?])]
    [:div.generic-skeleton
     [:div.top-panel {:style (if fullscreen? {:display "none"} {})}
      [:div.data-container
       [:div.data-selector
        top-left-component]]
      [:div.scene-container
       top-right-component]]
     bottom-component
     (when (some? modals)
       modals)]))
