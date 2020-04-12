(ns webchange.assets.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [clojure.tools.logging :as log]
            [webchange.common.handler :refer [handle current-user]]
            [webchange.auth.core :as auth]
            [clojure.java.io :as io]
            [mikera.image.core :as imagez]
            [config.core :refer [env]]
            [webchange.common.audio-parser.recognizer :refer [try-recognize-audio]]))

(def types
  {"image" ["jpg" "jpeg" "png"]
   "audio" ["mp3" "wav" "m4a"]
   "video" ["mp4"]})

(defn get-extension [filename]
  (-> filename
      (clojure.string/split #"\.")
      last
      clojure.string/lower-case))

(defn get-type [extension]
  (let [match? (fn [es] (some #(= extension %) es))]
    (->> types
         (filter #(match? (second %)))
         first
         first)))

(defn validated-type
  [type]
  (if (contains? types type)
    type))

(defn get-additional-image-params [file]
  (let [image (imagez/load-image file)]
    {:width (imagez/width image)
     :height (imagez/height image)}))

(defn get-additional-params [type file]
  (if (= type "image")
    (get-additional-image-params file)))

(defn normalize-size [size]
  (int (Math/ceil (/ size 10000))))

(defn rand-str [len]
  (apply str (take len (repeatedly #(char (+ (rand 26) 65))))))

(defn gen-filename [extension]
  (str (rand-str 16) "." extension))

(defn- process-asset
  [type path]
  (case type
    "audio" (future (try-recognize-audio path))))

(defn upload-asset [{{:keys [tempfile size filename]} "file" type "type"}]
  (let [extension (get-extension filename)
        new-name (gen-filename extension)
        path (str (env :upload-dir) (if (.endsWith (env :upload-dir) "/") "" "/") new-name)
        type (or (validated-type type) (get-type extension))
        params (get-additional-params type tempfile)
        relative-path (str "/upload/" new-name)]
    (try
      (with-open [xin (io/input-stream tempfile)
                  xout (io/output-stream path)]
        (io/copy xin xout)
        (process-asset type relative-path)
        (merge
          {:url relative-path
           :type type
           :size (normalize-size size)}
          params)))))

(defroutes asset-routes
           (POST "/api/assets/" request
             (-> request :multipart-params upload-asset response)))
