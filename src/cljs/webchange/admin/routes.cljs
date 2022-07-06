(ns webchange.admin.routes
  (:require
    [clojure.string :as str]
    [re-frame.core :as re-frame]
    [webchange.admin.state :as state]
    [webchange.utils.module-router :as module-router]))

(def routes {""          :dashboard
             "/"         :dashboard
             "/schools"  {""                            :schools
                          "/add"                        :school-add
                          "/archived"                   :schools-archived
                          ["/" [#"[\w-%]+" :school-id]] {""          :school-profile
                                                         "/classes"  {""                           :classes
                                                                      "/add"                       :class-add
                                                                      ["/" [#"[\w-%]+" :class-id]] {""          :class-profile
                                                                                                    "/students" {""                             :class-students
                                                                                                                 "/add"                         :class-students-add
                                                                                                                 ["/" [#"[\w-%]+" :student-id]] :student-profile}}}
                                                         "/students" {""                             :students
                                                                      "/add"                         :student-add
                                                                      ["/" [#"[\w-%]+" :student-id]] :student-edit}
                                                         "/teachers" {""                             :teachers
                                                                      "/add"                         :teacher-add
                                                                      ["/" [#"[\w-%]+" :teacher-id]] :teacher-profile}
                                                         "/courses"  {"" :school-courses}}}
             "/courses"  {""                              :courses
                          "/add"                          :course-add
                          ["/" [#"[\w-%]+" :course-slug]] {""      :course-profile
                                                           "/edit" :course-edit}}
             "/accounts" {"/my"                                :account-my
                          ["/add/" [#"[\w-%]+" :account-type]] :account-add
                          ["/" [#"[\d-%]+" :account-id]]       {""                :account-edit
                                                                "/reset-password" :password-reset}
                          ["/" [#"[\w-%]+" :account-type]]     :accounts}
             "/library"  {"/activities" {""                              :activities
                                         ["/" [#"[\w-%]+" :activity-id]] :activity-edit}
                          "/books"      {""                          :books
                                         "create"                    :book-create
                                         ["/" [#"[\w-%]+" :book-id]] :book-edit}}})

(def sitemap
  {:dashboard {:schools    {:school-profile   {:classes        {:class-add     true
                                                                :class-profile {:class-students     {:student-profile true}
                                                                                :class-students-add true}}
                                               :students       {:student-add true}
                                               :teachers       {:teacher-add     true
                                                                :teacher-profile true}
                                               :school-courses true}
                            :school-add       true
                            :schools-archived true}
               :courses    {:add-course     true
                            :course-profile true}
               :accounts   {:account-add    true
                            :account-edit   true
                            :password-reset true}
               :account-my true
               :library    {:activities {:activity-edit true}
                            :books      {:book-edit true}}}})

(defn get-title
  ([params]
   (get-title params {}))
  ([{:keys [handler props]} {:keys [with-root?] :or {with-root? true}}]
   (let [root "Admin"
         connector " / "
         s #(str/join connector (if with-root? (concat [root] %) %))]
     (case handler
       :dashboard (s ["Dashboard"])
       :school-add (s ["New School"])
       :schools (s ["Schools"])
       :school-profile (s [(str "School " (:school-id props))])
       :class-profile (s [(str "Class " (:class-id props))])
       :class-students "Students"
       :class-students-add "Add Students"
       :school-courses (s ["Courses"])
       :account-my "My Account"
       (s [(-> (or handler :unknown) (clojure.core/name) (clojure.string/replace "-" " ") (str/capitalize))])))))

(defonce router (atom nil))

(defn- dispatch-route
  [{:keys [handler route-params url-params]}]
  (re-frame/dispatch [::state/set-current-page {:handler handler
                                                :props   (cond-> route-params
                                                                 (some? url-params) (assoc :url-params url-params))}]))

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
