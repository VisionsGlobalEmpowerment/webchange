(ns webchange.common.audio-parser.recognizer
  (:require
    [clojure.data.json :as json]
    [clojure.string :as s]
    [clojure.java.io :as io]
    [clojure.java.shell :refer [sh]]
    [config.core :refer [env]]))

(def parser-directory "src/clj/webchange/common/audio_parser/rhubarb/")

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

(defn get-file-extension
  [file-name]
  (subs file-name (inc (s/last-index-of file-name "."))))

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

(defn recognize-audio
  [file-path]
  (let [result-file (get-changed-extension file-path "json")]
    (when-not (file-exist? result-file)
      (let [converted-file-path (convert-file file-path)
            recognizer (get ["pocketSphinx" "phonetic"] 0)
            result (sh "rhubarb"
                       "-o" result-file
                       "-r" recognizer
                       "--quiet"
                       "--exportFormat" "json"
                       "--machineReadable"
                       converted-file-path)]
        (io/delete-file converted-file-path)
        (when (= (:exit result) 1)
          (throw (Exception. (:err result))))))
    result-file))

(defn read-results
  [file-name]
  (->> file-name
       slurp
       json/read-json))

(defn get-phonemes
  [file-path]
  (-> file-path
      (relative->absolute-path)
      (recognize-audio)
      (read-results)
      (:mouthCues)))
