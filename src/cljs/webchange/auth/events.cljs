(ns webchange.auth.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]
    ))

(re-frame/reg-event-fx
  ::login
  (fn [{:keys [db]} [_ credentials]]
    {:db         (assoc-in db [:loading :login] true)
     :http-xhrio {:method          :post
                  :uri             "/api/users/login"
                  :params          {:user credentials}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::login-success]
                  :on-failure      [:api-request-error :login]}}))

(re-frame/reg-event-fx
  ::login-success
  (fn [{:keys [db]} [_ {user :user}]]
    {:db (update-in db [:user] merge user)
     :dispatch-n (list [:complete-request :login]
                       [:set-active-page {:page :home}])}))

(re-frame/reg-event-fx
  ::register-user
  (fn [{:keys [db]} [_ user-data]]
    {:db         (assoc-in db [:loading :register-user] true)
     :http-xhrio {:method          :post
                  :uri             "/api/users/register-user"
                  :params          {:user user-data}
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::register-user-success]
                  :on-failure      [:api-request-error :register-user]}}))

(re-frame/reg-event-fx
  ::register-user-success
  (fn [{:keys [db]} [_ {user :user}]]
    {:db (update-in db [:user] merge user)
     :dispatch-n (list [:complete-request :register-user]
                       [:set-active-page {:page :login}])}))