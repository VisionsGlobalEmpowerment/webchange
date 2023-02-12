(ns webchange.school.handler
  (:require
    [buddy.auth :refer [throw-unauthorized]]
    [compojure.api.sweet :refer [GET POST PUT DELETE defroutes context]]
    [compojure.route :refer [not-found]]
    [ring.util.response :refer [response]]
    [webchange.school.core :as core]
    [clojure.tools.logging :as log]
    [webchange.common.handler :refer [handle current-user]]
    [webchange.school.statistics :as stats]
    [webchange.validation.specs.school-spec :as school-spec]
    [webchange.auth.roles :refer [is-admin? is-at-least-teacher?]]
    [clojure.spec.alpha :as s]))

(defn handle-current-school [request]
  (-> (core/get-current-school)
      response))

(defn handle-create-school
  [data request]
  (let [user-id (current-user request)]
    (when-not (is-admin? user-id)
      (throw-unauthorized {:role :educator}))
    (-> (core/create-school! data)
        handle)))

(defn handle-list-schools [request]
    (-> (core/get-schools)
        response))

(defn handle-update-school
  [id data request]
  (let [user-id (current-user request)]
    (when-not (is-admin? user-id)
      (throw-unauthorized {:role :educator}))
    (-> (core/update-school! id data)
        handle)))

(defn handle-delete-school
  [id request]
  (let [user-id (current-user request)]
    (when-not (is-admin? user-id)
      (throw-unauthorized {:role :educator}))
    (-> (core/delete-school! id)
        handle)))

(defn handle-archive-school
  [id request]
  (let [user-id (current-user request)]
    (when-not (is-admin? user-id)
      (throw-unauthorized {:role :educator}))
    (->> (core/archive-school! id)
         (vector true)
         handle)))

(defroutes school-routes
  (context "/api/schools" []
    :coercion :spec
    :tags ["school"]
    (GET "/current" request
      :return ::school-spec/school
      (handle-current-school request))
    (POST "/" request
      :return ::school-spec/new-school
      :body [data ::school-spec/create-school]
      (handle-create-school data request))
    (GET "/:id" request
      :path-params [id :- ::school-spec/id]
      :return (s/keys :req-un [::school-spec/school])
      (let [user-id (current-user request)]
        (when-not (or (is-admin? user-id)
                      (core/school-teacher? id user-id))
          (throw-unauthorized {:role :educator}))
        (if-let [item (core/get-school id)]
          (response {:school item})
          (not-found "not found"))))
    (GET "/" request
      :return (s/keys :req-un [::school-spec/schools])
      (let [user-id (current-user request)]
        (when-not (is-at-least-teacher? user-id)
          (throw-unauthorized {:role :educator}))
        (handle-list-schools request)))
    (PUT "/:id" request
      :path-params [id :- ::school-spec/id]
      :return ::school-spec/new-school
      :body [data ::school-spec/edit-school]
      (handle-update-school id data request))
    (DELETE "/:id" request
      :path-params [id :- ::school-spec/id]
      (handle-delete-school id request))
    (PUT "/:id/archive" request
      :path-params [id :- ::school-spec/id]
      (handle-archive-school id request)))
  (GET "/api/overall-statistics" request
    (-> (stats/get-overall-statistics)
        response)))
