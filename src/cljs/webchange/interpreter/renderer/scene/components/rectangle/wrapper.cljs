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
      (.beginFill 0x000000))

    (if circle?
      (.drawCircle mask (/ (.-width bounds) 2) (/ (.-height bounds) 2) border-radius)
      (.drawRoundedRect mask 0 0 (.-width bounds) (.-height bounds) border-radius))

    (.endFill mask 0x000000)

    mask))

(defn wrap
  [type name state object sprite mask props]
  (create-wrapper {:name                 name
                   :type                 type
                   :object               object
                   :set-fill             #(aset sprite "tint" %)
                   :set-border-color     #(do
                                            (f/set-filter sprite "" {})
                                            (f/set-filter sprite "outline" {:color   % :width (:border-width props)
                                                                            :quality 1}))
                   :set-on-click-handler #(utils/set-handler object "click" %)
                   :get-wrapped-props    (fn []
                                           [:border-radius])
                   :get-prop             (fn [prop-name]
                                           (case prop-name
                                             :border-radius (get-border-radius state)))
                   :set-prop             (fn [prop-name prop-value]
                                           (case prop-name
                                             :border-radius (set-border-radius mask prop-value state)))}))
