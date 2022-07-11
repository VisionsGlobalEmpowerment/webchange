(ns webchange.parent.pages.students.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.events :as interpreter]
    [webchange.parent.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def path-to-db :page/students)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

;; students

(def students-loading-key :students-loading)

(defn- set-students-loading
  [db value]
  (assoc db students-loading-key value))

(re-frame/reg-sub
  ::students-loading
  :<- [path-to-db]
  #(get % students-loading-key true))

(def students-key :students)

(defn- set-students
  [db value]
  (assoc db students-key value))

(re-frame/reg-sub
  ::students
  :<- [path-to-db]
  #(get % students-key))

;;

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [_]} [_]]
    {:dispatch [::load-students]}))

(re-frame/reg-event-fx
  ::load-students
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db       (set-students-loading db true)
     :dispatch [::warehouse/load-parent-students
                {:on-success [::students-loading-success]
                 :on-failure [::students-loading-failure]}]}))

(re-frame/reg-event-fx
  ::students-loading-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ data]]
    {:db (-> db
             (set-students-loading false)
             (set-students data))}))

(re-frame/reg-event-fx
  ::students-loading-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-students-loading false))}))

;; play as student

(def login-as-student-key :login-as-student)

(defn- set-login-as-student
  [db value]
  (assoc db login-as-student-key value))

(re-frame/reg-sub
  ::login-as-student?
  :<- [path-to-db]
  #(get % login-as-student-key false))

(re-frame/reg-event-fx
  ::play-as-student
  (fn [{:keys [db]} [_ student-id]]
    {:db       (set-login-as-student db true)
     :dispatch [::warehouse/login-as-parent-student
                {:data {:id student-id}}
                {:on-success [::play-as-student-success]
                 :on-failure [::play-as-student-failure]}]}))

(re-frame/reg-event-fx
  ::play-as-student-success
  (fn [{:keys [db]} [_ user]]
    {:db       (-> db
                   (set-login-as-student false)
                   (assoc-in [:current-course] (:course-slug user))
                   (assoc-in [:user] user))
     :dispatch [::interpreter/open-student-course-dashboard (:course-slug user)]}))

(re-frame/reg-event-fx
  ::play-as-student-failure
  (fn [{:keys [db]} [_]]
    {:db (-> db (set-login-as-student false))}))

;; remove

(def remove-window-state-key :remove-window-state)

(def remove-window-default-state {:open?        false
                                  :in-progress? false
                                  :done?        false
                                  :student-id   nil})

(defn- update-remove-window-state
  [db data-patch]
  (update db remove-window-state-key merge data-patch))

(re-frame/reg-sub
  ::remove-window-state
  :<- [path-to-db]
  #(get % remove-window-state-key remove-window-default-state))

(re-frame/reg-event-fx
  ::open-remove-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    {:db (update-remove-window-state db {:open?      true
                                         :student-id student-id})}))

(re-frame/reg-event-fx
  ::close-remove-window
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-window-state db remove-window-default-state)}))

(re-frame/reg-event-fx
  ::remove-student
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ student-id]]
    {:db       (update-remove-window-state db {:in-progress? true})
     :dispatch [::warehouse/delete-parent-student {:id student-id}
                {:on-success [::remove-student-success]
                 :on-failure [::remove-student-failure]}]}))

(re-frame/reg-event-fx
  ::remove-student-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-window-state db {:in-progress? false
                                         :done?        true})}))

(re-frame/reg-event-fx
  ::remove-student-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (update-remove-window-state db {:in-progress? false})}))

;; add student

(re-frame/reg-event-fx
  ::open-add-student-page
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:dispatch [::routes/redirect :student-add]}))
