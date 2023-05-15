(ns webchange.capacitor
  (:require
    ["@capacitor/core" :refer [Capacitor]]
    ["@capacitor/filesystem" :refer [Filesystem Directory Encoding]]))

(def DirectoryData (.. Directory -Data))
(def EncodingUTF8 (.. Encoding -UTF8))

(defn native?
  []
  (Capacitor.isNativePlatform))

(comment
  )

(defn read-file
  [filename]
  (.readFile Filesystem #js {:path filename
                             :directory DirectoryData
                             :encoding EncodingUTF8}))

(defn write-file
  [filename data]
  (.writeFile Filesystem #js {:path filename
                              :data data
                              :directory DirectoryData
                              :encoding EncodingUTF8}))
