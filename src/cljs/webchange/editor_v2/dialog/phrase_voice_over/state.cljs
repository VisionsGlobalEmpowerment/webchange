(ns webchange.editor-v2.dialog.phrase-voice-over.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.state :as parent-state]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:phrase-voice-over])
       (parent-state/path-to-db)))

;; Modal state

(def modal-state-path (path-to-db [:modal-state]))

(re-frame/reg-sub
  ::modal-open?
  (fn [db]
    (get-in db modal-state-path false)))

(re-frame/reg-event-fx
  ::open-modal
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path true)}))

(re-frame/reg-event-fx
  ::close-modal
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))
