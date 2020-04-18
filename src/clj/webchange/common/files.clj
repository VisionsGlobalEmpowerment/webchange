(ns webchange.common.files
  (:require
    [clojure.java.io :as io]
    [config.core :refer [env]]))

(defn relative->absolute-path
  ([relative-path] (relative->absolute-path relative-path env))
  ([relative-path config]
   (str (config :public-dir) relative-path)))


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
