(ns webchange.common.files
  (:require
    [clojure.java.io :as io]
    [ring.util.codec :as codec]
    [config.core :refer [env]]))

(defn relative->absolute-path
  ([relative-path] (relative->absolute-path relative-path env))
  ([relative-path config]
   (str (config :public-dir) relative-path)))

(defn encode-path [path]
  (clojure.string/join "/"
    (map #(codec/url-encode %)
      (clojure.string/split path #"/"))))

(defn relative->absolute-primary-uri
  [relative-path]
   (if (clojure.string/starts-with? relative-path "/")
     (let [relative-path (clojure.string/replace-first relative-path "/" "")]
       (str (:host-url (:secondary env)) (encode-path relative-path))
       )
     (str (:host-url (:secondary env)) (encode-path relative-path))
   ))


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
