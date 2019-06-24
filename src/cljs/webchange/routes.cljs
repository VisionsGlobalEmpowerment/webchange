(ns webchange.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as re-frame]
            [webchange.events :as events]
            [webchange.interpreter.events :as ie]))

(def routes ["/" {""                  :home
                  "login"             :login
                  "student-login"     :student-login
                  "register"          :register-user
                  "courses"           {["/" :id]           :course
                                       ["/" :id "/editor"] :course-editor}
                  "student-dashboard" {""          :student-dashboard
                                       "/finished" :finished-activities}
                  "dashboard"         {[""]                                :dashboard
                                       ["/classes"]                        :dashboard-classes
                                       ["/classes/" :class-id "/students"] :dashboard-students}}])


(defn- parse-url [url]
  (bidi/match-route routes url))

(defn- dispatch-route [{:keys [handler route-params] :as params}]
  (re-frame/dispatch [::events/set-active-route params])
  (case handler
    :course (re-frame/dispatch [::ie/start-course (:id route-params)])
    :student-dashboard (re-frame/dispatch [::ie/start-course "test"])
    nil))

(def history
  (pushy/pushy dispatch-route parse-url))

(defn start! []
  (pushy/start! history))

(def url-for (partial bidi/path-for routes))

(defn redirect-to [& args]
  (let [key (first args)
        path (if (= (type key) Keyword)
               (apply url-for (vec args))
               key)]
    (pushy/set-token! history path)))

(re-frame/reg-fx
  :redirect
  (fn [args]
    (apply redirect-to args)))
