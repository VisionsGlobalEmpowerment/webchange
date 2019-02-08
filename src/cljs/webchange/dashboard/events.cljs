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
  ::show-manage-classes
  (fn [{:keys [db]} _]
    {:dispatch-n (list [::load-classes]
                       [::set-main-content :manage-classes])}))

(re-frame/reg-event-fx
  ::load-classes
  (fn [{:keys [db]} _]
    {:db (-> db
             (assoc-in [:loading :classes] true))
     :http-xhrio {:method          :get
                  :uri             (str "/api/classes")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-classes-success]
                  :on-failure      [:api-request-error :classes]}}))

(re-frame/reg-event-fx
  ::load-classes-success
  (fn [{:keys [db]} [_ result]]
    {:db (assoc-in db [:dashboard :classes] (:classes result))
     :dispatch-n (list [:complete-request :classes])}))

(re-frame/reg-event-fx
  ::show-manage-students
  (fn [{:keys [db]} _]
    {:dispatch-n (list [::load-students]
                       [::set-main-content :manage-students])}))

(re-frame/reg-event-fx
  ::load-students
  (fn [{:keys [db]} _]
    {:db (-> db
             (assoc-in [:loading :students] true))
     :http-xhrio {:method          :get
                  :uri             (str "/api/students")
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::load-students-success]
                  :on-failure      [:api-request-error :students]}}))

(re-frame/reg-event-fx
  ::load-students-success
  (fn [{:keys [db]} [_ result]]
    {:db (assoc-in db [:dashboard :students] (:students result))
     :dispatch-n (list [:complete-request :students])}))