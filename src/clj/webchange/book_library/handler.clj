(ns webchange.book-library.handler
  (:require
    [clojure.tools.logging :as log]
    [compojure.api.sweet :refer [GET POST PUT DELETE api context defroutes]]
    [schema.core :as s]
    [webchange.book-library.data :refer [ages categories genres languages reading-levels tags]]
    [webchange.common.handler :refer [handle]]
    [webchange.course.core :as core]
    [webchange.course.handler :refer [Course]]))

(s/defschema SimpleListItem {:name  s/Str
                             :value s/Str})

(s/defschema CategoryMetadata {(s/optional-key :book-library?) s/Bool})
(s/defschema Category {:name                      s/Str
                       :value                     s/Str
                       (s/optional-key :metadata) CategoryMetadata})

(defn- handle-get-books
  [_]
  (let [books (core/get-book-library {:with-host-name? false})]
    (handle [true books])))

(defn- handle-get-categories
  [_]
  (handle [true categories]))


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
      (handle-get-categories request))
    (GET "/ages" _
      :return [SimpleListItem]
      :summary "Returns available ages"
      (handle [true ages]))
    (GET "/genres" _
      :return [SimpleListItem]
      :summary "Returns available genres"
      (handle [true genres]))
    (GET "/languages" _
      :return [SimpleListItem]
      :summary "Returns available languages"
      (handle [true languages]))
    (GET "/reading-levels" _
      :return [SimpleListItem]
      :summary "Returns available reading levels"
      (handle [true reading-levels]))
    (GET "/tags" _
      :return [SimpleListItem]
      :summary "Returns available tags"
      (handle [true tags]))))
