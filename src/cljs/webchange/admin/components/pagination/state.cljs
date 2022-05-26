(ns webchange.admin.components.pagination.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]))

(def path-to-db :component/pagination)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(defn- set-param
  [db name value]
  (assoc db name value))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [current total]}]]
    {:db (-> db
             (set-param :current current)
             (set-param :total total))}))

(re-frame/reg-sub
  ::buttons
  :<- [path-to-db]
  (fn [{:keys [current total]}]
    (->> (range 1 (inc total))
         (map (fn [idx]
                {:value   idx
                 :title   idx
                 :active? (= idx current)})))))
