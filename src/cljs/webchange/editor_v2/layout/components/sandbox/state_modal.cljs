(ns webchange.editor-v2.layout.components.sandbox.state-modal
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.sandbox.state :as parent-state]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:modal-state])
       (parent-state/path-to-db)
       (vec)))

(def modal-state-path (path-to-db []))

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in modal-state-path)
        boolean)))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path true)
     :dispatch [::parent-state/reset]}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))
