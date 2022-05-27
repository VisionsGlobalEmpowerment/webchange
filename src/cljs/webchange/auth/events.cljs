(ns webchange.auth.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
    [day8.re-frame.http-fx]
    [ajax.core :refer [json-request-format json-response-format]]
    [webchange.events :as events]
    [webchange.interpreter.events :as ie]))

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
    {:db (-> db
             (assoc-in [:current-course] (:course-slug user))
             (update-in [:user] merge user))
     :dispatch-n (list [:complete-request :student-login]
                       (if redirect
                         [::events/redirect redirect]
                         [::ie/open-student-dashboard]))}))

(re-frame/reg-event-fx
  ::student-login-failure
  [(re-frame/inject-cofx :redirect-param)]
  (fn [{:keys [_]} []]
    {:dispatch [:api-request-error :student-login]}))
