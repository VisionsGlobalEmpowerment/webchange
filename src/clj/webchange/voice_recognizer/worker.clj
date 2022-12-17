(ns webchange.voice-recognizer.worker
  (:require [clojure.java.io :as io]
            [config.core :refer [env]]
            [clojure.tools.logging :as log]
            [webchange.common.files :as files]
            [webchange.common.audio-parser.converter :as converter]
            [webchange.voice-recognizer.recognizers.kaldi :as kaldi]
            [webchange.voice-recognizer.recognizers.google :as google]))

(def recognizer (atom nil))

(def recognizers {:kaldi {:init! kaldi/init!
                          :recognize kaldi/recognize}
                  :google {:init! google/init-credentials!
                           :recognize google/recognize}})

(defn download-file
  [file-path]
  (let [url (files/relative->absolute-url (:core-http-url env) file-path)
        extension (files/get-extension file-path)
        name (.toString (java.util.UUID/randomUUID))
        local-file-path (str "/tmp/" name "." extension)]
    (files/save-file-from-uri url local-file-path)
    local-file-path))

(defn worker
  [{:keys [file-path model]}]
  (let [_ (log/debug "Downloading file...")
        local-file-path (download-file file-path)
        _ (log/debug "Converting file...")
        local-file-path (converter/convert-to-wav local-file-path {:remove-origin? true
                                                                   :absolute-path? true
                                                                   :mono?          true
                                                                   :sample-rate    16000})
        input (io/input-stream local-file-path)
        result (@recognizer input model)]
    (io/delete-file local-file-path true)
    (log/debug "Recognized, sending result...")
    (assoc result :file-path file-path :model model)))

(defn init-recognizer!
  [recognizer-name]
  (let [{:keys [init! recognize]} (get recognizers recognizer-name)]
    (init!)
    (reset! recognizer recognize)))
