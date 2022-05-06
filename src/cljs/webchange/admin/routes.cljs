(ns webchange.admin.routes
  (:require
    [clojure.string :as str]
    [re-frame.core :as re-frame]
    [webchange.admin.state :as state]
    [webchange.utils.module-router :as module-router]))

(def routes {""         :dashboard
             "/"        :dashboard
             "/login"   :login
             "/schools" {""                            :schools
                         "/add"                        :add-school
                         ["/" [#"[\w-%]+" :school-id]] {""          :school-profile
                                                        "/classes"  {""                           :classes
                                                                     "/add"                       :add-class
                                                                     ["/" [#"[\w-%]+" :class-id]] :class-profile}
                                                        "/students" {""     :students
                                                                     "/add" :add-student}
                                                        "/teachers" {""                           :teachers
                                                                     "/add"                       :add-teacher
                                                                     ["/" [#"[\w-%]+" :class-id]] :teacher-profile}
                                                        "/courses"  {""     :school-courses}}}
             "/courses" {""                              :courses
                         "/add"                          :add-course
                         ["/" [#"[\w-%]+" :course-slug]] {"" :course-profile}}})

(def sitemap
  {:dashboard {:schools {:school-profile {:classes  {:add-class     true
                                                     :class-profile true}
                                          :students {:add-student true}
                                          :teachers {:add-teacher     true
                                                     :teacher-profile true}}}
               :courses {:add-course     true
                         :course-profile true}}})

(defn get-title
  ([params]
   (get-title params {}))
  ([{:keys [handler props]} {:keys [with-root?] :or {with-root? true}}]
   (let [root "Admin"
         connector " / "
         s #(str/join connector (if with-root? (concat [root] %) %))]
     (case handler
       :dashboard (s ["Dashboard"])
       :schools (s ["Schools"])
       :school-profile (s [(str "School " (:school-id props))])
       :class-profile (s [(str "Class " (:class-id props))])
       (s [(-> (or handler :unknown) (clojure.core/name) (str/capitalize))])))))

(defonce router (atom nil))

(defn- dispatch-route
  [{:keys [handler route-params]}]
  (re-frame/dispatch [::state/set-current-page {:handler handler
                                                :props   route-params}]))

(defn init!
  [root-path]
  (reset! router (module-router/init! root-path routes dispatch-route)))

(defn set-title!
  [page-params]
  (->> (get-title page-params)
       (set! (.-title js/document))))

(re-frame/reg-event-fx
  ::redirect
  (fn [{:keys [_]} [_ & args]]
    {::module-router/redirect {:router          @router
                               :redirect-params args}}))
