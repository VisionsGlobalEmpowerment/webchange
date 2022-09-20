(ns webchange.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :as re-frame]
            [webchange.book-library.routes :as book-library-routes]
            [webchange.student-dashboard.routes :as student-dashboard-routes]
            [webchange.events :as events]
            [webchange.subs :as subs]
            [webchange.interpreter.events :as ie]
            [webchange.dashboard.events :as dashboard-events]))

(def routes ["/" {""                  :login
                  "student-login"     {[""]             :student-login
                                       ["/" :school-id] :school-student-login}
                  "wizard"            :wizard               ;deprecated
                  "game-changer-beta" :game-changer
                  "game-changer"      {[""]                                                         :game-changer
                                       ["/" [#"[\w-%]+" :course-slug] "/" [#"[\w-%]+" :scene-slug]] :wizard-configured}
                  "book-creator"      :book-creator
                  "s"                 {["/" [#"[\w-%]+" :scene-id]]                                                        :activity-sandbox
                                       ["/" [#"[\w-%]+" :course-id] "/" [#"[\w-%]+" :scene-id]]                            :sandbox
                                       ["/" [#"[\w-%]+" :course-id] "/" [#"[\w-%]+" :scene-id] "/" [#".+" :encoded-items]] :sandbox}
                  "lesson-builder"    {["/" [#"[\w-%]+" :scene-id]] :lesson-builder}
                  "courses"           {["/" [#"[\w-%]+" :id]] {[""]                 :course
                                                               ["/editor"]          {[""]                         :course-editor
                                                                                     ["/" [#"[\w-%]+" :scene-id]] :course-editor-scene}
                                                               ["/editor-v2"]       {[""]                         :course-editor-v2 #_redirected
                                                                                     ["/concepts/" :concept-id]   :course-editor-v2-concept
                                                                                     ["/add-concept"]             :course-editor-v2-add-concept
                                                                                     ["/levels/" :level-id]       {["/add-lesson"]          :course-editor-v2-add-lesson
                                                                                                                   ["/lessons/" :lesson-id] :course-editor-v2-lesson}
                                                                                     ["/" [#"[\w-%]+" :scene-id]] :course-editor-v2-scene #_redirected}
                                                               ["/table"]           :course-table
                                                               ["/scenes-crossing"] :scenes-crossing
                                                               ["/dashboard"]       student-dashboard-routes/routes
                                                               ["/book-library"]    book-library-routes/routes}}
                  "dashboard"         {[""]                                             :dashboard
                                       ["/classes"]                                     :dashboard-classes
                                       ["/schools"]                                     :dashboard-schools
                                       ["/courses"]                                     :dashboard-courses
                                       ["/classes/" :class-id]                          :dashboard-class-profile
                                       ["/classes/" :class-id "/students"]              :dashboard-students
                                       ["/classes/" :class-id "/students/" :student-id] :dashboard-student-profile}
                  "parents"           {[""]        :parent
                                       ["/" #".*"] :parent}
                  "test-ui"           :test-ui
                  "admin"             {[""]        :admin
                                       ["/" #".*"] :admin}
                  "teacher"           {[""]        :teacher
                                       ["/" #".*"] :teacher}
                  "ui"                {[""]        :ui
                                       ["/" #".*"] :ui}
                  "accounts"          {["/" #".*"] :login}}])

(def redirects {:course-editor-v2       :course-editor
                :course-editor-v2-scene :course-editor-scene})

(defn- parse-url [url]
  (bidi/match-route routes url))

(defn- dispatch-route
  [{:keys [handler route-params] :as params}]
  (if (contains? redirects handler)
    (let [new-handler (get redirects handler)]
      (re-frame/dispatch (vec (concat [::redirect new-handler]
                                      (->> (into [] route-params) (flatten))))))
    (let [current-course @(re-frame/subscribe [::subs/current-course])
          route (->> route-params (into []) (flatten) (concat [routes handler]) (apply bidi/path-for))]
      (re-frame/dispatch [::events/set-active-route (assoc params :url route)])
      (case handler
        :course (re-frame/dispatch [::ie/start-course (:id route-params)])
        :sandbox (re-frame/dispatch [::ie/start-sandbox (:course-id route-params) (:scene-id route-params) (:encoded-items route-params)])

        :dashboard-student-profile (re-frame/dispatch [::dashboard-events/open-student-profile (:student-id route-params) current-course])
        :dashboard-classes (re-frame/dispatch [::dashboard-events/open-classes])
        :dashboard-schools (re-frame/dispatch [::dashboard-events/open-schools])
        :dashboard-courses (re-frame/dispatch [::dashboard-events/open-courses])
        :dashboard-students (re-frame/dispatch [::dashboard-events/open-students (:class-id route-params)])

        ;; student dashboard
        :student-course-dashboard (re-frame/dispatch [::ie/load-course (:id route-params)])
        nil)

      (let [module-routes {:book-library {:routes   (vals book-library-routes/routes)
                                          :dispatch book-library-routes/dispatch-route}}]
        (doseq [[_ {:keys [routes dispatch]}] module-routes]
          (when (some #{handler} routes)
            (dispatch params)))))))

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
