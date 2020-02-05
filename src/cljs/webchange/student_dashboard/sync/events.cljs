(ns webchange.student-dashboard.sync.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]))

(re-frame/reg-event-fx
  ::open-sync-list
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db [:sync-resources :list-open] true)}))

(re-frame/reg-event-fx
  ::close-sync-list
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db [:sync-resources :list-open] false)}))

(re-frame/reg-event-fx
  ::load-scenes
  (fn [{:keys [db]} _]
    (let [current-course (:current-course db)]
      {:db (-> db (assoc-in [:sync-resources :scenes :loading] true))
       :http-xhrio {:method          :get
                    :uri             (str "/api/resources/game-app/" current-course "/scenes")
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::load-scenes-success]
                    :on-failure      [::load-scenes-failed]}})))

(re-frame/reg-event-fx
  ::load-scenes-success
  (fn [{:keys [db]} [_ result]]
    {:db (-> db
             (assoc-in [:sync-resources :scenes :loading] false)
             (assoc-in [:sync-resources :scenes :data] result))}))

(re-frame/reg-event-fx
  ::load-scenes-failed
  (fn [{:keys [db]} [_ _]]
    {:db (-> db
             (assoc-in [:sync-resources :scenes :loading] false))}))
