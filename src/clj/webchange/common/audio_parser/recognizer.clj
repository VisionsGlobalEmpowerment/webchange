(ns webchange.common.audio-parser.recognizer
  (:require
    [clojure.data.json :as json]
    [clojure.string :as s]
    [clojure.java.io :as io]
    [clojure.java.shell :refer [sh]]
    [config.core :refer [env]]))

(defn relative->absolute-path
  [relative-path]
  (str (env :public-dir) relative-path))

(defn file-exist?
  [file-path]
  (->> file-path io/file .exists))

(defn get-directory
  [file-path]
  (->> file-path io/file .getParent))

(defn get-file-name
  [file-path]
  (->> file-path io/file .getName))

(defn create-new
  [file-path]
  (->> file-path io/file .createNewFile))

(defn get-changed-extension
  [audio-file-path extension]
  (let [directory (get-directory audio-file-path)
        audio-file-name (get-file-name audio-file-path)
        result-file-name (subs audio-file-name 0 (s/last-index-of audio-file-name "."))]
    (str directory "/" result-file-name "." extension)))

(defn convert-file
  [file-path]
  (let [converted-file-path (get-changed-extension file-path "wav")]
    (when-not (file-exist? converted-file-path)
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
        _ (create-new lock-file-path)
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
  (let [absolute-path (relative->absolute-path file-path)
        lock-file (lock-file-for absolute-path)
        result-file (result-file-for absolute-path)]
    (cond
      (file-exist? result-file) result-file
      (file-exist? lock-file) (throw (Exception. "Recognition is still in progress"))
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
