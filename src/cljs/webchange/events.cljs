(ns webchange.events
  (:require
   [re-frame.core :as re-frame]
   [webchange.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   [day8.re-frame.http-fx]
   [ajax.core :refer [json-request-format json-response-format]]
   [webchange.error-message.state :as error-message]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
  ::change-viewport
  (fn [db [_ value]]
    (assoc db :viewport value)))

(re-frame/reg-event-db
  ::set-loading-progress
  (fn [db [_ scene-id value]]
    (assoc-in db [:scene-loading-progress scene-id] value)))

(re-frame/reg-event-db
  ::set-scene-loaded
  (fn [db [_ scene-id value]]
    (assoc-in db [:scene-loading-complete scene-id] value)))

(re-frame/reg-event-fx
  ::login
  (fn [{:keys [db]} [_ credentials]] ;; credentials = {:email ... :password ...}
     {:db         (assoc-in db [:loading :login] true)
     :http-xhrio {:method          :post
                  :uri             "/api/users/login"
                  :params          {:user credentials}                      ;; {:user {:email ... :password ...}}
                  :format          (json-request-format)                    ;; make sure it's json
                  :response-format (json-response-format {:keywords? true}) ;; json response and all keys to keywords
                  :on-success      [::login-success]                        ;; trigger login-success
                  :on-failure      [:api-request-error :login]}}))          ;; trigger api-request-error with :login

(re-frame/reg-event-fx
  ::login-success
  (fn [{:keys [db]} [_ {user :user}]]
             {:db (update-in db [:user] merge user)
              :dispatch-n (list [:complete-request :login])}))

(re-frame/reg-event-fx
  ::init-current-school
  (fn [{:keys [db]} _]
    {:db         (assoc-in db [:loading :current-school] true)
     :http-xhrio {:method          :get
                  :uri             "/api/schools/current"
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::init-current-school-success]
                  :on-failure      [:api-request-error :current-school]}}))

(re-frame/reg-event-fx
  ::init-current-school-success
  (fn [{:keys [db]} [_ {school-id :id}]]
    {:db (assoc db :school-id school-id)
     :dispatch-n (list [:complete-request :current-school])}))

(re-frame/reg-event-fx
  ::init-current-user
  (fn [{:keys [db]} _]
    {:db         (assoc-in db [:loading :current-user] true)
     :http-xhrio {:method          :get
                  :uri             "/api/users/current"
                  :format          (json-request-format)
                  :response-format (json-response-format {:keywords? true})
                  :on-success      [::init-current-user-success]
                  :on-failure      [:api-request-error :current-user]}}))

(re-frame/reg-event-fx
  ::init-current-user-success
  (fn [{:keys [db]} [_ user]]
    {:db (update-in db [:user] merge user)
     :dispatch-n (list [:complete-request :current-user])}))

;; -- Request Handlers -----------------------------------------------------------
;;
(re-frame/reg-event-db
  :complete-request                ;; when we complete a request we need to clean up
  (fn-traced [db [_ request-type]] ;; few things so that our ui is nice and tidy
    (assoc-in db [:loading request-type] false)))

(re-frame/reg-event-fx
  :api-request-error                                                                         ;; triggered when we get request-error from the server
  (fn-traced [{:keys [db]} [_ request-type response]]                                        ;; destructure to obtain request-type and response
             {:db (assoc-in db [:errors request-type] (get-in response [:response :errors])) ;; save in db so that we can display it to the user
              :dispatch-n (list [:complete-request request-type]
                                [::error-message/show request-type (get-in response [:response :errors])])}))

(re-frame/reg-event-fx
  ::set-active-route
  (fn-traced [{:keys [db]} [_ params]]
    {:db (assoc db :active-route params)}))

(re-frame/reg-event-fx
  ::location
  (fn-traced [{:keys [db]} [_ & args]]
    {:location args}))
