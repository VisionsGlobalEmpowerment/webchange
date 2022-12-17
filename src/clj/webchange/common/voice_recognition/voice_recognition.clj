(ns webchange.common.voice-recognition.voice-recognition
  (:require
    [clojure.data.json :as json]
    [webchange.common.files :as f]
    [webchange.mq.zero-mq :as mq]
    [clojure.tools.logging :as log]
    [clojure.java.io :as io]))

(defn get-result-filename
  [file-path]
  (let [directory (f/get-directory file-path)
        file-name (f/get-file-name-without-extension file-path)]
  (f/relative->absolute-path (str directory "/" file-name "-transcript.json"))))

(defn sink-callback
  [result]
  (with-open [wrtr (io/writer (get-result-filename (:file-path result)))]
    (log/debug "Received recult for " (:file-path result))
    (.write wrtr (json/write-str result))))

(defn try-voice-recognition-audio
  [file-path model]
  (let [model (or (some #{model} ["english" "spanish" "tamil"]) "english")]
    (mq/send :voice-recognition {:file-path file-path :model model})))

(defn get-subtitles
  [filename]
  (-> filename
      (get-result-filename)
      (slurp)
      (json/read-json)
      :result))

