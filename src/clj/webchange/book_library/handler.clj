(ns webchange.book-library.handler
  (:require
    [clojure.tools.logging :as log]
    [compojure.api.sweet :refer [GET POST PUT DELETE api context defroutes]]
    [schema.core :as s]
    [webchange.common.handler :refer [handle]]
    [webchange.course.core :as core]
    [webchange.course.handler :refer [Course]]))

(s/defschema Category {:name  s/Str
                       :value s/Str})

(defn- handle-get-books
  [_]
  (let [books (core/get-book-library {:with-host-name? false})]
    (handle [true books])))

(defn- handle-get-categories
  [_]
  (let [categories [{:name  "Animals"
                     :value "animals"}
                    {:name  "Family and Friends"
                     :value "family-and-friends"}
                    {:name  "Science/STEM"
                     :value "science-stem"}
                    {:name  "Sports"
                     :value "sports"}
                    {:name  "Vehicles"
                     :value "vehicles"}]]
    (handle [true categories])))

(defroutes book-library-api-routes
  (context "/api/book-library" []
    :tags ["book-library"]
    (GET "/all" request
      :return [Course]
      :summary "Returns all published books"
      (handle-get-books request))
    (GET "/categories" request
      :return [Category]
      :summary "Returns available categories"
      (handle-get-categories request))))
