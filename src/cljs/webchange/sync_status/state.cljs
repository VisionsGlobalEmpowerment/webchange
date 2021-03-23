(ns webchange.sync-status.state
  (:require
    [re-frame.core :as re-frame]))

(defn path-to-db
  [relative-path]
  (concat [:sync-status] relative-path))

(defonce timeouts (atom {}))

(re-frame/reg-sub
  ::show?
  (fn [db]
    (get-in db (path-to-db [:show?]) false)))

(re-frame/reg-event-fx
  ::show
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:show?]) true)}))

(re-frame/reg-event-fx
  ::set-status-hidden
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:show?]) false)}))

(re-frame/reg-event-fx
  ::hide
  (fn [{:keys [_]} [_]]
    {:timeout {:id    :set-sync-status
               :event [::set-status-hidden]
               :time  500}}))

(re-frame/reg-fx
  :timeout
  (fn [{:keys [id event time]}]
    (when-some [existing (get @timeouts id)]
      (js/clearTimeout existing)
      (swap! timeouts dissoc id))
    (when (some? event)
      (swap! timeouts assoc id
             (js/setTimeout
               (fn []
                 (re-frame/dispatch event))
               time)))))
