(ns webchange.logger
  (:require
    [webchange.config :refer [log-level]]))

(def current-level log-level)

(def prefix "[Client]")

(def log-levels {:debug 1
                 :log   2
                 :warn  3})

(defn- allowed?
  [level]
  (let [level-num (get log-levels level)
        current-level-num (get log-levels current-level)]
    (>= level-num current-level-num)))

(defn debug
  [& args]
  (when (allowed? :debug)
    (apply js/console.info (into [prefix "[Debug]"] args))))

(defn log
  [& args]
  (when (allowed? :log)
    (apply js/console.log (into [prefix "[Log]"] args))))

(defn warn
  [& args]
  (when (allowed? :warn)
    (apply js/console.warn (into [prefix "[Warn]"] args))))

(defn error
  [& args]
  (when (allowed? :error)
    (apply js/console.error (into [prefix "[Error]"] args))))

(defn debug-folded
  [title & args]
  (when (allowed? :debug)
    (let [group-name (str prefix " [Debug] " title)]
      (js/console.groupCollapsed group-name)
      (apply js/console.log args)
      (js/console.groupEnd group-name))))
