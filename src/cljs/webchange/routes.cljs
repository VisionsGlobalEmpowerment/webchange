(ns webchange.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as re-frame]
            [webchange.events :as events]
            [webchange.subs :as subs]
            [webchange.interpreter.events :as ie]
            [webchange.dashboard.events :as dashboard-events]))

(def routes ["/" {""                  :home
                  "login"             :login
                  "student-login"     :student-login
                  "register"          :register-user
                  "courses"           {["/" :id]           :course
                                       ["/" :id "/editor"] :course-editor
                                       ["/" :id "/editor-v2"] :course-editor-v2
                                       ["/" :course-id "/editor-v2/concepts/" :concept-id] :course-editor-v2-concept
                                       ["/" :course-id "/editor-v2/add-concept"] :course-editor-v2-add-concept
                                       ["/" :id "/editor-v2/" :scene-id] :course-editor-v2-scene
                                       ["/" :id "/dashboard"] :student-course-dashboard
                                       ["/" :id "/dashboard/finished"] :finished-activities}
                  "student-dashboard" {""          :student-dashboard}
                  "dashboard"         {[""]                                             :dashboard
                                       ["/classes"]                                     :dashboard-classes
                                       ["/classes/" :class-id]                          :dashboard-class-profile
                                       ["/classes/" :class-id "/students"]              :dashboard-students
                                       ["/classes/" :class-id "/students/" :student-id] :dashboard-student-profile}}])


(defn- parse-url [url]
  (bidi/match-route routes url))

(defn- dispatch-route [{:keys [handler route-params] :as params}]
  (let [current-course @(re-frame/subscribe [::subs/current-course])]
    (re-frame/dispatch [::events/set-active-route params])
    (case handler
      :course (re-frame/dispatch [::ie/start-course (:id route-params)])
      :student-course-dashboard (do (re-frame/dispatch [::ie/start-course (:id route-params)]) (re-frame/dispatch [::ie/clear-current-scene]))
      :finished-activities (do (re-frame/dispatch [::ie/start-course (:id route-params)]) (re-frame/dispatch [::ie/clear-current-scene]))
      :student-dashboard (do (re-frame/dispatch [::ie/start-course current-course]) (re-frame/dispatch [::ie/clear-current-scene]))
      :dashboard-class-profile (re-frame/dispatch [::dashboard-events/open-class-profile (:class-id route-params) current-course])
      :dashboard-student-profile (re-frame/dispatch [::dashboard-events/open-student-profile (:student-id route-params) current-course])
      :dashboard-classes (re-frame/dispatch [::dashboard-events/open-classes])
      :dashboard-students (re-frame/dispatch [::dashboard-events/open-students (:class-id route-params)])
      nil)))

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
