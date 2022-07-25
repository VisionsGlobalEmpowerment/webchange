(ns webchange.lesson-builder.tools.template-options.sandbox-digging-rounds.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.tools.template-options.state :refer [path-to-db] :as template-options-state]))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [rounds-saved-value (get-in db [:form :rounds])
          rounds-number (count rounds-saved-value)]
      {:db (-> db
               (assoc :original-rounds-number rounds-number)
               (assoc :rounds-number rounds-number)
               (update :form dissoc :delete-last-round)
               (assoc :round-state (into [] (repeat rounds-number false))))})))

(re-frame/reg-sub
  ::rounds-number
  :<- [path-to-db]
  (fn [db]
    (get db :rounds-number)))

(re-frame/reg-sub
  ::last-round?
  :<- [path-to-db]
  (fn [db [_ idx]]
    (let [number (get db :rounds-number)]
      (= idx (dec number)))))

(re-frame/reg-event-fx
  ::add-round
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [current-round-idx (get db :rounds-number)]
      {:db (-> db
               (update :rounds-number inc)
               (update-in [:form :rounds] concat [{:which current-round-idx}])
               (update :round-state concat [true]))})))

(re-frame/reg-sub
  ::round-data
  :<- [::template-options-state/field-value "rounds"]
  (fn [rounds [_ idx]]
    (if (< idx (count rounds))
      (get rounds idx))))

(re-frame/reg-event-fx
  ::change-round-data
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ round-idx key value]]
    {:db (-> db
             (assoc-in [:form :rounds round-idx key] value))}))

(re-frame/reg-event-fx
  ::delete-last-round
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [original-rounds-number (:original-rounds-number db)
          current-rounds-number (:rounds-number db)]
      {:db (cond-> db
                   :always (update :rounds-number dec)
                   :always (update-in [:form :rounds] drop-last)
                   :always (update :round-state drop-last)
                   (= original-rounds-number current-rounds-number) (assoc-in [:form :delete-last-round] true))})))

(re-frame/reg-sub
  ::round-state
  :<- [path-to-db]
  (fn [db [_ idx]]
    (get-in db [:round-state idx])))

(re-frame/reg-event-fx
  ::toggle-round-state
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ idx]]
    {:db (update-in db [:round-state idx] not)}))

