(ns webchange.server
  (:require [webchange.handler :refer [handler dev-handler]]
            [config.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]]
            [mount.core :as mount]
            [luminus-migrations.core :as migrations]
            [webchange.dataset.loader :as datasets])
  (:gen-class))

(defn -main [& args]
  (mount/start)
  (cond
    (migrations/migration? args)
    (do
      (migrations/migrate args (select-keys env [:database-url]))
      (System/exit 0))
    (datasets/command? args)
    (do
      (datasets/execute args (select-keys env [:dataset-dir]))
      (System/exit 0))
    :else
    (let [port (Integer/parseInt (or (env :port) "3000"))]
      (run-jetty handler {:port port :join? false}))))

(defn dev [& args]
  (mount/start)
  (let [port (Integer/parseInt (or (env :port) "3000"))]
    (run-jetty dev-handler {:port port :join? false})))