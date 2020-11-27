(ns webchange.common.voice-recognition.worker
  (:require [clojure.java.io :as io]
            [config.core :refer [env]]
            [clojure.data.json :as json]
            [clojure.edn :as edn]
            [clojure.tools.logging :as log]
            [webchange.common.files :as files]
            [webchange.common.audio-parser.converter :as converter]))

(defn download-file
  [file-path]
  (let [url (files/relative->absolute-url (:core-http-url env) file-path)
        extension (files/get-extension file-path)
        name (.toString (java.util.UUID/randomUUID))
        local-file-path (str "/tmp/" name "." extension)]
    (files/save-file-from-uri url local-file-path)
    local-file-path))

(defn- read-result
  [result]
  (-> result
      (clojure.string/replace #"([0-9]+),([0-9]+)" (fn [[all number decimal]] (str number "." decimal)))
      (json/read-str)))

(defn- collect-result
  [result step-result]
  (if (get step-result "text") (-> result
                                   (assoc :result (concat (:result result) (get step-result "result")))
                                   (assoc :text (str (:text result) " " (get step-result "text"))))
                               result))

(def recognizer-model (atom nil))

(defn worker
  [task]
  (if-not @recognizer-model (reset! recognizer-model (org.kaldi.Model. (:voice-recognition-model env))))
  (let [_ (log/debug "Downloading file...")
        local-file-path (download-file (:file-path task))
        _ (log/debug "Converting file...")
        local-file-path (converter/convert-to-wav local-file-path {:remove-origin? true
                                                                   :absolute-path? true
                                                                   :mono?          true
                                                                   :sample-rate    16000})
        input (io/input-stream local-file-path)
        recognizer (org.kaldi.KaldiRecognizer. @recognizer-model 16000.0)
        buf (byte-array 4096)
        _ (log/debug "Recognizing...")
        result (-> (loop [nbytes (.read input buf), result {:result [], :text ""}]
                     (let [step-result (if (.AcceptWaveform recognizer buf)
                                         (read-result (.Result recognizer)))
                           result (collect-result result step-result)]
                       (if (> nbytes 0)
                         (recur (.read input buf) result)
                         result)))
                   (collect-result (read-result (.FinalResult recognizer))))]
    _ (log/debug "Recognized, sending result...")
    (println result)
    (assoc result :file-path (:file-path task))))
