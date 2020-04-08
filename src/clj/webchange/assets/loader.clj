(ns webchange.assets.loader
  (:require
            [clojure.string :refer [join]]
            [webchange.assets.core :as core]
            ))


(defn calc-hashes!
  [config]
  (let [dirs (core/directories config)]
    (core/clear-asset-hash-table!)
    (doseq [dir dirs]
      (doseq [file (filter (fn [f] (. f isFile)) (core/files dir))]
        (core/store-asset-hash! (. file getPath))))
    ))

(def commands
  {"calc-asset-hash"
   (fn [config args]
     (apply calc-hashes! config args))
   })

(defn command? [[arg]]
  (contains? (set (keys commands)) arg)
  )

(defn execute
  "args - vector of arguments, e.g: [\"calc-asset-hash\"]"
  [args opts]
  (when-not (command? args)
    (throw
      (IllegalArgumentException.
        (str "unrecognized option: " (first args)
             ", valid options are: " (join ", " (keys commands))))))
  ((get commands (first args)) opts (rest args)))
