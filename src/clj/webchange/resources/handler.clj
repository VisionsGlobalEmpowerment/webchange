(ns webchange.resources.handler
  (:require
    [compojure.core :refer [GET defroutes]]
    [webchange.common.handler :refer [handle]]
    [webchange.resources.core-course :as course]
    [webchange.resources.core-web-app :as web-app]))

(defn- wrap
  [data]
  (handle [true data]))

(defn handle-course-lessons
  [course-slug]
  (-> (course/get-course-lessons course-slug)
      (wrap)))

(defn handle-course-lessons-resources
  [{:keys [course-slug]} {:keys [lessons]}]
  (-> (course/get-lessons-resources course-slug lessons)
      (wrap)))

(defn handle-web-app-resources
  []
  (-> (web-app/get-web-app-resources)
      (wrap)))

(defn handle-queried-request
  [request handler]
  (let [params (:params request)
        query (:query-string request)
        query-params (->> (clojure.string/split query #"&")
                          (map (fn [query-param]
                                 (clojure.string/split query-param #"=")))
                          (map (fn [[param-name param-value]]
                                 [(keyword param-name) (clojure.string/split param-value #",")]))
                          (into {}))]
    (handler params query-params)))

(defroutes resources-routes
           (GET "/api/resources/web-app" _ (handle-web-app-resources))
           (GET "/api/resources/game-app/:course-slug/lessons" [course-slug] (handle-course-lessons course-slug))
           (GET "/api/resources/game-app/:course-slug/lessons-resources" req (handle-queried-request req handle-course-lessons-resources)))
