(ns webchange.voice-recognizer.recognizers.kaldi
  (:require
    [clojure.data.json :as json]
    [clojure.string :as str]
    [clojure.tools.logging :as log]
    [config.core :refer [env]])
  (:import (org.kaldi KaldiRecognizer)))

(defn- read-result
  [result]
  (-> result
      (str/replace #"([0-9]+),([0-9]+)" (fn [[_all number decimal]] (str number "." decimal)))
      (json/read-str)))

(defn- collect-result
  [result step-result]
  (if (get step-result "text") (-> result
                                   (assoc :result (concat (:result result) (get step-result "result")))
                                   (assoc :text (str (:text result) " " (get step-result "text"))))
      result))

(def recognizer-models (atom {}))

(defn recognize
  [input model]
  (let [recognizer-model (get @recognizer-models (or model "english"))
        recognizer (KaldiRecognizer. recognizer-model 16000.0)
        buf (byte-array 4096)]
    (log/debug "Recognizing...")
    (-> (loop [nbytes (.read input buf), result {:result [], :text ""}]
          (let [step-result (if (.AcceptWaveform recognizer buf)
                              (read-result (.Result recognizer)))
                result (collect-result result step-result)]
            (if (> nbytes 0)
              (recur (.read input buf) result)
              result)))
        (collect-result (read-result (.FinalResult recognizer))))))

(defn init!
  []
  (let [dir (:voice-recognition-models-dir env)
        english-model (org.kaldi.Model. (str dir "/english"))
        spanish-model (org.kaldi.Model. (str dir "/spanish"))]
    (reset! recognizer-models {"english" english-model
                               "spanish" spanish-model})))
