(ns webchange.common.audio-parser.converter
  (:require
    [clojure.java.io :as io]
    [clojure.string :as s]
    [webchange.common.files :as f]
    [clojure.java.shell :refer [sh]]))

(defn delete-file
  [file-path]
  (let [delete-silently? true]
    (when (f/file-exist? file-path)
      (io/delete-file file-path delete-silently?))))

(defn get-changed-extension
  [audio-file-path extension]
  (let [directory (f/get-directory audio-file-path)
        audio-file-name (f/get-file-name audio-file-path)
        result-file-name (subs audio-file-name 0 (s/last-index-of audio-file-name "."))]
    (str directory "/" result-file-name "." extension)))

(defn- convert-to
  [file-path extension {:keys [remove-origin? absolute-path?]}]
  (let [converted-file-path (get-changed-extension file-path extension)
        origin-path (if absolute-path? file-path (f/relative->absolute-path file-path))
        target-path (if absolute-path? converted-file-path (f/relative->absolute-path converted-file-path))]
    (when-not (f/file-exist? converted-file-path)
      (let [result (sh "ffmpeg" "-i" origin-path target-path)]
        (if (= (:exit result) 1)
          (throw (Exception. (:err result)))
          (when remove-origin?
            (delete-file origin-path)))))
    converted-file-path))

(defn convert-to-wav
  ([file-path]
   (convert-to file-path "wav" {}))
  ([file-path options]
   (convert-to file-path "wav" options)))

(defn convert-to-mp3
  ([file-path]
   (convert-to file-path "mp3" {}))
  ([file-path options]
   (convert-to file-path "mp3" options)))
