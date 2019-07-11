(ns webchange.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as re-frame]
            [webchange.events :as events]
            [webchange.interpreter.events :as ie]
            [webchange.dashboard.classes.events :as classes-events]
            [webchange.dashboard.students.events :as students-events]))

(def routes ["/" {""                  :home
                  "login"             :login
                  "student-login"     :student-login
                  "register"          :register-user
                  "courses"           {["/" :id]           :course
                                       ["/" :id "/editor"] :course-editor}
                  "student-dashboard" {""          :student-dashboard
                                       "/finished" :finished-activities}
                  "dashboard"         {[""]                                             :dashboard
                                       ["/classes"]                                     :dashboard-classes
                                       ["/classes/" :class-id]                          :dashboard-class-profile
                                       ["/classes/" :class-id "/students"]              :dashboard-students
                                       ["/classes/" :class-id "/students/" :student-id] :dashboard-student-profile}}])


(defn- parse-url [url]
  (bidi/match-route routes url))

(def default-course-name "test")

(defn- dispatch-route [{:keys [handler route-params] :as params}]
  (re-frame/dispatch [::events/set-active-route params])
  (case handler
    :course (re-frame/dispatch [::ie/start-course (:id route-params)])
    :student-dashboard (re-frame/dispatch [::ie/start-course default-course-name])
    :dashboard-class-profile (re-frame/dispatch [::classes-events/load-class-profile (:class-id route-params) default-course-name])
    :dashboard-student-profile (re-frame/dispatch [::students-events/open-student-profile (:student-id route-params) default-course-name])
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
