(ns webchange.state.state-templates
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.state :as state]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:templates])
       (state/path-to-db)))

(re-frame/reg-event-fx
  ::load-templates
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse/load-templates {:on-success [::set-templates-list]}]}))

(def templates-list-path (path-to-db [:templates-list]))

(re-frame/reg-sub
  ::templates-list
  (fn [db [_]]
    (get-in db templates-list-path [])))

(re-frame/reg-event-fx
  ::set-templates-list
  (fn [{:keys [db]} [_ templates]]
    {:db (assoc-in db templates-list-path templates)}))
