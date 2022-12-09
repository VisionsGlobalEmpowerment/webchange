(ns webchange.interpreter.renderer.scene.components.rectangle.wrapper
  (:require
    [webchange.interpreter.renderer.scene.filters.filters :as f]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn- get-border-radius
  [state]
  (:border-radius @state))

(defn set-border-radius
  [mask border-radius state]
  (swap! state assoc :border-radius border-radius)
  (let [bounds (.getLocalBounds mask)
        circle? (and (= (.-width bounds) (.-height bounds))
                     (= border-radius (/ (.-width bounds) 2)))]
    (doto mask
      (.clear)
      (.beginFill 0x000000 1))

    (if circle?
      (.drawCircle mask (/ (.-width bounds) 2) (/ (.-height bounds) 2) border-radius)
      (.drawRoundedRect mask 0 0 (.-width bounds) (.-height bounds) border-radius))

    (.endFill mask 0x000000)

    mask))

(defn- update-border
  [sprite props]
  (f/set-filter sprite "" {})
  (f/set-filter sprite "outline" {:color   (:border-color props)
                                  :width   (:border-width props)
                                  :quality 1}))

(defn wrap
  [type name state object sprite mask]
  (create-wrapper {:name                 name
                   :type                 type
                   :object               object
                   :set-highlight           (fn [highlight]
                                              (let [highlight-filter-set (f/has-filter-by-name object "glow")]
                                                (when (and (not highlight) highlight-filter-set)
                                                  (f/set-filter object "" {}))
                                                (when (and highlight (not highlight-filter-set))
                                                  (f/set-filter object "glow" {}))))
                   :set-fill             (fn [color]
                                           (aset sprite "tint" color))
                   :get-fill             (fn []
                                           (aget sprite "tint"))
                   :set-border-color     #(do
                                            (swap! state assoc :border-color %)
                                            (update-border sprite @state))
                   :set-border-width     (fn [border-width]
                                           (swap! state assoc :border-width border-width)
                                           (update-border sprite @state))
                   :set-on-click-handler #(utils/set-handler object "click" %)
                   :get-wrapped-props    (fn []
                                           [:border-radius])
                   :get-prop             (fn [prop-name]
                                           (case prop-name
                                             :border-radius (get-border-radius state)))
                   :set-prop             (fn [prop-name prop-value]
                                           (case prop-name
                                             :border-radius (set-border-radius mask prop-value state)))}))
