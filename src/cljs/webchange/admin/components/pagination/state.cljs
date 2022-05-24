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
  (fn [{:keys [db]} [_ {:keys [total per-page]}]]
    {:db (-> db
             (set-param :total total)
             (set-param :per-page per-page))}))

(re-frame/reg-sub
  ::buttons
  :<- [path-to-db]
  (fn [{:keys [total per-page]}]
    (let [buttons-number (-> (/ total per-page) (Math/ceil))]
      (print "buttons-number" buttons-number)
      (->> (range buttons-number)
           (map (fn [idx]
                  {:id    idx
                   :name  (inc idx)
                   :value [(inc (* idx per-page)) (Math/min (* (inc idx) per-page) total)]}))))))
