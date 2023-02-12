(ns webchange.class.handler
  (:require [buddy.auth :refer [throw-unauthorized]]
            [compojure.api.sweet :refer [GET POST PUT DELETE defroutes]]
            [ring.util.response :refer [response not-found]]
            [webchange.class.core :as core]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user current-school]]
            [webchange.auth.core :as auth]
            [webchange.auth.roles :refer [is-admin?]]
            [webchange.accounts.core :as accounts]
            [webchange.validation.specs.student :as student-specs]
            [webchange.validation.specs.school-spec :as school-spec]
            [webchange.validation.specs.class-spec :as class-spec]
            [webchange.validation.specs.teacher :as teacher-spec]
            [clojure.spec.alpha :as s]
            [webchange.class.statistics]
            [webchange.school.core :as school]))

(defn handle-create-class
  [school-id data request]
  (let [owner-id (current-user request)]
    (when-not (or (is-admin? owner-id) (school/school-admin? school-id owner-id))
      (throw-unauthorized {:role :educator}))
    (-> data
        (assoc :school-id school-id)
        (core/create-class!)
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
  ([data request]
   (let [school-id (current-school request)]
     (handle-create-student school-id data request)))
  ([school-id data request]
   (let [owner-id (current-user request)
         data (assoc data
                     :school-id school-id
                     :type "student")]
     (when-not (or (is-admin? owner-id) (school/school-admin? school-id owner-id))
       (throw-unauthorized {:role :educator}))
     (let [[{user-id :id}] (accounts/create-user! data)]
       (-> data
           (assoc :user-id user-id)
           core/create-student!
           handle)))))

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
          [{user-id :id}] (accounts/create-user-with-credentials! (assoc user-data :type "teacher"))
          teacher-data {:school-id school-id
                        :user-id   user-id
                        :type      (:type data)
                        :status    "active"}]
      (-> (core/create-teacher! teacher-data)
          response))))

(defn handle-edit-teacher
  [teacher-id data request]
  (let [user-id (current-user request)
        teacher (core/get-teacher teacher-id)]
    (when-not (or (is-admin? user-id) (school/school-admin? (:school-id teacher) user-id))
      (throw-unauthorized {:role :educator}))
    (let [user-data (select-keys data [:first-name :last-name :password])
          teacher-data {:type   (:type data)
                        :status "active"}]
      (accounts/edit-teacher-user! (:user-id teacher) (assoc user-data :type "teacher"))
      (-> (core/edit-teacher! teacher-id teacher-data)
          response))))

(defn handle-edit-teacher-status
  [teacher-id {:keys [status]} request]
  (let [user-id (current-user request)
        teacher (core/get-teacher teacher-id)        ]
    (when-not (or (is-admin? user-id) (school/school-admin? (:school-id teacher) user-id))
      (throw-unauthorized {:role :educator}))
    (let [teacher-data {:type   (:type teacher)
                        :status status}]
      (-> (core/edit-teacher! teacher-id teacher-data)
          response))))

(defn handle-delete-teacher
  [teacher-id request]
  (let [user-id (current-user request)
        teacher (core/get-teacher teacher-id)]
    (when-not (or (is-admin? user-id) (school/school-admin? (:school-id teacher) user-id))
      (throw-unauthorized {:role :educator}))
    (-> (core/archive-teacher! teacher-id)
        response)))

(defn handle-remove-teacher-from-class
  [teacher-id class-id request]
  (let [user-id (current-user request)
        teacher (core/get-teacher teacher-id)]
    (when-not (or (is-admin? user-id) (school/school-admin? (:school-id teacher) user-id))
      (throw-unauthorized {:role :educator}))
    (-> (core/remove-teacher-from-class teacher-id class-id)
        response)))

(defn- can-edit-class?
  [class-id request]
  (let [user-id (current-user request)
        class (core/get-class class-id)]
    (or (is-admin? user-id)
        (school/school-admin? (:school-id class) user-id))))

(defn- can-view-class?
  [class-id request]
  (let [user-id (current-user request)
        class (core/get-class class-id)]
    (or (is-admin? user-id)
        (school/school-teacher? (:school-id class) user-id))))

(defn- can-view-school?
  [school-id request]
  (let [user-id (current-user request)]
    (or (is-admin? user-id)
        (school/school-teacher? school-id user-id))))

(defroutes class-routes
  (GET "/api/classes/:class-id" [class-id :as request]
    :coercion :spec
    :path-params [class-id :- ::class-spec/id]
    (if (can-view-class? class-id request)
      (if-let [class (core/get-class class-id)]
        (-> {:class class}
            response)
        (not-found "not found"))
      (throw-unauthorized {:role :educator})))
  (GET "/api/schools/:school-id/classes" request
    :coercion :spec
    :path-params [school-id :- ::school-spec/id]
    (if (can-view-school? school-id request)
      (-> (core/get-classes school-id)
          response)
      (throw-unauthorized {:role :educator})))
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

  (PUT "/api/classes/:class-id/students" request
    :coercion :spec
    :path-params [class-id :- ::class-spec/id]
    :body [students-ids (s/coll-of ::student-specs/id)]
    (if (can-edit-class? class-id request)
      (-> (core/assign-students-to-class students-ids class-id)
          (response))
      (throw-unauthorized {:role :educator})))

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
  (DELETE "/api/teachers/:teacher-id" request
    :coercion :spec
    :path-params [teacher-id :- ::teacher-spec/id]
    (handle-delete-teacher teacher-id request))
  (DELETE "/api/teachers/:teacher-id/classes/:class-id" request
    :coercion :spec
    :path-params [teacher-id :- ::teacher-spec/id
                  class-id :- ::class-spec/id]
    (handle-remove-teacher-from-class teacher-id class-id request))
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
    (if (can-edit-class? class-id request)
      (-> (core/teachers-by-class class-id)
          (response))
      (throw-unauthorized {:role :educator})))
  (PUT "/api/classes/:class-id/teachers" request
    :coercion :spec
    :path-params [class-id :- ::class-spec/id]
    :body [teachers-ids (s/coll-of ::teacher-spec/id)]
    (if (can-edit-class? class-id request)
      (-> (core/assign-teachers-to-class teachers-ids class-id)
          (response))
      (throw-unauthorized {:role :educator})))
  (PUT "/api/classes/:class-id/archive" request
    :coercion :spec
    :path-params [class-id :- ::class-spec/id]
    (let [user-id (current-user request)
          {school-id :schoo-id} (core/get-class class-id)]
      (when-not (or (is-admin? user-id) (school/school-admin? school-id user-id))
        (throw-unauthorized {:role :educator}))
      (-> (core/archive-class! class-id)
          response)))
  (PUT "/api/teachers/:teacher-id/archive" request
    :coercion :spec
    :path-params [teacher-id :- ::teacher-spec/id]
    (let [user-id (current-user request)
          {school-id :school-id} (core/get-teacher teacher-id)]
      (when-not (or (is-admin? user-id) (school/school-admin? school-id user-id))
        (throw-unauthorized {:role :educator}))
      (-> (core/archive-teacher! teacher-id)
          response)))
  (PUT "/api/teachers/:teacher-id/status" request
    :coercion :spec
    :body [data ::teacher-spec/edit-teacher-status]
    :path-params [teacher-id :- ::teacher-spec/id]
    (handle-edit-teacher-status teacher-id data request))
  (PUT "/api/students/:student-id/archive" request
    :coercion :spec
    :path-params [student-id :- ::student-specs/id]
    (let [user-id (current-user request)
          {school-id :school-id} (core/get-student student-id)]
      (when-not (or (is-admin? user-id) (school/school-admin? school-id user-id))
        (throw-unauthorized {:role :educator}))
      (-> (core/archive-student! student-id)
          response))))
