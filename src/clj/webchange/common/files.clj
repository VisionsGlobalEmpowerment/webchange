(ns webchange.common.files
  (:require
    [clojure.java.io :as io]
    [clj-http.client :as client]
    [ring.util.codec :as codec]
    [cemerick.url :refer (url url-encode)]
    [config.core :refer [env]])
  (:import (java.io File)
           [java.util.zip ZipInputStream]
           ))

(defn relative->absolute-path
  ([relative-path] (relative->absolute-path relative-path env))
  ([relative-path config]
   (str (config :public-dir) relative-path)))

(defn save-file-from-uri [uri file]
  (->
    (client/get uri {:as :stream})
    (:body)
    (clojure.java.io/copy (clojure.java.io/file file))))

(defn relative->absolute-url
  ([base-url relative-path]
   (-> (url base-url)
       (assoc :path relative-path)
       str)))

(defn replace-extension
  [filename new-extension]
  (-> filename
      (clojure.string/split #"\.")
      (drop-last)
      (vec)
      (conj new-extension)
      (#(clojure.string/join "." %))))

(defn get-extension [filename]
  (-> filename
      (clojure.string/split #"\.")
      last
      clojure.string/lower-case))

(defn encode-path [path]
  (clojure.string/join "/"
    (map #(codec/url-encode %)
      (clojure.string/split path #"/"))))

(defn relative->absolute-primary-uri
  [relative-path]
  (relative->absolute-url (:host-url (:secondary env)) (encode-path relative-path)))

(defn file-exist?
  [file-path]
  (->> file-path io/file .exists))

(defn get-directory
  [file-path]
  (->> file-path io/file .getParent))

(defn get-file-name
  [file-path]
  (->> file-path io/file .getName))

(defn get-file-name-without-extension
  [file-path]
  (let [file-name (get-file-name file-path)
        file-name (subs file-name 0 (clojure.string/last-index-of file-name "."))]
    file-name))

(defn create-new
  [file-path]
  (->> file-path io/file .createNewFile))

(defn unzip-file
  [input output]
  (with-open [stream (-> input io/input-stream ZipInputStream.)]
    (loop [entry (.getNextEntry stream)]
      (if entry
        (let [save-path (str output File/separatorChar (.getName entry))
              out-file (File. save-path)]
          (if (.isDirectory entry)
            (if-not (.exists out-file)
              (.mkdirs out-file))
            (let [parent-dir (File. (.substring save-path 0 (.lastIndexOf save-path (int File/separatorChar))))]
              (if-not (.exists parent-dir) (.mkdirs parent-dir))
              (clojure.java.io/copy stream out-file)))
          (recur (.getNextEntry stream)))))))

(defn get-first-directory
  [dir]
  (.getPath (first (filter #(.isDirectory %) (.listFiles (io/file dir))))))

(defn move
  [source target]
  (.renameTo (io/file source) (io/file (str target))))

(defn delete-recursively
  [directory]
  (doseq [f (reverse (file-seq (clojure.java.io/file directory)))]
    (clojure.java.io/delete-file f true)))
