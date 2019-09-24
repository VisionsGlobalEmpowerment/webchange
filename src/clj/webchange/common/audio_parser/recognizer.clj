(ns webchange.common.audio-parser.recognizer
  (:require
    [clojure.data.json :as json]
    [clojure.string :as s]
    [clojure.java.io :as io]
    [clojure.java.shell :refer [sh]]))

(def parser-directory "src/clj/webchange/common/audio_parser/rhubarb/")

(defn relative->absolute-path
  [relative-path]
  (str "resources/public" relative-path))

(defn file-exist?
  [file-path]
  (->> file-path io/file .exists))

(defn get-directory
  [file-path]
  (->> file-path io/file .getParent))

(defn get-file-name
  [file-path]
  (->> file-path io/file .getName))

(defn get-result-file-path
  [audio-file-path]
  (let [directory (get-directory audio-file-path)
        audio-file-name (get-file-name audio-file-path)
        result-file-name (subs audio-file-name 0 (s/last-index-of audio-file-name "."))]
    (str directory "/" result-file-name ".json")))

(defn recognize-audio
  [file-path parser-directory]
  (let [result-file (get-result-file-path file-path)]
    (when-not (file-exist? result-file)
      (let [parser (str parser-directory "rhubarb")
            recognizer (get ["pocketSphinx" "phonetic"] 0)]
        (sh parser
            "-o" result-file
            "-r" recognizer
            "--quiet"
            "--exportFormat" "json"
            "--machineReadable"
            file-path)))
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
      (recognize-audio parser-directory)
      (read-results)
      (:mouthCues)))
