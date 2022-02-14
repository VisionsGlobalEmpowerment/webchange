(ns webchange.dashboard.classes.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]))

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
  ::set-current-class
  (fn [{:keys [db]} [_ class-id]]
    {:db (assoc-in db [:dashboard :current-class-id] class-id)}))
