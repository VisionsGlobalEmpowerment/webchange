(ns webchange.hackathon.loader
  (:require [clojure.string :refer [join]]
            [clojure.java.io :as io]
            [config.core :refer [env]]))

(defn copy-file [source-path dest-path]
  (io/copy (io/file source-path) (io/file dest-path)))

(defn copy-dir
  [source-dir target-dir]
  (doseq [file (file-seq (io/file source-dir))]
    (if-not (.isDirectory file)
      (let [source-file (.getPath file)
            target-file (clojure.string/replace (.getPath file) source-dir target-dir)]
          (clojure.java.io/make-parents target-file)
          (copy-file source-file target-file)))))

(defn init-hackathon! [config]
  (copy-file "../profiles.clj" "./profiles.clj")
  (copy-dir "../public" "resources/public")
  )

(def commands
  {"init-hackathon"
   (fn [config args]
     (apply init-hackathon! config args))})

(defn command? [[arg]]
  (contains? (set (keys commands)) arg))

(defn execute
  [args opts]
  (when-not (command? args)
    (throw
      (IllegalArgumentException.
        (str "unrecognized option: " (first args)
             ", valid options are:" (join ", " (keys commands))))))
  ((get commands (first args)) opts (rest args)))
