(ns webchange.interpreter.renderer.scene.components.timer.clock.numbers-block
  (:require
    [webchange.interpreter.pixi :refer [Container]]
    [webchange.interpreter.renderer.scene.components.timer.clock.number :refer [number]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})))

(defn- set-value
  [set-left-component-value set-right-component-value value]
  (let [fixed-value (-> value (Math/max 0) (Math/min 99))
        left-value (quot fixed-value 10)
        right-value (mod fixed-value 10)]
    (set-left-component-value left-value)
    (set-right-component-value right-value)))

(defn numbers-block
  [{:keys [padding style value] :as props
    :or   {value 0}}]
  (let [container (create-container props)
        {left-component :component set-left-component-value :set-value} (number {:x 0 :text 0 :style style})
        {right-component :component set-right-component-value :set-value} (number {:x padding :text 0 :style style})
        set-value (partial set-value set-left-component-value set-right-component-value)]
    (set-value value)
    (.addChild container left-component)
    (.addChild container right-component)
    {:component container
     :set-value (fn [value]
                  {:pre [(number? value)]}
                  (set-value value))}))
