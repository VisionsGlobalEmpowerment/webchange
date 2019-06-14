(ns webchange.dashboard.students.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]))

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
  ::add-student
  (fn [{:keys [db]} [_ class-id data]]
    (let [prepared-data (assoc data :class-id class-id)]
      {:db (assoc-in db [:loading :add-student] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/students")
                    :params          prepared-data
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::add-student-success class-id]
                    :on-failure      [:api-request-error :add-student]}})))

(re-frame/reg-event-fx
  ::add-student-success
  (fn [{:keys [db]} [_ class-id]]
    {:dispatch-n (list [:complete-request :add-student]
                       [::load-students class-id])}))

(re-frame/reg-event-fx
  ::edit-student
  (fn [{:keys [db]} [_ class-id student-id data]]
    {:db (assoc-in db [:loading :edit-student] true)
     :http-xhrio {:method          :put
                  :uri             (str "/api/students/" student-id)
                  :params          data
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::edit-student-success class-id]
                  :on-failure      [:api-request-error :edit-student]}}))

(re-frame/reg-event-fx
  ::edit-student-success
  (fn [_ [_ class-id]]
    {:dispatch-n (list [:complete-request :edit-student]
                       [::load-students class-id])}))

(re-frame/reg-event-fx
  ::delete-student
  (fn [{:keys [db]} [_ class-id id]]
    (println (str "Delete student " class-id "|" id))
    {:db (assoc-in db [:loading :delete-student] true)
     :http-xhrio {:method          :delete
                  :uri             (str "/api/students/" id)
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::delete-student-success class-id]
                  :on-failure      [:api-request-error :delete-student]}}))

(re-frame/reg-event-fx
  ::delete-student-success
  (fn [_ [_ class-id]]
    (println (str "::delete-student-success " class-id))
    {:dispatch-n (list [:complete-request :delete-student]
                       [::load-students class-id])}))

(re-frame/reg-event-fx
  ::show-add-student-form
  (fn [{:keys [db]} _]
    {:dispatch-n (list [::generate-access-code]
                       [::open-student-modal :add])}))

(re-frame/reg-event-fx
  ::show-edit-student-form
  (fn [{:keys [db]} [_ id]]
    {:db (assoc-in db [:dashboard :current-student-id] id)
     :dispatch-n (list [::load-student id]
                       [::reset-access-code]
                       [::open-student-modal :edit])}))

(re-frame/reg-event-fx
  ::generate-access-code
  (fn [{:keys [db]} _]
    (let []
      {:db (assoc-in db [:loading :generate-access-code] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/next-access-code")
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::generate-access-code-success]
                    :on-failure      [:api-request-error :generate-access-code]}})))


(re-frame/reg-event-fx
  ::generate-access-code-success
  (fn [{:keys [db]} [_ {:keys [access-code]}]]
    {:db (assoc-in db [:dashboard :access-code] access-code)
     :dispatch [:complete-request :generate-access-code]}))

(re-frame/reg-event-fx
  ::reset-access-code
  (fn [{:keys [db]} _]
    {:db (update-in db [:dashboard] dissoc :access-code)}))

(re-frame/reg-event-fx
  ::close-student-modal
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:dashboard :student-modal-state] nil)}))

(re-frame/reg-event-fx
  ::open-student-modal
  (fn [{:keys [db]} [_ state]]
    {:db (assoc-in db [:dashboard :student-modal-state] state)}))