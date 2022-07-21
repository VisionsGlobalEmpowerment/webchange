(ns webchange.lesson-builder.tools.template-options.first-words-book-spreads.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.tools.template-options.state :refer [path-to-db] :as template-options-state]))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [spreads-saved-value (get-in db [:form :spreads])
          spreads-number (count spreads-saved-value)]
      {:db (-> db
               (assoc :original-spreads-number spreads-number)
               (assoc :spreads-number spreads-number)
               (update :form dissoc :delete-last-spread)
               (assoc :spread-state (into [] (repeat spreads-number {:left false :right false}))))})))

(re-frame/reg-sub
  ::spreads-number
  :<- [path-to-db]
  (fn [db]
    (get db :spreads-number)))

(re-frame/reg-sub
  ::last-spread?
  :<- [path-to-db]
  (fn [db [_ idx]]
    (let [number (get db :spreads-number)]
      (= idx (dec number)))))

(re-frame/reg-event-fx
  ::add-spread
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [current-id (-> db (get :spreads-number) (+ 2))]
      {:db (-> db
               (update :spreads-number inc)
               (update-in [:form :spreads] concat [{:id current-id}])
               (update :spread-state concat [{:left true
                                              :right true}]))})))

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

(re-frame/reg-event-fx
  ::delete-last-spread
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [original-spreads-number (:original-spreads-number db)
          current-spreads-number (:spreads-number db)]
      {:db (cond-> db
                   :always (update :spreads-number dec)
                   :always (update-in [:form :spreads] drop-last)
                   :always (update :spread-state drop-last)
                   (= original-spreads-number current-spreads-number) (assoc-in [:form :delete-last-spread] true))})))

(re-frame/reg-sub
  ::spread-state
  :<- [path-to-db]
  (fn [db [_ idx]]
    (get-in db [:spread-state idx])))

(re-frame/reg-event-fx
  ::toggle-spread-state
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ idx key]]
    {:db (update-in db [:spread-state idx key] not)}))
