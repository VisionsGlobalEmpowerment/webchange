(ns webchange.class.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [webchange.class.core :as core]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user current-school validation-error]]
            [webchange.auth.core :as auth]
            [webchange.validation.validate :refer [validate]]
            [webchange.validation.specs.student :as student-specs]))

(defn handle-list-classes [request]
  (let [school-id (current-school request)]
    (-> (core/get-classes school-id)
        response)))

(defn handle-create-class
  [request]
  (let [owner-id (current-user request)
        school-id (current-school request)
        data (-> request
                 :body
                 (assoc :school-id school-id))]
    (-> (core/create-class! data)
        handle)))

(defn handle-update-class
  [id request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (-> (core/update-class! (Integer/parseInt id) data)
        handle)))

(defn handle-delete-class
  [id request]
  (let [owner-id (current-user request)]
    (-> (core/delete-class! (Integer/parseInt id))
        handle)))

(defn validate-student
  [student-data]
  (validate ::student-specs/student student-data))

(defn handle-create-student
  [request]
  (let [owner-id (current-user request)
        school-id (current-school request)
        data (-> request
                 :body
                 (assoc :school-id school-id))
        [{user-id :id}] (auth/create-user! data)]
    (auth/activate-user! user-id)
    (-> data
        (assoc :user-id user-id)
        core/create-student!
        handle)))

(defn handle-update-student
  [id request]
  (let [owner-id (current-user request)
        data (-> request :body)]
    (when (:access-code data)
      (core/update-student-access-code! (Integer/parseInt id) (select-keys data [:access-code])))
    (core/update-student! (Integer/parseInt id) (select-keys data [:class-id :gender :date-of-birth]))
    (auth/update-student-user! (:user-id data) (select-keys data [:first-name :last-name]))
    (handle [true {:id id}])))

(defn handle-unassign-student
  [id request]
  (let [owner-id (current-user request)]
    (-> (core/unassign-student! (Integer/parseInt id))
        handle)))

(defn handle-current-school [request]
  (-> (core/get-current-school)
      response))

(defn handle-next-access-code [request]
  (let [school-id (current-school request)]
    (-> (core/next-code school-id)
        handle)))

(defroutes class-routes
           (GET "/api/schools/current" request (handle-current-school request))
           (GET "/api/classes" request (handle-list-classes request))
           (GET "/api/classes/:id" [id]
             (if-let [item (-> id Integer/parseInt core/get-class)]
               (response {:class item})
               (not-found "not found")))
           (POST "/api/classes" request
             (handle-create-class request))
           (PUT "/api/classes/:id" [id :as request]
             (handle-update-class id request))
           (DELETE "/api/classes/:id" [id :as request]
             (handle-delete-class id request))

           (GET "/api/classes/:id/students" [id] (-> id Integer/parseInt core/get-students-by-class response))
           (GET "/api/unassigned-students" [] (-> (core/get-students-unassigned) response))
           (GET "/api/students/:id" [id] (-> id Integer/parseInt core/get-student response))
           (POST "/api/students" request
             (if-let [errors (validate-student (:body request))]
               (validation-error errors)
               (handle-create-student request)))
           (PUT "/api/students/:id" [id :as request]
             (handle-update-student id request))
           (DELETE "/api/students/:id" [id :as request]
             (handle-unassign-student id request))

           (POST "/api/next-access-code" request (handle-next-access-code request))
           )