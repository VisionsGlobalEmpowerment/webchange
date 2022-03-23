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

(s/defschema LanguageMetadata {(s/optional-key :primary?) s/Bool})
(s/defschema Language {:name                      s/Str
                       :value                     s/Str
                       (s/optional-key :metadata) LanguageMetadata})

(defn- handle-get-books
  ([request]
   (handle-get-books request nil))
  ([_ language]
   (let [books (cond->> (core/get-book-library {:with-host-name? false})
                        (some? language) (filter #(= (:lang %) language)))]
     (handle [true books]))))

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
      :return [Language]
      :summary "Returns available languages"
      (handle [true languages]))
    (GET "/reading-levels" _
      :return [SimpleListItem]
      :summary "Returns available reading levels"
      (handle [true reading-levels]))
    (GET "/tags" _
      :return [SimpleListItem]
      :summary "Returns available tags"
      (handle [true tags]))
    (GET "/:language" request
      :path-params [language :- s/Str]
      :return [Course]
      :summary "Returns published books for specified language"
      (handle-get-books request language))))
