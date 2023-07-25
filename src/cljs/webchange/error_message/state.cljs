(ns webchange.error-message.state
  (:require
    [re-frame.core :as re-frame]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:error-message])))

(re-frame/reg-event-fx
  ::show
  (fn [{:keys [db]} [_ type errors]]
    {:db (assoc-in db (path-to-db [:current-error]) {:type    type
                                                     :message errors})}))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:current-error]) nil)}))

(re-frame/reg-event-fx
  ::schedule-reset
  (fn [{:keys [_]} [_]]
    {:dispatch-later [{:ms 7000
                       :dispatch [::reset]}]}))

(re-frame/reg-sub
  ::error-message
  (fn [db]
    (get-in db (path-to-db [:current-error]))))
