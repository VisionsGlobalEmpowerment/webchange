(ns webchange.editor-v2.layout.components.sync-status.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.warehouse :as warehouse]))

(re-frame/reg-sub
  ::sync-in-progress?
  (fn []
    [(re-frame/subscribe [::warehouse/sync-status :update-activity])])
  (fn [[activity-updating?]]
    activity-updating?))








