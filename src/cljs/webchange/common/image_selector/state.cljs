(ns webchange.common.image-selector.state
  (:require
    [re-frame.core :as re-frame]))

(defn path-to-db [relative-path] (concat [:image-selector] relative-path))

(def modal-state-path (path-to-db [:modal-state]))

(re-frame/reg-sub
  ::open?
  (fn [db]
    (get-in db modal-state-path false)))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path true)}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))
