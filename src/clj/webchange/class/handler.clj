(ns webchange.class.handler
  (:require [compojure.api.sweet :refer [GET POST PUT DELETE defroutes]]
            [compojure.route :refer [not-found]]
            [ring.util.response :refer [response]]
            [webchange.class.core :as core]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user current-school]]
            [webchange.auth.core :as auth]
            [webchange.validation.specs.student :as student-specs]
            [clojure.spec.alpha :as s]))

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

(defn handle-create-student
  [data request]
  (let [owner-id (current-user request)
        school-id (current-school request)
        data (assoc data :school-id school-id)
        [{user-id :id}] (auth/create-user! data)]
    (auth/activate-user! user-id)
    (-> data
        (assoc :user-id user-id)
        core/create-student!
        handle)))

(defn handle-update-student
  [id request]
  (let [owner-id (current-user request)
        data (-> request :body)
        student (core/get-student (Integer/parseInt id))]
    (when (:access-code data)
      (core/update-student-access-code! (Integer/parseInt id) (select-keys data [:access-code])))
    (core/update-student! (Integer/parseInt id) (select-keys data [:class-id :gender :date-of-birth]))
    (auth/update-student-user! (:user-id student) (select-keys data [:first-name :last-name]))
    (handle [true {:id id}])))

(defn handle-unassign-student
  [id request]
  (let [owner-id (current-user request)]
    (-> (core/unassign-student! (Integer/parseInt id))
        handle)))

(defn handle-delete-student
  [id request]
  (let [owner-id (current-user request)]
    (-> (core/delete-student! (Integer/parseInt id))
        handle)))

(defn handle-next-access-code [request]
  (let [school-id (current-school request)]
    (-> (core/next-code school-id)
        handle)))

(defroutes class-routes
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
  (GET "/api/students/:id" [id]
       (if-let [item (-> id Integer/parseInt core/get-student)]
         (response {:student item})
         (not-found "not found")))
  (POST "/api/students" request
        :coercion :spec
        :body [student ::student-specs/student]
        :return (s/keys :req-un [::id])
        (handle-create-student student request))
  (PUT "/api/students/:id" [id :as request]
       (handle-update-student id request))
  (DELETE "/api/students/:id/class" [id :as request]
          (handle-unassign-student id request))
  (DELETE "/api/students/:id" [id :as request]
          (handle-delete-student id request))

  (POST "/api/next-access-code" request (handle-next-access-code request)))
