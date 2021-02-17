(ns webchange.editor-v2.layout.flipbook.page-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.activity-action.state :as activity-action]
    [webchange.editor-v2.layout.flipbook.page-form.utils :refer [scene-data->objects-list]]
    [webchange.state.state :as state]))

(re-frame/reg-event-fx
  ::remove-page
  (fn [{:keys [_]} [_ data]]
    {:dispatch [::activity-action/call-activity-action {:action "remove-page"
                                                        :data   data}]}))

(re-frame/reg-event-fx
  ::move-page
  (fn [{:keys [_]} [_ data]]
    {:dispatch [::activity-action/call-activity-action {:action "move-page"
                                                        :data   data}]}))

(re-frame/reg-sub
  ::page-text-data
  (fn []
    [(re-frame/subscribe [::state/scene-data])])
  (fn [[scene-data] [_ stage page-side]]
    (scene-data->objects-list scene-data stage page-side)))
