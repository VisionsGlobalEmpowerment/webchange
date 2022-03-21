(ns webchange.editor-v2.activity-form.common.object-form.text-tracing-pattern-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.common.object-form.state :as state]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ id objects-data objects-names]]
      {:dispatch [::state/init id {:data  (select-keys objects-data [:dashed])
                                   :names objects-names}]}))
(re-frame/reg-sub
  ::current-dashed
  (fn [[_ id]]
    {:pre [(some? id)]}
    [(re-frame/subscribe [::state/current-data id])])
  (fn [[current-data]]
    (get current-data :dashed false)))

(re-frame/reg-event-fx
  ::set-current-dashed
  (fn [{:keys [db]} [_ id value]]
    {:dispatch [::state/update-current-data id {:dashed value}]}))
