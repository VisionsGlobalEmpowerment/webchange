(ns webchange.server
  (:require [webchange.handler :refer [handler dev-handler]]
            [clojure.tools.logging :as log]
            [config.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]]
            [mount.core :as mount]
            [luminus-migrations.core :as migrations]
            [webchange.dataset.loader :as datasets]
            [webchange.secondary.loader :as secondary]
            [webchange.secondary.updater :as updater]
            [webchange.assets.loader :as assets]
            [webchange.templates.loader :as templates]
            [webchange.voice-recognizer.loader :as voice-recognizer]
            [webchange.mq.zero-mq :as queues]
            [webchange.mq.zero-mq-init :as zmq-init]
            [webchange.course.loader :as courses]
            [webchange.migrations.core])
  (:gen-class))

(defn -main [& args]
  (cond
    (migrations/migration? args)
    (do
      (mount/start-without #'zmq-init/zero-mq-init)
      (migrations/migrate args (select-keys env [:database-url]))
      (System/exit 0))
    (datasets/command? args)
    (do
      (mount/start-without #'zmq-init/zero-mq-init)
      (datasets/execute args (select-keys env [:dataset-dir]))
      (System/exit 0))
    (assets/command? args)
    (do
      (mount/start-without #'zmq-init/zero-mq-init)
      (assets/execute args env)
      (System/exit 0))
    (voice-recognizer/command? args)
    (do
      (voice-recognizer/execute args {})
      (System/exit 0))
    (courses/command? args)
    (do
      (mount/start-without #'zmq-init/zero-mq-init)
      (courses/execute args (select-keys env [:course-dir]))
      (System/exit 0))
    (secondary/command? args)
    (do
      (mount/start-without #'zmq-init/zero-mq-init)
      (secondary/execute args {})
      (System/exit 0))
    (templates/command? args)
    (do
      (mount/start-without #'zmq-init/zero-mq-init)
      (templates/execute args {})
      (System/exit 0))
    :else
    (let [port (Integer/parseInt (or (env :port) "3000"))]
      (mount/start)
      (migrations/migrate ["migrate"] (select-keys env [:database-url]))
      (let [server (run-jetty handler {:port port :join? false})]
        (try 
          (updater/resume-sync!)
          (catch Exception e
            (log/debug e)))
        (reset! updater/server-instance server)))))

(defn dev [& args]
  (mount/start)
  (let [port (Integer/parseInt (or (env :port) "3000"))]
    (run-jetty dev-handler {:port port :join? false})))
