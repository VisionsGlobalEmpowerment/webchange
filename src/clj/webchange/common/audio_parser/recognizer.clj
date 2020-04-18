(ns webchange.common.audio-parser.recognizer
  (:require
    [clojure.data.json :as json]
    [clojure.string :as s]
    [webchange.common.files :as f]
    [clojure.java.io :as io]
    [clojure.java.shell :refer [sh]]
    [config.core :refer [env]]))


(defn get-changed-extension
  [audio-file-path extension]
  (let [directory (f/get-directory audio-file-path)
        audio-file-name (f/get-file-name audio-file-path)
        result-file-name (subs audio-file-name 0 (s/last-index-of audio-file-name "."))]
    (str directory "/" result-file-name "." extension)))

(defn convert-file
  [file-path]
  (let [converted-file-path (get-changed-extension file-path "wav")]
    (when-not (f/file-exist? converted-file-path)
      (let [result (sh "ffmpeg" "-i" file-path converted-file-path)]
        (when (= (:exit result) 1)
          (throw (Exception. (:err result))))))
    converted-file-path))

(defn- result-file-for
  [file-path]
  (get-changed-extension file-path "json"))

(defn- lock-file-for
  [file-path]
  (get-changed-extension file-path "lock"))

(defn- with-lock
  [file-path f]
  (let [lock-file-path (lock-file-for file-path)
        delete-silently? true
        _ (f/create-new lock-file-path)
        result (f)]
    (io/delete-file lock-file-path delete-silently?)
    result))

(defn do-recognize-audio
  [file-path]
  (let [result-file (result-file-for file-path)
        converted-file-path (convert-file file-path)
        recognizer (get ["pocketSphinx" "phonetic"] 0)
        result (sh "rhubarb"
                   "-o" result-file
                   "-r" recognizer
                   "--quiet"
                   "--exportFormat" "json"
                   "--machineReadable"
                   converted-file-path)
        delete-silently? true]                          ; delete silently because of possible deleting from 2 racing processes
    ; (from 2 requests)
    (io/delete-file converted-file-path delete-silently?)
    (when (= (:exit result) 1)
      (throw (Exception. (:err result))))
    result-file))

(defn try-recognize-audio
  [file-path]
  (let [absolute-path (f/relative->absolute-path file-path)
        lock-file (lock-file-for absolute-path)
        result-file (result-file-for absolute-path)]
    (cond
      (f/file-exist? result-file) result-file
      (f/file-exist? lock-file) (throw (Exception. "Recognition is still in progress"))
      :else (with-lock absolute-path #(do-recognize-audio absolute-path)))))

(defn read-results
  [file-name]
  (->> file-name
       slurp
       json/read-json))

(defn get-phonemes
  [file-path]
  (-> file-path
      (try-recognize-audio)
      (read-results)
      (:mouthCues)))
