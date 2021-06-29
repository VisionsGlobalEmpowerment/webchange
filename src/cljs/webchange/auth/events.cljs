(ns webchange.auth.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.events :as events]
    [webchange.interpreter.events :as ie]
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

(defn get-url-params
  "Parse URL parameters into a hashmap"
  []
  (let [location (-> (.-location js/window) (.-search) (.split #"\?") last (.split #"\&"))]
    (into {} (for [[k v] (map #(.split % #"=") location)]
               [(keyword k) v]))))

(re-frame/reg-cofx
  :redirect-param
  (fn [coeffects _]
    (assoc coeffects :redirect (:redirect (get-url-params)))))

(re-frame/reg-event-fx
  ::login-success
  [(re-frame/inject-cofx :redirect-param)]
  (fn [{:keys [db redirect]} [_ user]]
    (let [redirect-to (or redirect :dashboard)]
      {:db (update-in db [:user] merge user)
       :dispatch-n (list [:complete-request :login]
                         [::events/redirect redirect-to])})))

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
  (fn [{:keys [db]} [_ user]]
    {:db (update-in db [:user] merge user)
     :dispatch-n (list [:complete-request :register-user]
                       [::events/redirect :login])}))

(re-frame/reg-event-fx
  ::student-login
  (fn [{:keys [db]} [_ access-code]]
    (let [current-school (:school-id db)
          credentials {:school-id current-school :access-code access-code}]
      {:db         (assoc-in db [:loading :student-login] true)
       :http-xhrio {:method          :post
                    :uri             "/api/students/login"
                    :params          credentials
                    :format          (json-request-format)
                    :response-format (json-response-format {:keywords? true})
                    :on-success      [::student-login-success]
                    :on-failure      [::student-login-failure]}})))

(re-frame/reg-event-fx
  ::student-login-success
  [(re-frame/inject-cofx :redirect-param)]
  (fn [{:keys [db redirect]} [_ user]]
    {:db (update-in db [:user] merge user)
     :dispatch-n (list [:complete-request :student-login]
                       (if redirect
                         [::events/redirect redirect]
                         [::ie/open-student-dashboard]))}))

(re-frame/reg-event-fx
  ::student-login-failure
  [(re-frame/inject-cofx :redirect-param)]
  (fn [{:keys [_]} []]
    {:dispatch [:api-request-error :student-login]}))
