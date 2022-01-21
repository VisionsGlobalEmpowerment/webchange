(ns webchange.parent-dashboard.students-list.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]
    [webchange.parent-dashboard.state :as parent-state]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:add-student-form])
       (parent-state/path-to-db)))

;; Students

(def students-list-path (path-to-db [:students-list]))

(re-frame/reg-event-fx
  ::load-students
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse/load-parent-students
                {:on-success [::set-students-list]}]}))

(re-frame/reg-event-fx
  ::set-students-list
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db students-list-path data)}))

(re-frame/reg-sub
  ::students-list
  (fn [db]
    (get-in db students-list-path (->> {:name   "Ivan"
                                        :level  1
                                        :lesson 1}
                                       (repeat 6)
                                       (map-indexed (fn [idx data]
                                                      (assoc data :id idx)))))))

;; Confirm delete

(def window-state-path (path-to-db [:window-state]))

(defn- get-window-state
  [db]
  (get-in db window-state-path {:open?      false
                                :student-id nil}))

(re-frame/reg-sub
  ::window-state
  get-window-state)

(re-frame/reg-sub
  ::window-open?
  (fn []
    (re-frame/subscribe [::window-state]))
  (fn [window-state]
    (get window-state :open?)))

(re-frame/reg-event-fx
  ::open-window
  (fn [{:keys [db]} [_]]
    {:db (update-in db window-state-path assoc :open? true)}))

(re-frame/reg-event-fx
  ::close-window
  (fn [{:keys [db]} [_]]
    {:db (update-in db window-state-path assoc :open? false)}))

(re-frame/reg-event-fx
  ::set-current-student
  (fn [{:keys [db]} [_ id]]
    {:db (update-in db window-state-path assoc :student-id id)}))

(defn- get-current-student
  [db]
  (-> (get-window-state db)
      (get :student-id)))

(re-frame/reg-event-fx
  ::open-confirm-delete-student
  (fn [{:keys [_]} [_ student-id]]
    {:dispatch-n [[::open-window]
                  [::set-current-student student-id]]}))

(re-frame/reg-event-fx
  ::confirm-delete-student
  (fn [{:keys [db]} [_]]
    (let [student-id (get-current-student db)]
      {:dispatch-n [[::close-window]
                    [::delete-student student-id]]})))

;; Buttons

(re-frame/reg-event-fx
  ::open-add-form
  (fn [{:keys [_]} [_]]
    {:dispatch [::events/redirect :parent-add-student]}))

(re-frame/reg-event-fx
  ::open-student-dashboard
  (fn [{:keys [_]} [_ student-id]]
    (print "::open-student-dashboard" student-id)
    {}))

(re-frame/reg-event-fx
  ::delete-student
  (fn [{:keys [_]} [_ student-id]]
    {:dispatch [::warehouse/delete-parent-student {:id student-id}
                {:on-success [::load-students]}]}))
