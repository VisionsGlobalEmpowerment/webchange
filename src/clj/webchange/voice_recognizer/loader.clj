(ns webchange.voice-recognizer.loader
  (:require [clojure.string :refer [join]]
            [clojure.java.io :as io]
            [webchange.mq.zero-mq :as mq]
            [webchange.common.voice-recognition.worker :as vr]
            [webchange.common.files :as files]
            [config.core :refer [env]]))

(defn init-recognizer
  [voice-recognition-model-url]
  (let [model-path (:voice-recognition-model env)
        url voice-recognition-model-url
        name (.toString (java.util.UUID/randomUUID))
        tmp-file (str "/tmp/" name ".zip")
        tmp-dir (str "/tmp/" name)]
    (println "Preparing directories...")
    (files/delete-recursively model-path)
    (clojure.java.io/make-parents (str model-path "/1.txt"))
    (clojure.java.io/make-parents (str tmp-dir "/1.txt"))
    (println "Downloading model...")
    (files/save-file-from-uri url tmp-file)
    (println "Unpacking model...")
    (files/unzip-file tmp-file tmp-dir)
    (files/move (files/get-first-directory tmp-dir) model-path)
    (println "Do some clean up...")
    (io/delete-file tmp-file)
    (io/delete-file tmp-dir)
    ))

(defn recognition-worker
  []
  (mq/receive :voice-recognition vr/worker))

(def commands
  {
   "init-recognizer"
   (fn [config args]
     (apply init-recognizer args))
   "recognition-worker"
   (fn [config args]
     (apply recognition-worker args))
   })

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
