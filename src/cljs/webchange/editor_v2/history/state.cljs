(ns webchange.editor-v2.history.state
  (:require
    [re-frame.core :as re-frame]))

(defn path-to-db
  [relative-path]
  (concat [:editor-v2 :history] relative-path))

(re-frame/reg-event-fx
  ::init-history
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:undo-events]) [])}))

(re-frame/reg-event-fx
  ::add-history-event
  (fn [{:keys [db]} [_ event]]
    {:db (update-in db (path-to-db [:undo-events]) conj event)}))

(defn history
  [db]
  (vec (get-in db (path-to-db [:undo-events]))))

(re-frame/reg-sub ::history history)

(re-frame/reg-sub
  ::has-history
  (fn []
    [(re-frame/subscribe [::history])])
  (fn [[history]]
    (not (empty? history))))

(re-frame/reg-event-fx
  ::remove-history-event
  (fn [{:keys [db]} [_ ]]
    (let [history-data (history db)]
     {:db (assoc-in db (path-to-db [:undo-events]) (drop-last history-data))})))
