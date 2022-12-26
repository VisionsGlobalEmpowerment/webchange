(ns webchange.ui.components.audio-wave.region-utils
  (:require
    [webchange.ui.components.audio-wave.config :as config]
    [webchange.ui.components.audio-wave.region-core :as core]))

(def get-end core/get-end)
(def get-start core/get-start)
(def play core/play)
(def remove core/remove)
(def get-id core/get-id)
(def get-data core/get-data)
(def set-data core/set-data)

(defn set-prop
  [region prop-name prop-value]
  (aset region prop-name prop-value))

(defn set-props
  [region props]
  (doseq [[prop-name prop-value] props]
    (set-prop region prop-name (clj->js prop-value))))

(defn set-style
  [region region-style]
  (set-props region region-style))

(defn set-default-style
  [region]
  (let [region-style (-> (config/get-config :region))]
    (set-style region region-style)))

(defn set-bounds
  [region {:keys [start end]}]
  (core/set-start region start)
  (core/set-end region end)
  (core/update-render region))

(defn compare-regions
  [one another]
  (compare (get-start one) (get-start another)))

(defn get-duration
  [region]
  (- (get-end region) (get-start region)))

(defn set-changed
  [region value]
  (let [data (-> region
                 get-data
                 (assoc :changed value))]
    (set-data region data)))

(defn is-changed?
  [region]
  (-> region
      get-data
      (get :changed false)))
