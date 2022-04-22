(ns webchange.admin-app.state
  (:require
    [re-frame.core :as re-frame]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:admin-app])
       (vec)))

(def current-page-path (path-to-db [:current-page]))

(re-frame/reg-sub
  ::current-page
  (fn [db]
    (get-in db current-page-path)))

(re-frame/reg-event-fx
  ::set-current-page
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db current-page-path value)}))
