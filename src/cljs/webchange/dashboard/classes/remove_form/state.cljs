(ns webchange.dashboard.classes.remove-form.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.dashboard.classes.state :as parent-state]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:remove-form])
       (parent-state/path-to-db)))

(re-frame/reg-event-fx
  ::open-remove-class-window
  (fn [{:keys [_]} [_ class-id handlers]]
    {:dispatch-n [[::set-form-state {:class-id     class-id
                                     :handlers     handlers
                                     :loading?     true
                                     :window-open? true}]
                  [::warehouse/load-class-students
                   {:class-id class-id}
                   {:on-success [::load-class-students-success]}]]}))

(re-frame/reg-event-fx
  ::load-class-students-success
  (fn [{:keys [_]} [_ {:keys [students]}]]
    {:dispatch [::update-form-state {:loading?      false
                                     :has-students? (-> students empty? not)}]}))

;; Form state

(def form-state-path (path-to-db [:form-state]))

(defn get-form-state
  [db]
  (get-in db form-state-path))

(defn get-class-id
  [db]
  (-> (get-form-state db)
      (get :class-id)))

(defn get-handlers
  [db]
  (-> (get-form-state db)
      (get :handlers)))

(re-frame/reg-sub
  ::form-state
  get-form-state)

(re-frame/reg-event-fx
  ::set-form-state
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db form-state-path data)}))

(re-frame/reg-event-fx
  ::update-form-state
  (fn [{:keys [db]} [_ data-patch]]
    {:db (update-in db form-state-path merge data-patch)}))

(re-frame/reg-event-fx
  ::reset-form-state
  (fn [{:keys [_]} [_]]
    {:dispatch [::set-form-state {:class-id      nil
                                  :has-students? false
                                  :loading?      false
                                  :window-open?  false}]}))

(re-frame/reg-sub
  ::window-open?
  (fn []
    [(re-frame/subscribe [::form-state])])
  (fn [[form-state]]
    (get form-state :window-open? false)))

(re-frame/reg-sub
  ::loading?
  (fn []
    [(re-frame/subscribe [::form-state])])
  (fn [[form-state]]
    (get form-state :loading? false)))

(re-frame/reg-sub
  ::has-students?
  (fn []
    [(re-frame/subscribe [::form-state])])
  (fn [[form-state]]
    (get form-state :has-students? false)))

(re-frame/reg-sub
  ::confirm-button-enabled?
  (fn []
    [(re-frame/subscribe [::loading?])
     (re-frame/subscribe [::has-students?])])
  (fn [[loading? has-students?]]
    (and (not loading?)
         (not has-students?))))

(re-frame/reg-event-fx
  ::remove-class
  (fn [{:keys [db]} [_]]
    (let [class-id (get-class-id db)]
      {:dispatch [::parent-state/delete-class
                  {:class-id class-id}
                  {:on-success [::remove-class-success]}]})))

(re-frame/reg-event-fx
  ::remove-class-success
  (fn [{:keys [db]} [_]]
    (let [{:keys [on-success]} (get-handlers db)]
      {:dispatch-n (cond-> [[::reset-form-state]]
                           (some? on-success) (conj on-success))})))

(re-frame/reg-event-fx
  ::cancel
  (fn [{:keys [_]} [_]]
    {:dispatch [::reset-form-state]}))

