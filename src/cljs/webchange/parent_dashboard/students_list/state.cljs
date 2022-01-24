(ns webchange.parent-dashboard.students-list.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]
    [webchange.parent-dashboard.state :as parent-state]
    [webchange.state.warehouse :as warehouse]
    [webchange.interpreter.events :as ie]))

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
    {:dispatch-n [[::students-loading-start]
                  [::warehouse/load-parent-students
                   {:on-success [::students-loading-finish]
                    :on-failure [::students-loading-fail]}]]}))

(re-frame/reg-event-fx
  ::students-loading-start
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db students-list-path {:loading? true
                                          :data     []})}))

(re-frame/reg-event-fx
  ::students-loading-finish
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db students-list-path {:loading? false
                                          :data     data})}))

(re-frame/reg-event-fx
  ::students-loading-fail
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db students-list-path {:loading? false})}))

(re-frame/reg-sub
  ::students-list-data
  (fn [db]
    (get-in db students-list-path)))

(re-frame/reg-sub
  ::students-loading?
  (fn []
    (re-frame/subscribe [::students-list-data]))
  (fn [data]
    (get data :loading? false)))

(re-frame/reg-sub
  ::students-list
  (fn []
    (re-frame/subscribe [::students-list-data]))
  (fn [data]
    (get data :data)))

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
  ::delete-student
  (fn [{:keys [db]} [_]]
    (let [student-id (get-current-student db)]
      {:dispatch-n [[::set-submit-status {:loading? true}]
                    [::warehouse/delete-parent-student {:id student-id}
                     {:on-success [::delete-student-success]
                      :on-failure [::delete-student-failure]}]
                    ]})))

(re-frame/reg-event-fx
  ::delete-student-success
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::close-window]
                  [::set-submit-status {:loading? false}]
                  [::load-students]]}))

(re-frame/reg-event-fx
  ::delete-student-failure
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::set-submit-status {:loading? false}]]}))

;; Submit status

(def submit-status-path (path-to-db [:submit-status]))

(re-frame/reg-sub
  ::submit-status
  (fn [db]
    (get-in db submit-status-path {:loading? false})))

(re-frame/reg-event-fx
  ::set-submit-status
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db submit-status-path data)}))

(re-frame/reg-sub
  ::loading?
  (fn []
    (re-frame/subscribe [::submit-status]))
  (fn [status]
    (get status :loading? false)))

;; Buttons

(re-frame/reg-event-fx
  ::open-add-form
  (fn [{:keys [_]} [_]]
    {:dispatch [::events/redirect :parent-add-student]}))

(re-frame/reg-event-fx
  ::open-student-dashboard
  (fn [{:keys [_]} [_ student-id]]
    (print ::open-student-dashboard student-id)
    {}))

(re-frame/reg-event-fx
  ::play-as-student
  (fn [{:keys [db]} [_ student-id]]
    {:dispatch-n [[::set-submit-status {:loading? true}]
                  [::warehouse/login-as-parent-student {:data {:id student-id}}
                   {:on-success [::play-as-student-success]}]]}))

(re-frame/reg-event-fx
  ::play-as-student-success
  (fn [{:keys [db]} [_ user]]
    {:db (-> db
             (assoc-in [:current-course] (:course-slug user))
             (update-in [:user] merge user))
     :dispatch-n (list [::ie/open-student-course-dashboard (:course-slug user)]
                       [::set-submit-status {:loading? false}])}))
