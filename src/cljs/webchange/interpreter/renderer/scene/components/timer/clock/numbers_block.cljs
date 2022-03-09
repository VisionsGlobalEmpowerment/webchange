(ns webchange.interpreter.renderer.scene.components.timer.clock.numbers-block
  (:require
    [camel-snake-kebab.core :refer [->camelCase]]
    [camel-snake-kebab.extras :refer [transform-keys]]
    [webchange.interpreter.pixi :refer [Container Text]]
    [webchange.interpreter.renderer.scene.components.text.utils :refer [set-align-anchor]]
    [webchange.interpreter.renderer.scene.components.timer.clock.number :refer [number]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(defn- create-container
  [{:keys [position]}]
  (doto (Container.)
    (utils/set-position position)))

(defn- create-text
  [{:keys [align style position]}]
  (doto (Text. "" (->> style
                       (transform-keys ->camelCase)
                       (clj->js)))
    (set-align-anchor align)
    (utils/set-position position)))


(defn- set-value
  [component show-leading-zero? value]
  (let [fixed-value (-> value (Math/max 0) (Math/min 99))
        str-value (str (when (and show-leading-zero?
                                  (< fixed-value 10))
                         "0")
                       fixed-value)]
    (aset component "text" str-value)))

(defn numbers-block
  [{:keys [align show-leading-zero? style width x y]}]
  (let [container (create-container {:position {:x x :y y}})
        text (create-text {:align    align
                           :style    style
                           :position {:x (if (= align "right") width 0)}})
        set-value (partial set-value text show-leading-zero?)]
    (set-value 0)
    (.addChild container text)
    {:component container
     :set-value (fn [value]
                  {:pre [(number? value)]}
                  (set-value value))}))
