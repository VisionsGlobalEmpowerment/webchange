(ns webchange.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as re-frame]
            [webchange.events :as events]))

(def routes ["/" {""                  :home
                  "login"             :login
                  "student-login"     :student-login
                  "register"          :register-user
                  "courses"           {["/" :id]           :course
                                       ["/" :id "/editor"] :course-editor}
                  "student-dashboard" :student-dashboard
                  "dashboard"         :dashboard}])

(defn- parse-url [url]
  (bidi/match-route routes url))

(defn- dispatch-route [params]
  (re-frame/dispatch [::events/set-active-route params]))

(def history
  (pushy/pushy dispatch-route parse-url))

(defn start! []
  (pushy/start! history))

(def url-for (partial bidi/path-for routes))

(defn redirect-to [& args]
  (let [key (first args)
        path (if (= (type key) Keyword)
               (apply url-for args)
               key)]
    (pushy/set-token! history path)))

(re-frame/reg-fx
  :redirect
  (fn [args]
    (apply redirect-to args)))
