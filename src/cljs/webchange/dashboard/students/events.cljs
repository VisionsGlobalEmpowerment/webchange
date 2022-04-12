(ns webchange.dashboard.students.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.dashboard.events-utils :refer [when-valid clear-errors]]
    [webchange.validation.specs.student :as spec]
    [webchange.state.warehouse :as warehouse]))

(re-frame/reg-event-fx
  ::load-students
  (fn [{:keys [db]} [_ class-id]]
    {:db (-> db
             (assoc-in [:loading :students] true))
     :http-xhrio {:method          :get
                  :uri             (str "/api/classes/" class-id "/students")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-students-success]
                  :on-failure      [:api-request-error :students]}}))

(re-frame/reg-event-fx
  ::load-students-success
  (fn [{:keys [db]} [_ result]]
    {:db (assoc-in db [:dashboard :students (:class-id result)] (:students result))
     :dispatch-n (list [:complete-request :students])}))

(re-frame/reg-event-fx
  ::load-unassigned-students
  (fn [{:keys [db]} _]
    {:db (-> db
             (assoc-in [:loading :unassigned-students] true))
     :http-xhrio {:method          :get
                  :uri             (str "/api/unassigned-students")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-unassigned-students-success]
                  :on-failure      [:api-request-error :unassigned-students]}}))

(re-frame/reg-event-fx
  ::load-unassigned-students-success
  (fn [{:keys [db]} [_ result]]
    {:db (assoc-in db [:dashboard :unassigned-students] (:students result))
     :dispatch-n (list [:complete-request :unassigned-students])}))

(re-frame/reg-event-fx
  ::load-student
  (fn [{:keys [db]} [_ id]]
    {:db (-> db
             (assoc-in [:loading :student] true))
     :http-xhrio {:method          :get
                  :uri             (str "/api/students/" id)
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-student-success]
                  :on-failure      [:api-request-error :student]}}))

(re-frame/reg-event-fx
  ::load-student-success
  (fn [{:keys [db]} [_ result]]
    {:db (assoc-in db [:dashboard :current-student] (:student result))
     :dispatch-n (list [:complete-request :student])}))

(re-frame/reg-event-fx
  ::remove-student-from-class
  (fn [{:keys [db]} [_ class-id student-id]]
    {:db (assoc-in db [:loading :remove-student-from-class] true)
     :http-xhrio {:method          :delete
                  :uri             (str "/api/students/" student-id "/class")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::remove-student-from-class-success class-id]
                  :on-failure      [:api-request-error :remove-student-from-class]}}))

(re-frame/reg-event-fx
  ::remove-student-from-class-success
  (fn [{:keys [db]} [_ class-id]]
    {:db (assoc-in db [:dashboard :current-student] nil)
     :dispatch-n (list [:complete-request :remove-student-from-class]
                       [::load-unassigned-students]
                       [::load-students class-id])}))

(re-frame/reg-event-fx
  ::delete-student
  (fn [{:keys [db]} [_ student-id]]
    {:db (assoc-in db [:loading :delete-student] true)
     :http-xhrio {:method          :delete
                  :uri             (str "/api/students/" student-id)
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::delete-student-success]
                  :on-failure      [:api-request-error :delete-student]}}))

(re-frame/reg-event-fx
  ::delete-student-success
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:dashboard :current-student] nil)
     :dispatch-n (list [:complete-request :delete-student]
                       [::load-unassigned-students])}))

(re-frame/reg-event-fx
  ::complete-student-success
  (fn [{:keys [db]} _]
    {:dispatch-n (list [:complete-request :complete-student-progress])}))

(re-frame/reg-event-fx
  ::set-current-student
  (fn [{:keys [db]} [_ student-id]]
    {:db (assoc-in db [:dashboard :current-student-id] student-id)}))

(re-frame/reg-event-fx
  ::set-student-profile
  (fn [{:keys [db]} [_ student-profile]]
    {:db (assoc-in db [:dashboard :student-profile] student-profile)}))

(re-frame/reg-event-fx
  ::show-remove-from-class-form
  (fn [{:keys [db]} [_ student-id]]
    {:db (assoc-in db [:dashboard :current-student-id] student-id)
     :dispatch-n (list
                   [::load-student student-id]
                   [::open-remove-from-class-modal])}))

(re-frame/reg-event-fx
  ::show-complete-form
  (fn [{:keys [db]} [_ student-id]]
    {:db (assoc-in db [:dashboard :current-student-id] student-id)
     :dispatch-n (list
                   [::load-student student-id]
                   [::open-complete-modal])}))

(re-frame/reg-event-fx
  ::open-remove-from-class-modal
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:dashboard :remove-student-from-class-modal-state] true)}))

(re-frame/reg-event-fx
  ::open-complete-modal
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:dashboard :complete-student-modal-state] true)}))

(re-frame/reg-event-fx
  ::close-complete-modal
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:dashboard :complete-student-modal-state] nil)}))

(re-frame/reg-event-fx
  ::confirm-complete
  (fn [{:keys [db]} [_ student-id course-slug data]]
    {:db       (assoc-in db [:dashboard :complete-student-modal-state] nil)
     :dispatch [::warehouse/complete-student-progress
                {:student-id  student-id
                 :course-name course-slug
                 :data        data}
                {:on-success [::complete-student-success]}]}))

(re-frame/reg-event-fx
  ::close-remove-from-class-modal
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:dashboard :remove-student-from-class-modal-state] nil)}))

(re-frame/reg-event-fx
  ::confirm-remove
  (fn [{:keys [db]} [_ class-id student-id]]
    {:db (assoc-in db [:dashboard :remove-student-from-class-modal-state] nil)
     :dispatch [::remove-student-from-class class-id student-id]}))

(re-frame/reg-event-fx
  ::show-delete-form
  (fn [{:keys [db]} [_ student-id]]
    {:db (assoc-in db [:dashboard :current-student-id] student-id)
     :dispatch-n (list
                   [::load-student student-id]
                   [::open-delete-modal])}))

(re-frame/reg-event-fx
  ::open-delete-modal
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:dashboard :delete-student-modal-state] true)}))

(re-frame/reg-event-fx
  ::close-delete-modal
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:dashboard :delete-student-modal-state] nil)}))

(re-frame/reg-event-fx
  ::confirm-delete
  (fn [{:keys [db]} [_ student-id]]
    {:db (assoc-in db [:dashboard :delete-student-modal-state] nil)
     :dispatch [::delete-student student-id]}))