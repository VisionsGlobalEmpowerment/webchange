(ns webchange.student-dashboard.toolbar.sync.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.sw-utils.state.resources :as sw]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_]]
    {:dispatch [::sw/init]}))
