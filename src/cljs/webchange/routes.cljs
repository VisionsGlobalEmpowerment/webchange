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
                  "wizard"            :wizard               ;deprecated
                  "game-changer-beta" :game-changer
                  "game-changer"      {[""]                                                         :game-changer
                                       ["/" [#"[\w-%]+" :course-slug] "/" [#"[\w-%]+" :scene-slug]] :wizard-configured}
                  "book-creator"      :book-creator
                  "s"                 {["/" [#"[\w-%]+" :course-id] "/" [#"[\w-%]+" :scene-id]]                            :sandbox
                                       ["/" [#"[\w-%]+" :course-id] "/" [#"[\w-%]+" :scene-id] "/" [#".+" :encoded-items]] :sandbox}
                  "courses"           {["/" [#"[\w-%]+" :id]]                                                              :course
                                       ["/" [#"[\w-%]+" :id] "/editor"]                                                    :course-editor
                                       ["/" [#"[\w-%]+" :id] "/editor-v2"]                                                 :course-editor-v2
                                       ["/" [#"[\w-%]+" :course-id] "/table"]                                              :course-table
                                       ["/" [#"[\w-%]+" :course-id] "/scenes-crossing"]                                    :scenes-crossing
                                       ["/" [#"[\w-%]+" :course-id] "/editor-v2/concepts/" :concept-id]                    :course-editor-v2-concept
                                       ["/" [#"[\w-%]+" :course-id] "/editor-v2/add-concept"]                              :course-editor-v2-add-concept
                                       ["/" [#"[\w-%]+" :course-id] "/editor-v2/levels/" :level-id "/lessons/" :lesson-id] :course-editor-v2-lesson
                                       ["/" [#"[\w-%]+" :course-id] "/editor-v2/levels/" :level-id "/add-lesson"]          :course-editor-v2-add-lesson
                                       ["/" [#"[\w-%]+" :id] "/editor-v2/" [#"[\w-%]+" :scene-id]]                         :course-editor-v2-scene
                                       ["/" [#"[\w-%]+" :id] "/dashboard"]                                                 :student-course-dashboard
                                       ["/" [#"[\w-%]+" :id] "/dashboard/finished"]                                        :finished-activities}
                  "dashboard"         {[""]                                             :dashboard
                                       ["/classes"]                                     :dashboard-classes
                                       ["/schools"]                                     :dashboard-schools
                                       ["/courses"]                                     :dashboard-courses
                                       ["/classes/" :class-id]                          :dashboard-class-profile
                                       ["/classes/" :class-id "/students"]              :dashboard-students
                                       ["/classes/" :class-id "/students/" :student-id] :dashboard-student-profile}
                  "test-ui"           :test-ui}])


(defn- parse-url [url]
  (bidi/match-route routes url))

(defn- dispatch-route [{:keys [handler route-params] :as params}]
  (let [current-course @(re-frame/subscribe [::subs/current-course])]
    (re-frame/dispatch [::events/set-active-route params])
    (case handler
      :course (re-frame/dispatch [::ie/start-course (:id route-params)])
      :sandbox (re-frame/dispatch [::ie/start-sandbox (:course-id route-params) (:scene-id route-params) (:encoded-items route-params)])

      :dashboard-class-profile (re-frame/dispatch [::dashboard-events/open-class-profile (:class-id route-params) current-course])
      :dashboard-student-profile (re-frame/dispatch [::dashboard-events/open-student-profile (:student-id route-params) current-course])
      :dashboard-classes (re-frame/dispatch [::dashboard-events/open-classes])
      :dashboard-schools (re-frame/dispatch [::dashboard-events/open-schools])
      :dashboard-courses (re-frame/dispatch [::dashboard-events/open-courses])
      :dashboard-students (re-frame/dispatch [::dashboard-events/open-students (:class-id route-params)])

      ;; student dashboard
      :student-course-dashboard (re-frame/dispatch [::ie/load-course (:id route-params)])
      :finished-activities (re-frame/dispatch [::ie/load-course (:id route-params)])
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

(defn location
  [name]
  (let [locations {:profile "/user/profile"
                   :courses "/user/courses"
                   :login   "/user/login"
                   :books   "/user/books"}
        href (get locations name)]
    (set! js/document.location href)))

(re-frame/reg-fx
  :redirect
  (fn [args]
    (apply redirect-to args)))
