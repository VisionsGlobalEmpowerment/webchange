(ns webchange.lesson-builder.tools.template-options.first-words-book-spreads.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.tools.template-options.state :refer [path-to-db] :as template-options-state]))

(re-frame/reg-event-fx
  ::init
  [(re-frame/inject-cofx :activity-data)
   (i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [spreads-saved-value (get-in db [:form :spreads])
          spreads-number (count spreads-saved-value)]
      {:db (-> db
               (assoc :spreads-number spreads-number))})))


(re-frame/reg-sub
  ::spreads-number
  :<- [path-to-db]
  (fn [db]
    (get db :spreads-number)))

(re-frame/reg-event-fx
  ::add-spread
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db
             (update :spreads-number inc)
             (update-in [:form :spreads] concat [{}]))}))

(re-frame/reg-sub
  ::spread-data
  :<- [::template-options-state/field-value "spreads"]
  (fn [spreads [_ idx]]
    (if (< idx (count spreads))
      (get spreads idx))))

(re-frame/reg-event-fx
  ::change-spread-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ spread-idx key value]]
    {:db (-> db
             (assoc-in [:form :spreads spread-idx key] value))}))
