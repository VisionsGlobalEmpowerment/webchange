(ns webchange.school.handler
  (:require [compojure.api.sweet :refer [GET POST PUT DELETE defroutes context]]
            [compojure.route :refer [not-found]]
            [ring.util.response :refer [response]]
            [webchange.school.core :as core]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user]]
            [schema.core :as s]
            [webchange.school.statistics]))

(s/defschema Stats {(s/optional-key :teachers) s/Int
                    (s/optional-key :students) s/Int
                    (s/optional-key :courses) s/Int
                    (s/optional-key :classes) s/Int})
(s/defschema School {:id s/Int :name s/Str :location (s/maybe s/Str) :about (s/maybe s/Str) :stats (s/maybe Stats)})
(s/defschema CreateSchool {:name s/Str :location s/Str :about s/Str})
(s/defschema EditSchool {:name s/Str :location s/Str :about s/Str})

(defn handle-current-school [request]
  (-> (core/get-current-school)
      response))

(defn handle-create-school
  [data request]
  (let [owner-id (current-user request)]
    (-> (core/create-school! data)
        handle)))

(defn handle-list-schools [request]
    (-> (core/get-schools)
        response))

(defn handle-update-school
  [id data request]
  (let [owner-id (current-user request)]
    (-> (core/update-school! id data)
        handle)))

(defn handle-delete-school
  [id request]
  (let [owner-id (current-user request)]
    (-> (core/delete-school! id)
        handle)))

(defroutes school-routes
  (context "/api/schools" []
           :tags ["school"]
           (GET "/current" request
                :return School
                (handle-current-school request))
           (POST "/" request
                 :return School
                 :body [data CreateSchool]
                 (handle-create-school data request))
           (GET "/:id" request
                :path-params [id :- s/Int]
                :return {:school School}
                (if-let [item (core/get-school id)]
                  (response {:school item})
                  (not-found "not found")))
           (GET "/" request
                :return {:schools [School]}
                (handle-list-schools request))
           (PUT "/:id" request
                :path-params [id :- s/Int]
                :return School
                :body [data EditSchool]
                (handle-update-school id data request))
           (DELETE "/:id" request
                   :path-params [id :- s/Int]
                   (handle-delete-school id request))))
