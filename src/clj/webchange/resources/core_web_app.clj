(ns webchange.resources.core-web-app
  (:require
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [clojure.string :as s]
    [config.core :refer [env]]
    [webchange.interpreter.defaults :as defaults]))

(def prefix "resources/")
(def public-fold "public/")
(def js-fold (str public-fold "js/compiled/"))
(def css-fold (str public-fold "css/"))
(def img-fold (str public-fold "images/"))

(defn- remove-prefix
  [s]
  (let [value (str prefix public-fold)
        pos (s/index-of s value)
        len (count value)]
    (str "/" (subs s (+ pos len)))))

(defn- get-files-list
  [dir]
  (if-let [url (io/resource dir)]
    (->> url
         (.getPath)
         (clojure.java.io/file)
         (file-seq)
         (filter #(.isFile %))
         (mapv str)
         (map remove-prefix))))

(defn get-resources-file-path
  []
  "sw/web_app_resources.edn")

(defn generate-app-resources
  []
  (->> ["/manifest.json"
        "/page-skeleton"
        (get-files-list (str js-fold "app.js"))
        (get-files-list (str js-fold "out/"))
        (get-files-list css-fold)
        (get-files-list img-fold)
        (->> defaults/default-assets (map :url))
        "https://fonts.googleapis.com/css?family=Luckiest+Guy"
        "https://fonts.googleapis.com/css?family=Lexend+Deca"
        "https://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic&subset=latin"
        "https://fonts.gstatic.com/s/lato/v15/S6u9w4BMUTPHh6UVSwiPGQ.woff2"
        "https://fonts.gstatic.com/s/lexenddeca/v1/K2F1fZFYk-dHSE0UPPuwQ5qnJy8.woff2"
        "//cdn.jsdelivr.net/npm/semantic-ui@2.4.2/dist/semantic.min.css"]
       (flatten)
       (remove nil?)
       (vec)))

(defn- get-resources-list
  []
  (let [path (get-resources-file-path)]
    (-> path io/resource io/reader java.io.PushbackReader. edn/read)))

(defn- get-resources
  []
  (if (env :dev?)
    (generate-app-resources)
    (get-resources-list)))

(defn get-web-app-resources
  []
  {:resources (get-resources)
   :endpoints []})
