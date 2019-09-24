(ns webchange.resources.handler
  (:require [compojure.core :refer [GET defroutes]]
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
  []
  (-> [true (get-talking-animation "/raw/audio/english/l1/a3/vera.wav" 1.5 3.0)]
      handle))

(defroutes resources-routes
           (GET "/api/resources/app" _ (handle-app-resources))
           (GET "/api/resources/level/:level-id" [level-id] (handle-level-resources level-id))
           (GET "/api/resources/parse-audio-animation" _ (handle-parse-audio-animation)))

