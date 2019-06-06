(ns webchange.dashboard.classes.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.dashboard.events :as dashboard-events]))

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
  ::show-manage-classes
  (fn [{:keys [db]} _]
    {:dispatch-n (list [::dashboard-events/set-main-content :manage-classes])}))

(re-frame/reg-event-fx
  ::load-classes-success
  (fn [{:keys [db]} [_ result]]
    {:db (assoc-in db [:dashboard :classes] (:classes result))
     :dispatch-n (list [:complete-request :classes])}))

(re-frame/reg-event-fx
  ::add-class
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db [:loading :add-class] true)
     :http-xhrio {:method          :post
                  :uri             (str "/api/classes")
                  :params          data
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::add-class-success]
                  :on-failure      [:api-request-error :add-class]}}))


(re-frame/reg-event-fx
  ::add-class-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :add-class]
                       [::load-classes])}))

(re-frame/reg-event-fx
  ::edit-class
  (fn [{:keys [db]} [_ id data]]
    (println (str "::edit-class " id " " data))
    {:db (assoc-in db [:loading :edit-class] true)
     :http-xhrio {:method          :put
                  :uri             (str "/api/classes/" id)
                  :params          data
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::edit-class-success]
                  :on-failure      [:api-request-error :edit-class]}}))


(re-frame/reg-event-fx
  ::edit-class-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :edit-class]
                       [::load-classes]
                       [::show-manage-classes])}))

(re-frame/reg-event-fx
  ::delete-class
  (fn [{:keys [db]} [_ id]]
    {:db (assoc-in db [:loading :delete-class] true)
     :http-xhrio {:method          :delete
                  :uri             (str "/api/classes/" id)
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::delete-class-success]
                  :on-failure      [:api-request-error :delete-class]}}))


(re-frame/reg-event-fx
  ::delete-class-success
  (fn [_ _]
    {:dispatch-n (list [:complete-request :delete-class]
                       [::load-classes])}))

(re-frame/reg-event-fx
  ::show-edit-class-form
  (fn [{:keys [db]} [_ class-id]]
    {:db (assoc-in db [:dashboard :current-class-id] class-id)
     :dispatch [::dashboard-events/set-main-content :edit-class-form]}))

(re-frame/reg-event-fx
  ::show-class
  (fn [{:keys [db]} [_ class-id]]
    {:db (assoc-in db [:dashboard :current-class-id] class-id)
     :dispatch-n (list [::load-students class-id]
                       [::dashboard-events/set-main-content :manage-students])}))
