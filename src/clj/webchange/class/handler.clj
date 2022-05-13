(ns webchange.class.handler
  (:require [buddy.auth :refer [throw-unauthorized]]
            [compojure.api.sweet :refer [GET POST PUT DELETE defroutes]]
            [compojure.route :refer [not-found]]
            [ring.util.response :refer [response]]
            [webchange.class.core :as core]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user current-school]]
            [webchange.auth.core :as auth]
            [webchange.auth.roles :refer [is-admin?]]
            [webchange.validation.specs.student :as student-specs]
            [webchange.validation.specs.school-spec :as school-spec]
            [webchange.validation.specs.class-spec :as class-spec]
            [webchange.validation.specs.teacher :as teacher-spec]
            [clojure.spec.alpha :as s]
            [webchange.class.statistics]
            [webchange.school.core :as school]))

(defn handle-list-classes [request]
  (let [school-id (current-school request)]
    (-> (core/get-classes school-id)
        response)))

(defn handle-create-class
  ([data request]
   (let [school-id (current-school request)]
     (handle-create-class school-id data request)))
  ([school-id data request]
   (let [owner-id (current-user request)]
     (-> data
         (assoc :school-id school-id)
         (core/create-class!)
         handle))))

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
  ([data request]
   (let [school-id (current-school request)]
     (handle-create-student school-id data request)))
  ([school-id data request]
   (let [owner-id (current-user request)
         data (assoc data :school-id school-id)
         [{user-id :id}] (auth/create-user! data)]
     (auth/activate-user! user-id)
     (-> data
         (assoc :user-id user-id)
         core/create-student!
         handle))))

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

(defn handle-next-access-code
  ([request]
   (let [school-id (current-school request)]
     (handle-next-access-code school-id request)))
  ([school-id request]
   (-> (core/next-code school-id)
       handle)))

(defn handle-create-teacher
  [school-id data request]
  (let [user-id (current-user request)]
    (when-not (or (is-admin? user-id) (school/school-admin? school-id user-id))
      (throw-unauthorized {:role :educator}))
    (let [user-data (select-keys data [:first-name :last-name :email :password])
          [{user-id :id}] (auth/create-user-with-credentials! user-data)
          teacher-data {:school-id school-id
                        :user-id user-id
                        :type (:type data)
                        :status "active"}]
      (auth/activate-user! user-id)
      (-> (core/create-teacher! teacher-data)
          response))))

(defn handle-edit-teacher
  [teacher-id data request]
  (let [user-id (current-user request)
        teacher (core/get-teacher teacher-id)]
    (when-not (or (is-admin? user-id) (school/school-admin? (:school-id teacher) user-id))
      (throw-unauthorized {:role :educator}))
    (let [user-data (select-keys data [:fist-name :last-name :password])
          teacher-data {:type (:type data)
                        :status "active"}]
      (auth/edit-user! (:user-id teacher) user-data)
      (-> (core/edit-teacher! teacher-id teacher-data)
          response))))

(defroutes class-routes
  (GET "/api/classes" request (handle-list-classes request))
  (GET "/api/classes/:id" [id]
       (if-let [item (-> id Integer/parseInt core/get-class)]
         (response {:class item})
         (not-found "not found")))
  (POST "/api/classes" request
        (handle-create-class (:body request) request))
  (GET "/api/schools/:school-id/classes" request
       :coercion :spec
       :path-params [school-id :- ::school-spec/id]
       (-> (core/get-classes school-id)
           response))
  (POST "/api/schools/:school-id/classes" request
        :coercion :spec
        :path-params [school-id :- ::school-spec/id]
        :body [data ::class-spec/create-class]
        :return (s/keys :req-un [::class-spec/id])
        (handle-create-class school-id data request))
  (PUT "/api/classes/:id" [id :as request]
       (handle-update-class id request))
  (DELETE "/api/classes/:id" [id :as request]
          (handle-delete-class id request))

  (GET "/api/schools/:school-id/students" request
       :coercion :spec
       :path-params [school-id :- ::school-spec/id]
       (-> (core/get-students-by-school school-id)
           response))
  (POST "/api/schools/:school-id/students" request
        :coercion :spec
        :path-params [school-id :- ::school-spec/id] 
        :body [student ::student-specs/create-student]
        :return (s/keys :req-un [::id])
        (handle-create-student school-id student request))
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

  (POST "/api/schools/:school-id/next-access-code" request 
        :coercion :spec
        :path-params [school-id :- ::school-spec/id]
        (handle-next-access-code school-id request))
  (POST "/api/next-access-code" request (handle-next-access-code request))

  (GET "/api/teachers/:teacher-id" request
       :coercion :spec
       :path-params [teacher-id :- ::teacher-spec/id]
       (let [user-id (current-user request)
             teacher (core/get-teacher teacher-id)]
         (when-not (or (is-admin? user-id) (school/school-admin? (:school-id teacher) user-id))
           (throw-unauthorized {:role :educator}))
         (response {:teacher teacher})))
  (POST "/api/schools/:school-id/teachers" request
        :coercion :spec
        :path-params [school-id :- ::school-spec/id]
        :body [data ::teacher-spec/create-teacher]
        (handle-create-teacher school-id data request))
  (PUT "/api/teachers/:teacher-id" request
       :coercion :spec
       :path-params [teacher-id :- ::teacher-spec/id]
       :body [data ::teacher-spec/edit-teacher]
       (handle-edit-teacher teacher-id data request))
  (GET "/api/schools/:school-id/teachers" request
       :coercion :spec
       :path-params [school-id :- ::school-spec/id]
       (let [user-id (current-user request)]
         (when-not (or (is-admin? user-id) (school/school-admin? school-id user-id))
           (throw-unauthorized {:role :educator}))
         (-> (core/teachers-by-school school-id)
             response)))
  (GET "/api/classes/:class-id/teachers" request
       :coercion :spec
       :path-params [class-id :- ::class-spec/id]
       (let [user-id (current-user request)
             class (core/get-class class-id)]
         (when-not (or (is-admin? user-id) (school/school-admin? (:school-id class) user-id))
           (throw-unauthorized {:role :educator}))
         (-> (core/teachers-by-class class-id)
             response))))
