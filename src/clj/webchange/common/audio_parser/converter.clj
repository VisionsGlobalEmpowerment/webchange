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
        result-file-name (f/get-file-name-without-extension audio-file-path)]
    (str directory "/" result-file-name "." extension)))

(defn- convert-to
  [file-path extension {:keys [remove-origin? absolute-path? sample-rate mono?]}]
  (let [converted-file-path (get-changed-extension file-path extension)
        origin-path (if absolute-path? file-path (f/relative->absolute-path file-path))
        target-path (if absolute-path? converted-file-path (f/relative->absolute-path converted-file-path))
        additional-options (cond-> []
                                   sample-rate (concat ["-ar" (str sample-rate)])
                                   mono? (concat ["-ac" (str 1)]))]
    (when-not (f/file-exist? converted-file-path)
      (let [result (apply sh (vec (concat ["ffmpeg" "-i" origin-path] additional-options [target-path])))]
        (if (not= (:exit result) 0)
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
