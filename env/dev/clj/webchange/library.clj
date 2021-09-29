(ns webchange.library
  (:require
    [clojure.java.io :as io]
    [clojure.tools.logging :as log]
    [webchange.assets.core :as assets]))

;(log/debug "message")

(defn- relative->absolute-path
  [relative-path]
  (str "resources/public/raw/" relative-path))

(defn- copy-file
  [source-path target-path]
  (io/make-parents target-path)
  (io/copy (io/file source-path)
           (io/file target-path)))

(defn- add-file-tags
  [file-name {:keys [tags type] :or {tags [] type "etc"}}]
  (let [[name extension] (clojure.string/split file-name #"\.")
        tags-str (clojure.string/join "-" tags)]
    (-> (clojure.string/join "--" [type tags-str name])
        (str "." extension))))

(defn- copy-files-with-tags-adding
  [source-folder target-folder tags]
  (let [source-path (relative->absolute-path source-folder)
        target-path (relative->absolute-path target-folder)]
    (doseq [file (filter (fn [f] (. f isFile)) (assets/files source-path))]
       (let [source-file-path (. file getPath)
             target-file-path (str target-path "/" (add-file-tags (. file getName) tags))]
         (copy-file source-file-path target-file-path)))))

(comment
  ;; Copy concept images to library
  (copy-files-with-tags-adding "img/elements" "clipart/elements" {:tags ["elements" "concepts"]}))
