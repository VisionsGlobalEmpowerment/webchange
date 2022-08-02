(ns webchange.lesson-builder.widgets.audio-list.item-edit.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]))

(def path-to-db :lesson-builder/audio-list-item-edit)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; state

(defn- get-state
  [db url]
  (get db url))

(defn- set-state
  [db url value]
  (assoc db url value))

(defn- update-state
  [db url data-patch]
  (update db url merge data-patch))

(re-frame/reg-sub
  ::state
  :<- [path-to-db]
  (fn [db [_ url]]
    (get-state db url)))

;; value

(re-frame/reg-sub
  ::value
  (fn [[_ url]]
    (re-frame/subscribe [::state url]))
  (fn [state]
    (get state :value "")))

(re-frame/reg-event-fx
  ::set-value
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ url value]]
    {:db (update-state db url {:value value})}))

;; events

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [name on-cancel on-change url]}]]
    {:db (set-state db url {:value     name
                            :on-cancel on-cancel
                            :on-change on-change})}))

(re-frame/reg-event-fx
  ::save
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [url]}]]
    (let [{:keys [on-change value]} (get-state db url)]
      (cond-> {:db (dissoc db url)}
              (fn? on-change) (assoc :callback [on-change value])))))

(re-frame/reg-event-fx
  ::reset
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [url]}]]
    (let [{:keys [on-cancel]} (get-state db url)]
      (cond-> {:db (dissoc db url)}
              (fn? on-cancel) (assoc :callback on-cancel)))))
