(ns webchange.common.voice-recognition.voice-recognition
  (:require
    [clojure.data.json :as json]
    [webchange.common.files :as f]
    [webchange.mq.zero-mq :as mq]
    [clojure.java.shell :refer [sh]]
    [webchange.common.audio-parser.animation :as animation]
    [config.core :refer [env]]
    [clojure.java.io :as io]
    [webchange.common.audio-parser.converter :refer [get-changed-extension convert-to-wav]]))

(defn get-result-filename
  [file-path]
  (let [directory (f/get-directory file-path)
        file-name (f/get-file-name-without-extension file-path)]
  (f/relative->absolute-path (str directory "/" file-name "-transcript.json"))))

(defn sink-callback
  [result]
  (with-open [wrtr (io/writer (get-result-filename (:file-path result)))]
    (.write wrtr (json/write-str result))))

(defn try-voice-recognition-audio
  [file-path model]
  (let [model (or (some #{model} ["english" "spanish"]) "english")]
    (mq/send :voice-recognition {:file-path file-path :model model})))

(defn get-subtitles
  [filename]
  (-> filename
      (get-result-filename)
      (slurp)
      (json/read-json)
      :result))

