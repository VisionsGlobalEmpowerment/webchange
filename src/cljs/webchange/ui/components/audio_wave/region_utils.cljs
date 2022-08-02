(ns webchange.ui.components.audio-wave.region-utils
  (:require
    [webchange.ui.components.audio-wave.config :as config]
    [webchange.ui.components.audio-wave.region-core :as core]))

(def get-end core/get-end)
(def get-start core/get-start)
(def play core/play)
(def remove core/remove)

(defn set-style
  [region region-style]
  (core/set-props region region-style))

(defn set-default-style
  [region]
  (let [region-style (-> (config/get-config :region))]
    (set-style region region-style)))
