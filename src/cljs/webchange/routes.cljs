(ns webchange.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as re-frame]
            [webchange.book-library.routes :as book-library-routes]
            [webchange.student-dashboard.routes :as student-dashboard-routes]
            [webchange.events :as events]
            [webchange.subs :as subs]
            [webchange.interpreter.events :as ie]))

(def routes ["/" {""                  :login
                  "student-login"     {[""]             :student-login
                                       ["/" :school-id] :school-student-login}
                  "s"                 {["/" [#"[\w-%]+" :scene-id]] :activity-sandbox}
                  "lesson-builder"    {["/" [#"[\w-%]+" :scene-id]] :lesson-builder}
                  "courses"           {["/" [#"[\w-%]+" :id]] {[""]                 :course
                                                               ["/dashboard"]       student-dashboard-routes/routes
                                                               ["/book-library"]    book-library-routes/routes}}
                  "parents"           {[""]        :parent
                                       ["/" #".*"] :parent}
                  
                  "educators"         {[""]        :admin
                                       ["/" #".*"] :admin}
                  "teacher"           {[""]        :teacher
                                       ["/" #".*"] :teacher}
                  "ui"                {[""]        :ui
                                       ["/" #".*"] :ui}
                  "accounts"          {["/" #".*"] :login}}])

(defn- parse-url [url]
  (bidi/match-route routes url))

(defn- dispatch-route
  [{:keys [handler route-params] :as params}]
  (let [route (->> route-params (into []) (flatten) (concat [routes handler]) (apply bidi/path-for))]
    (re-frame/dispatch [::events/set-active-route (assoc params :url route)])
    (case handler
      :course (re-frame/dispatch [::ie/start-course (:id route-params)])
      :student-course-dashboard (re-frame/dispatch [::ie/load-course (:id route-params)])
      nil)

    (let [module-routes {:book-library {:routes   (vals book-library-routes/routes)
                                        :dispatch book-library-routes/dispatch-route}}]
      (doseq [[_ {:keys [routes dispatch]}] module-routes]
        (when (some #{handler} routes)
          (dispatch params))))))

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

(defn location
  [name]
  (let [locations {:profile "/user/profile"
                   :courses "/user/courses"
                   :login   "/user/login"
                   :logout  "/user/logout"
                   :books   "/user/books"}
        href (get locations name)]
    (set! js/document.location href)))

(re-frame/reg-fx
  :location
  (fn [args]
    (redirect-to (apply location args))))

(re-frame/reg-event-fx
  ::redirect
  (fn [{:keys [_]} [_ & args]]
    {:redirect args}))

(re-frame/reg-fx
  :redirect
  (fn [args]
    (apply redirect-to args)))

(defn get-active-route
  [db]
  (subs/get-active-route db))

(re-frame/reg-sub
  ::active-route
  (fn []
    (re-frame/subscribe [::subs/active-route]))
  (fn [active-route]
    active-route))
