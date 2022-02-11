(ns webchange.common.validation.state
  (:require
    [re-frame.core :as re-frame]))

(defn path-to-db
  [id]
  [:validation-form id])

(re-frame/reg-event-fx
  ::set-errors
  (fn [{:keys [db]} [_ id errors]]
    {:db (assoc-in db (path-to-db id) errors)}))

(re-frame/reg-event-fx
  ::reset-errors
  (fn [{:keys [db]} [_ id]]
    {:db (update-in db (path-to-db []) dissoc id)}))

(re-frame/reg-sub
  ::errors
  (fn [db [_ id]]
    (get-in db (path-to-db id))))

(re-frame/reg-sub
  ::error
  (fn [[_ id]]
    (re-frame/subscribe [::errors id]))
  (fn [errors [_ _ field]]
    (get errors field)))
