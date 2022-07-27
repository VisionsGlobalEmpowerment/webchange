(ns webchange.ui.components.audio-wave.audio-loader
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [cljs.core.async :refer [<!]]
    [cljs-http.client :as http]))

(def resources-url "")

(def audios (atom {}))
(defn put-audio [key data] (swap! audios assoc (name key) data) data)
(defn get-audio [key] (get @audios key))
(defn has-audio? [key] (contains? @audios key))

(defn- get-blob
  [array-buffer]
  (js/Blob. [array-buffer]))

(defn- load-audio
  [url callback]
  (go (let [response (<! (http/get (str resources-url url)
                                   {:response-type     :array-buffer
                                    :with-credentials? false}))]
        (->> (:body response)
             (get-blob)
             (put-audio url)
             (callback)))))

(defn get-audio-blob
  [key callback]
  (if (has-audio? key)
    (-> (get-audio key)
        (callback))
    (load-audio key callback)))
