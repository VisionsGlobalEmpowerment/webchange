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

(defn- get-dimensions
  [show-minutes? text-style]
  (let [delimiter-width (:width (measure ":" text-style))
        minutes-width (:width (measure "00" text-style))]
    (if show-minutes?
      {:delimiter-x    minutes-width
       :seconds-x      (+ minutes-width delimiter-width)
       :component-size (measure "00:00" text-style)}
      {:delimiter-x    0
       :seconds-x      delimiter-width
       :component-size (measure ":00" text-style)})))

(defn clock
  [{:keys [minutes seconds show-minutes? font-size] :as props}]
  (let [container (create-container props)
        text-style (get-text-style props)
        numbers-padding (:width (measure "0" text-style))
        component-props {:padding numbers-padding
                         :style   text-style}
        {:keys [delimiter-x seconds-x component-size]} (get-dimensions show-minutes? text-style)
        {minutes :component set-minutes :set-value} (numbers-block (merge component-props {:x 0 :value minutes}))
        {delimiter :component set-activated :set-activated} (delimiter (merge component-props {:x delimiter-x :y (+ (* 0.15 font-size) -5.2)}))
        {seconds :component set-seconds :set-value} (numbers-block (merge component-props {:x seconds-x :value seconds}))]
    (when show-minutes? (.addChild container minutes))
    (.addChild container delimiter)
    (.addChild container seconds)
    {:component     container
     :size          component-size
     :set-activated set-activated
     :set-value     (fn [{:keys [minutes seconds]}]
                      (when (and show-minutes? (some? minutes)) (set-minutes minutes))
                      (when (some? seconds) (set-seconds seconds)))}))
