(ns webchange.resources.handler
  (:require [clojure.edn :as edn]
            [compojure.core :refer [GET defroutes]]
            [ring.middleware.params :refer [wrap-params]]
            [webchange.common.audio-parser :refer [get-talking-animation]]
            [webchange.resources.core :as core]
            [webchange.common.handler :refer [handle]]))

(defn handle-app-resources
  []
  (-> (core/get-app-resources)
      handle))

(defn handle-level-resources
  [_]
  (-> (core/get-level-resources)
      handle))

(defn handle-parse-audio-animation
  [request]
  (let [{:strs [file start duration]} (:query-params request)]
    (-> [true (get-talking-animation file
                                     (edn/read-string start)
                                     (edn/read-string duration))]
        handle)))

(defroutes resources-routes
           (GET "/api/resources/app" _ (handle-app-resources))
           (GET "/api/resources/level/:level-id" [level-id] (handle-level-resources level-id))
           (GET "/api/resources/talking-animation" _ (wrap-params handle-parse-audio-animation)))

