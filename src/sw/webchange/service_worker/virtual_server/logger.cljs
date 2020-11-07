(ns webchange.service-worker.virtual-server.logger
  (:require
    [webchange.service-worker.logger :as logger]))

(def prefix "[Virtual Server]")

(defn debug [& args] (apply logger/debug (into [prefix] args)))
(defn debug-folded [& args] (apply logger/debug-folded (into [prefix] args)))
(defn log [& args] (apply logger/log (into [prefix] args)))
(defn warn [& args] (apply logger/warn (into [prefix] args)))
(defn error [& args] (apply logger/error (into [prefix] args)))
