(ns webchange.server-dev
  (:require [webchange.handler :refer [handler dev-handler]]
            [config.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]]
            [mount.core :as mount]
            [webchange.server :as server]
            [webchange.mq.zero-mq :as mq]
            [webchange.common.voice-recognition.worker :as vr]
            )
  (:gen-class))

(defn taskwork-future [] (clojure.core.async/thread (mq/receive :voice-recognition vr/worker)))

(defn -main [& args]
  (apply server/-main args))

(defn dev [& args]
  (taskwork-future)
  (mount/start)
  (let [port (Integer/parseInt (or (env :port) "3000"))]
    (run-jetty dev-handler {:port port :join? false})))
