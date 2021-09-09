(ns webchange.common.image-selector.state
  (:require
    [re-frame.core :as re-frame]))

(defn path-to-db
  [id relative-path]
  {:pre [(some? id) (sequential? relative-path)]}
  (->> relative-path
       (concat [:image-selector id])
       (vec)))

(def modal-state-path [:modal-state])

(re-frame/reg-sub
  ::open?
  (fn [db [_ id]]
    (get-in db (path-to-db id modal-state-path) false)))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_ id]]
    {:db (assoc-in db (path-to-db id modal-state-path) true)}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_ id]]
    {:db (assoc-in db (path-to-db id modal-state-path) false)}))
