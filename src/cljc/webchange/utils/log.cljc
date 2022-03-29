(ns webchange.utils.log)

(defn apply-log
  [message]
  #?(:clj  (clojure.tools.logging/debug message)
     :cljs (js/console.log message)))

(defn log
  [& messages]
  (doseq [message messages]
    (apply-log message)))
