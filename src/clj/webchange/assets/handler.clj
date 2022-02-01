(ns webchange.assets.handler
  (:require
   [buddy.auth :refer [throw-unauthorized]]
   [clojure.tools.logging :as log]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [compojure.core :refer [GET POST PUT defroutes]]
   [config.core :refer [env]]
   [ring.middleware.multipart-params :refer [wrap-multipart-params]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.util.response :refer [response]]
   [webchange.assets.core :as core]
   [webchange.auth.roles :refer [is-admin?]]
   [webchange.common.audio-parser.converter :refer [convert-to-mp3]]
   [webchange.common.audio-parser.recognizer :refer [try-recognize-audio]]
   [webchange.common.files :as f]
   [webchange.common.handler :refer [current-user handle]]
   [webchange.common.hmac-sha256 :as sign]
   [webchange.common.image-manipulation :as im]
   [webchange.common.voice-recognition.voice-recognition :refer [get-subtitles try-voice-recognition-audio]]))

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

(defn get-additional-image-params [filepath]
  (let [file (io/file filepath)]
    (when (.exists file)
      (im/get-image-info file))))

(defn get-additional-params [type filepath]
  (if (= type "image")
    (get-additional-image-params filepath)))

(defn normalize-size [size]
  (int (Math/ceil (/ size 10000))))

(defn rand-str [len]
  (apply str (take len (repeatedly #(char (+ (rand 26) 65))))))

(defn gen-filename [extension]
  (str (rand-str 16) "." extension))

(defn- convert-asset
  [relative-path extension {:strs [type blob-type options]}]
  (let [type (or (validated-type type) (get-type extension))
        upload-options (if options (edn/read-string options) {})
        convert-params (merge {:blob-type blob-type} upload-options)]
    (cond (and (= type "blob")
               (= (:blob-type convert-params) "audio"))
          ["audio" (convert-to-mp3 relative-path {:remove-origin? true})]
          (= extension "wav")
          ["audio" (convert-to-mp3 relative-path {:remove-origin? true})]
          (= type "image") (let [target (f/replace-extension relative-path "png")]
                             (im/scale-to-window
                               (f/relative->absolute-path relative-path)
                               (f/relative->absolute-path target)
                               convert-params)
                             [type target])
          :else [type relative-path])))

(defn- process-asset
  [type path {:strs [lang]}]
  (when (= type "audio")
    (future (try-recognize-audio path))
    (try-voice-recognition-audio path lang)))

(defn handle-parse-audio-subtitles
  [request]
  (let [{:strs [file]} (:query-params request)]
    (try
      (-> [true (get-subtitles file)]
          handle)
      (catch java.io.FileNotFoundException e
        (-> [false {:message "File not found"}]
            handle)))))

(defn upload-asset [{{:keys [tempfile size filename]} "file" :as params}]
  (let [extension (f/get-extension filename)
        new-name (gen-filename extension)
        path (str (env :upload-dir) (if (.endsWith (env :upload-dir) "/") "" "/") new-name)
        relative-path (str "/upload/" new-name)]
    (with-open [xin (io/input-stream tempfile)
                xout (io/output-stream path)]
      (io/copy xin xout))
    (let [[type relative-path] (convert-asset relative-path extension params)
          path (f/relative->absolute-path relative-path)]
      (process-asset type relative-path params)
      (core/store-asset-hash! path)
      (merge
        {:url  relative-path
         :type type
         :size (normalize-size size)}
        (get-additional-params type path)))))

(defn upload-asset-by-path [{{:keys [tempfile]} "file" target-path "target-path" :as request}]
  (let [full-path (f/relative->absolute-path target-path)]
    (clojure.java.io/make-parents full-path)
    (with-open [xin (io/input-stream tempfile)
                xout (io/output-stream full-path)]
      (io/copy xin xout))
    (core/store-asset-hash! full-path)))

(defn- handle-retry-voice-recognition
  [request]
  (let [user-id (current-user request)]
    (when-not (is-admin? user-id)
      (throw-unauthorized {:role :eduction}))
    (let [body (-> request :body)
          path (-> body :url)
          lang (-> body :lang)]
      (try-voice-recognition-audio path lang))
    (handle [true {:message "ok"}])))

(defroutes asset-routes
           (POST "/api/assets/" request
             (wrap-multipart-params
               (fn [request]
                 (-> request :multipart-params upload-asset response))))
  (GET "/api/actions/get-subtitles" _ (->> handle-parse-audio-subtitles wrap-params))
  (PUT "/api/assets/retry-voice-recognition" request (handle-retry-voice-recognition request)))

(defroutes asset-maintainer-routes
           (POST "/api/assets/by-path/" request
             (-> (fn [request]
                   (-> request :multipart-params upload-asset-by-path response))
                 (wrap-multipart-params)
                 (sign/wrap-api-with-signature true))))
