(ns webchange.server
  (:require [webchange.handler :refer [handler dev-handler]]
            [config.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]]
            [mount.core :as mount]
            [luminus-migrations.core :as migrations]
            [webchange.dataset.loader :as datasets]
            [webchange.secondary.loader :as secondary]
            [webchange.assets.loader :as assets]
            [webchange.hackathon.loader :as hackathon]
            [webchange.voice-recognizer.loader :as voice-recognizer]
            [webchange.migrations.kitkit.loader :as kitkit-book-import]
            [webchange.migrations.onebillion.loader :as onebillion-book-import]
            [webchange.mq.zero-mq :as queues]
            [webchange.mq.zero-mq-init :as zmq-init]
            [webchange.course.loader :as courses])
  (:gen-class))

(defn -main [& args]
  (mount/start-without #'zmq-init/zero-mq-init)
  (cond
    (migrations/migration? args)
    (do
      (migrations/migrate args (select-keys env [:database-url]))
      (System/exit 0))
    (datasets/command? args)
    (do
      (datasets/execute args (select-keys env [:dataset-dir]))
      (System/exit 0))
    (assets/command? args)
    (do
      (assets/execute args env)
      (System/exit 0))
    (kitkit-book-import/command? args)
    (do
      (kitkit-book-import/execute args env)
      (System/exit 0))
    (onebillion-book-import/command? args)
    (do
      (onebillion-book-import/execute args env)
      (System/exit 0))
    (hackathon/command? args)
    (do
      (hackathon/execute args {})
      (System/exit 0))
    (voice-recognizer/command? args)
    (do
      (voice-recognizer/execute args {})
      (System/exit 0))
    (courses/command? args)
    (do
      (courses/execute args (select-keys env [:course-dir]))
      (System/exit 0))
    (secondary/command? args)
    (do
      (secondary/execute args {})
      (System/exit 0))
    :else
    (let [port (Integer/parseInt (or (env :port) "3000"))]
      (mount/start #'zmq-init/zero-mq-init)
      (run-jetty handler {:port port :join? false}))))

(defn dev [& args]
  (mount/start)
  (let [port (Integer/parseInt (or (env :port) "3000"))]
    (run-jetty dev-handler {:port port :join? false})))
