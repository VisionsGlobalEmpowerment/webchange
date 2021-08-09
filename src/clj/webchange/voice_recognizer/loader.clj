(ns webchange.voice-recognizer.loader
  (:require [clojure.string :refer [join]]
            [clojure.java.io :as io]
            [webchange.mq.zero-mq :as mq]
            [webchange.common.voice-recognition.worker :as vr]
            [webchange.common.files :as files]
            [config.core :refer [env]]))

(defn add-model
  [name url]
  (let [models-path (:voice-recognition-models-dir env)
        model-path (str models-path "/" name)
        tmp-file (str "/tmp/" name ".zip")
        tmp-dir (str "/tmp/" name)]
    (println "Preparing directories...")
    (io/make-parents model-path)
    (io/make-parents tmp-dir)
    (println "Downloading model...")
    (files/save-file-from-uri url tmp-file)
    (println "Unpacking model...")
    (files/unzip-file tmp-file tmp-dir)
    (files/move (files/get-first-directory tmp-dir) model-path)
    (println "Do some clean up...")
    (io/delete-file tmp-file)
    (io/delete-file tmp-dir)))

(defn recognition-worker
  []
  (vr/init-models!)
  (mq/receive :voice-recognition vr/worker))

(def commands
  {"add-model"
   (fn [config args]
     (apply add-model args))
   "recognition-worker"
   (fn [config args]
     (apply recognition-worker args))})

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
