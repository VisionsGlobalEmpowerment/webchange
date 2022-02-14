(ns webchange.dashboard.classes.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.state :as parent-state]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:classes-page])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_]]
    {:dispatch [::load-classes]}))

;; Classes

(re-frame/reg-event-fx
  ::load-classes
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse/load-classes {:on-success [::load-classes-success]}]}))

(re-frame/reg-event-fx
  ::load-classes-success
  (fn [{:keys [_]} [_ {:keys [classes]}]]
    {:dispatch [::set-classes classes]}))

(def classes-path (path-to-db [:classes]))

(defn get-classes
  [db]
  (get-in db classes-path))

(re-frame/reg-sub
  ::classes
  get-classes)

(re-frame/reg-event-fx
  ::set-classes
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db classes-path data)}))

(re-frame/reg-event-fx
  ::add-class
  (fn [{:keys [_]} [_ {:keys [data]} handlers]]
    {:dispatch [::warehouse/create-class
                {:data data}
                (-> handlers
                    (assoc :on-success [::update-classes-list handlers]))]}))

(re-frame/reg-event-fx
  ::edit-class
  (fn [{:keys [_]} [_ {:keys [id data]} handlers]]
    {:dispatch [::warehouse/save-class
                {:class-id id
                 :data     data}
                (-> handlers
                    (assoc :on-success [::update-classes-list handlers]))]}))

(re-frame/reg-event-fx
  ::delete-class
  (fn [{:keys [_]} [_ {:keys [class-id]} handlers]]
    {:dispatch [::warehouse/delete-class
                {:class-id class-id}
                (-> handlers
                    (assoc :on-success [::update-classes-list handlers]))]}))

(re-frame/reg-event-fx
  ::update-classes-list
  (fn [{:keys [_]} [_ {:keys [on-success]}]]
    {:dispatch-n (cond-> [[::load-classes]]
                         (some? on-success) (conj on-success))}))
