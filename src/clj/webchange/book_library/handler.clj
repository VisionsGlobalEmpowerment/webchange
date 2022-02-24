(ns webchange.book-library.handler
  (:require
    [compojure.api.sweet :refer [GET POST PUT DELETE api context defroutes]]
    [webchange.common.handler :refer [handle]]
    [webchange.course.core :as core]
    [webchange.course.handler :refer [Course]]))

(defn- handle-get-books
  [_]
  (let [books (core/get-book-library {:with-host-name? false})]
    (handle [true books])))

(defroutes book-library-api-routes
  (context "/api/book-library" []
    :tags ["book-library"]
    (GET "/all" request
      :return [Course]
      :summary "Returns all published books"
      (handle-get-books request))))
