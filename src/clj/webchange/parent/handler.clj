(ns webchange.parent.handler
  (:require
    [clojure.tools.logging :as log]
    [compojure.api.sweet :refer [GET POST PUT DELETE api context defroutes]]
    [schema.core :as s]
    [webchange.auth.core :as auth]
    [webchange.auth.handler :as auth-handler]
    [webchange.common.handler :refer [current-user handle]]
    [webchange.parent.core :as core]
    [buddy.auth :refer [throw-unauthorized]]))

(s/defschema Student {:id s/Int :name s/Str :first-name s/Str :last-name (s/maybe s/Str) :course-slug s/Str
                      :level s/Int :lesson s/Int})
(s/defschema CreateStudent {:name s/Str :age s/Int (s/optional-key :device) s/Str})

(s/defschema LoginAs {:id s/Int})

(defn- handle-get-students
  [request]
  (let [parent-id (current-user request)
        students (core/find-students-by-parent parent-id)]
    (handle students)))

(defn- handle-create-student
  [data request]
  (let [parent-id (current-user request)
        student (core/create-student data parent-id)]
    (handle student)))

(defn- handle-delete-student
  [student-id request]
  (let [parent-id (current-user request)]
    (when-not (core/parent-of? student-id parent-id)
      (throw-unauthorized {:role :parent}))
    (-> student-id
        core/delete-student
        handle)))

(defn- handle-login-as
  [{student-id :id} request]
  (let [parent-id (current-user request)]
    (when-not (core/parent-of? student-id parent-id)
      (throw-unauthorized {:role :parent}))
    (let [result (core/child-login! student-id)]
      (auth-handler/handle-login result request))))

(defroutes parent-api-routes
  (context "/api/parent" []
           :tags ["parent"]
           (GET "/students" request
                :return [Student]
                :summary "Returns list of student for current user as a parent"
                (handle-get-students request))
           (POST "/students" request
                 :return Student
                 :body [student-data CreateStudent]
                 :summary "Creates a new parent student"
                 (handle-create-student student-data request))
           (DELETE "/students/:student-id" request
                   :path-params [student-id :- s/Int]
                   :summary "Delete student"
                   (handle-delete-student student-id request))
           (POST "/students/login" request
                 :return Student
                 :body [login-data LoginAs]
                 :summary "Logs in as provided child student"
                 (handle-login-as login-data request))))