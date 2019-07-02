(ns webchange.service-worker.utils)

(def prefix "[ServiceWorker]")

(defn debug
  [& args]
  (apply js/console.info (into [prefix] args)))

(defn log
  [& args]
  (apply js/console.log (into [prefix] args)))

(defn warn
  [& args]
  (apply js/console.warn (into [prefix] args)))

(defn log-folded
  [title & args]
  (let [group-name (str prefix " " title)]
    (js/console.groupCollapsed group-name)
    (apply js/console.log args)
    (js/console.groupEnd group-name)))
