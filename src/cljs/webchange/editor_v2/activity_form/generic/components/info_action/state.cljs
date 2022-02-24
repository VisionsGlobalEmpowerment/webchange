(ns webchange.editor-v2.activity-form.generic.components.info-action.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.warehouse :as warehouse]))

(def modal-state-path [:editor-v2 :info-action-modal :state])
(def book-categories-path [:editor-v2 :info-action-modal :book-categories])

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
    {:db       (assoc-in db modal-state-path true)
     :dispatch [::warehouse/load-book-categories {:on-success [::set-book-categories]}]}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

(defn open-info-window
  []
  (re-frame/dispatch [::open]))

;; Book categories

(fn [db]
  (get-in db book-categories-path))

(re-frame/reg-sub
  ::book-categories
  (fn [db]
    (get-in db book-categories-path)))

(re-frame/reg-event-fx
  ::set-book-categories
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db book-categories-path data)}))
