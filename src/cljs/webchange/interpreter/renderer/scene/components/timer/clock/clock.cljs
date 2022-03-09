(ns webchange.interpreter.renderer.scene.components.timer.clock.clock
  (:require
    [webchange.interpreter.pixi :refer [Container]]
    [webchange.interpreter.renderer.scene.components.timer.clock.delimiter :refer [delimiter]]
    [webchange.interpreter.renderer.scene.components.timer.clock.numbers-block :refer [numbers-block]]
    [webchange.interpreter.renderer.scene.components.timer.clock.utils :refer [measure]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(defn get-text-style
  [{:keys [color font-family font-size font-weight]}]
  (cond-> {:fill        color
           :font-family "Luckiest Guy"
           :font-size   68
           :font-weight "normal"}
          font-family (assoc :font-family font-family)
          font-size (assoc :font-size font-size)
          font-weight (assoc :font-weight font-weight)))

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x
                         :y y})))

(defn clock
  [{:keys [show-leading-zero? show-minutes? font-size] :as props}]
  (let [container (create-container props)
        text-style (get-text-style props)
        numbers-padding (:width (measure "0" text-style))
        component-props {:padding            numbers-padding
                         :show-leading-zero? show-leading-zero?
                         :style              text-style}

        {block-width :width} (measure "00" text-style)
        {delimiter-width :width} (measure ":" text-style)

        {minutes :component set-minutes :set-value} (numbers-block {:x                  0
                                                                    :y                  0
                                                                    :width              block-width
                                                                    :align              "right"
                                                                    :show-leading-zero? false
                                                                    :style              text-style})
        {seconds :component set-seconds :set-value} (numbers-block {:x                  (+ block-width delimiter-width)
                                                                    :y                  0
                                                                    :width              block-width
                                                                    :align              "left"
                                                                    :show-leading-zero? show-leading-zero?
                                                                    :style              text-style})

        {delimiter :component set-activated :set-activated} (delimiter (merge component-props
                                                                              {:x block-width
                                                                               :y (+ (* 0.15 font-size) -5.2)}))]
    (when show-minutes?
      (.addChild container delimiter)
      (.addChild container minutes))
    (.addChild container seconds)
    {:component     container
     :set-activated set-activated
     :set-value     (fn [{:keys [minutes seconds]}]
                      (when (and show-minutes? (some? minutes)) (set-minutes minutes {:keys show-leading-zero?}))
                      (when (some? seconds) (set-seconds seconds show-leading-zero?)))}))
