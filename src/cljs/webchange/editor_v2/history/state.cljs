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

(defn- batch-events
  [last-event new-event]
  (cond
    (nil? last-event) [new-event]
    (= last-event new-event) [last-event]
    :else (let [same-action? (and (= (:path last-event) (:path new-event))
                                  (= (:concept-id last-event) (:concept-id new-event)))
                same-fields? (= (:to last-event) (:from new-event))]
            (if (and same-action? same-fields?)
              [(assoc new-event :from (:from last-event))]
              [last-event new-event]))))

(defn history
  [db]
  (vec (get-in db (path-to-db [:undo-events]))))

(re-frame/reg-sub ::history history)

(re-frame/reg-event-fx
  ::add-history-event
  (fn [{:keys [db]} [_ event]]
    (let [events (history db)
          events-list (concat (butlast events)
                              (batch-events (last events) event))]
      {:db (assoc-in db (path-to-db [:undo-events]) events-list)})))

(re-frame/reg-sub
  ::has-history
  (fn []
    [(re-frame/subscribe [::history])])
  (fn [[history]]
    (not (empty? history))))

(re-frame/reg-event-fx
  ::remove-history-event
  (fn [{:keys [db]} [_]]
    (let [history-data (history db)]
      {:db (assoc-in db (path-to-db [:undo-events]) (drop-last history-data))})))
