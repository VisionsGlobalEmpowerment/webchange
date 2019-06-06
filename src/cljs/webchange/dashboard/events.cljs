(ns webchange.dashboard.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]))

(re-frame/reg-event-fx
  ::set-main-content
  (fn [{:keys [db]} [_ screen]]
    {:db (assoc-in db [:dashboard :current-main-content] screen)}))

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
  (fn [{:keys [db]} [_ data]]
    (let [class-id (get-in db [:dashboard :current-class-id])
          prepared-data (assoc data :class-id class-id)]
      {:db (assoc-in db [:loading :add-student] true)
       :http-xhrio {:method          :post
                    :uri             (str "/api/students")
                    :params          prepared-data
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::add-student-success]
                    :on-failure      [:api-request-error :add-student]}})))


(re-frame/reg-event-fx
  ::add-student-success
  (fn [{:keys [db]} _]
    (let [class-id (get-in db [:dashboard :current-class-id])]
      {:dispatch-n (list [:complete-request :add-student]
                         [::show-class class-id])})))

(re-frame/reg-event-fx
  ::edit-student
  (fn [{:keys [db]} [_ id data]]
    {:db (assoc-in db [:loading :edit-student] true)
     :http-xhrio {:method          :put
                  :uri             (str "/api/students/" id)
                  :params          data
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::edit-student-success]
                  :on-failure      [:api-request-error :edit-student]}}))


(re-frame/reg-event-fx
  ::edit-student-success
  (fn [{:keys [db]} _]
    (let [class-id (get-in db [:dashboard :current-class-id])]
      {:dispatch-n (list [:complete-request :edit-student]
                         [::show-class class-id])})))

(re-frame/reg-event-fx
  ::delete-student
  (fn [{:keys [db]} [_ id]]
    {:db (assoc-in db [:loading :delete-student] true)
     :http-xhrio {:method          :delete
                  :uri             (str "/api/students/" id)
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::delete-student-success]
                  :on-failure      [:api-request-error :delete-student]}}))


(re-frame/reg-event-fx
  ::delete-student-success
  ((fn [{:keys [db]} _]
     (let [class-id (get-in db [:dashboard :current-class-id])]
       {:dispatch-n (list [:complete-request :delete-student]
                          [::show-class class-id])}))))

(re-frame/reg-event-fx
  ::show-edit-student-form
  (fn [{:keys [db]} [_ id]]
    {:db (assoc-in db [:dashboard :current-student-id] id)
     :dispatch-n (list [::load-student id]
                       [::set-main-content :edit-student-form])}))
