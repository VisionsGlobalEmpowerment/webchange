(ns webchange.templates.handler
  (:require
    [compojure.api.sweet :refer [api context GET POST PUT DELETE defroutes]]
    [ring.util.response :refer [response]]
    [schema.core :as s]
    [webchange.templates.core :as core]
    [webchange.templates.library]))

(s/defschema Template {:id s/Int :name s/Str :description s/Str})

(defroutes templates-api-routes
  (context "/api/templates" []
    :tags ["templates"]
    (GET "/" []
      :return [Template]
      :summary "Returns list of templates"
      (-> (core/get-available-templates) response))))
