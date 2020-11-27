(ns webchange.assets.handler
  (:require [compojure.core :refer [GET POST PUT DELETE defroutes routes]]
            [compojure.route :refer [resources not-found]]
            [ring.util.response :refer [resource-response response redirect]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [webchange.common.handler :refer [handle current-user]]
            [ring.middleware.params :refer [wrap-params]]
            [clojure.java.io :as io]
            [mikera.image.core :as imagez]
            [webchange.assets.core :as core]
            [config.core :refer [env]]
            [clojure.edn :as edn]
            [webchange.common.hmac-sha256 :as sign]
            [ring.middleware.multipart-params :refer [wrap-multipart-params]]
            [webchange.common.audio-parser.converter :refer [convert-to-mp3 get-changed-extension]]
            [webchange.common.audio-parser.recognizer :refer [try-recognize-audio]]
            [webchange.common.voice-recognition.voice-recognition :refer [try-voice-recognition-audio get-subtitles]]
            [webchange.common.files :as f]))

(def types
  {"image" ["jpg" "jpeg" "png"]
   "audio" ["mp3" "wav" "m4a"]
   "video" ["mp4"]
   "blob"  ["blob"]})

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
    {:width  (imagez/width image)
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

(defn- convert-asset
  [relative-path type convert-params]
  (if (and (= type "blob")
           (= (:blob-type convert-params) "audio"))
    ["audio" (convert-to-mp3 relative-path {:remove-origin? true})]
    [type relative-path]))

(defn- process-asset
  [type path]
  (when (= type "audio")
    (future (try-recognize-audio path))
    (try-voice-recognition-audio path)
    ))

(defn handle-parse-audio-subtitles
  [request]
  (let [{:strs [file start duration]} (:query-params request)]
    (-> [true (get-subtitles file (edn/read-string start) (edn/read-string duration))]
        handle)))

(defn upload-asset [{{:keys [tempfile size filename]} "file" type "type" blob-type "blob-type"}]
  (let [extension (f/get-extension filename)
        new-name (gen-filename extension)
        path (str (env :upload-dir) (if (.endsWith (env :upload-dir) "/") "" "/") new-name)
        type (or (validated-type type) (get-type extension))
        params (get-additional-params type tempfile)
        relative-path (str "/upload/" new-name)]
    (try
      (with-open [xin (io/input-stream tempfile)
                  xout (io/output-stream path)]
        (io/copy xin xout))
      (let [[type relative-path] (convert-asset relative-path type {:blob-type blob-type})
            path (f/relative->absolute-path relative-path)]
        (process-asset type relative-path)
        (core/store-asset-hash! path)
        (merge
          {:url  relative-path
           :type type
           :size (normalize-size size)}
          params)))))


(defn upload-asset-by-path [{{:keys [tempfile]} "file" target-path "target-path" :as request}]
  (let [full-path (f/relative->absolute-path target-path)]
    (clojure.java.io/make-parents full-path)
    (with-open [xin (io/input-stream tempfile)
                xout (io/output-stream full-path)]
      (io/copy xin xout))
    (core/store-asset-hash! full-path)))

(defroutes asset-routes
           (POST "/api/assets/" request
             (wrap-multipart-params
               (fn [request]
                (-> request :multipart-params upload-asset response))))
           (GET "/api/actions/get-subtitles" _ (->> handle-parse-audio-subtitles wrap-params))

           )

(defroutes asset-maintainer-routes
           (POST "/api/assets/by-path/" request
             (-> (fn [request]
                   (-> request :multipart-params upload-asset-by-path response))
                 (wrap-multipart-params)
                 (sign/wrap-api-with-signature true))))
