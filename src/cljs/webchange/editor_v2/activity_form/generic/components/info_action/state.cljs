(ns webchange.editor-v2.activity-form.generic.components.info-action.state
  (:require
   [re-frame.core :as re-frame]))

(def modal-state-path [:editor-v2 :info-action-modal :state])

;; Subs
(re-frame/reg-sub
 ::modal-state
 (fn [db]
   (-> db
       (get-in modal-state-path)
       boolean)))

;; Events
(re-frame/reg-event-fx
 ::open
 (fn [{:keys [db]} [_]]
   {:db (-> db
            (assoc-in modal-state-path true))}))

(re-frame/reg-event-fx
 ::close
 (fn [{:keys [db]} [_]]
   {:db (assoc-in db modal-state-path false)}))

(defn open-info-window
  []
  (re-frame/dispatch [::open]))
