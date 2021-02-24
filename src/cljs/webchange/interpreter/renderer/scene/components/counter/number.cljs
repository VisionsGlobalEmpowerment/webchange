(ns webchange.interpreter.renderer.scene.components.counter.number
  (:require
    [camel-snake-kebab.core :refer [->camelCase]]
    [camel-snake-kebab.extras :refer [transform-keys]]
    [webchange.interpreter.pixi :refer [Container Text]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(defn- create-container
  [{:keys [x y]
    :or   {x 0 y 0}}]
  (doto (Container.)
    (utils/set-position {:x x :y y})))

(defn- create-text
  [text style]
  (doto (Text. text (->> style (transform-keys ->camelCase) (clj->js)))))

(defn number
  [{:keys [text style] :as props}]
  (let [container (create-container props)
        text (create-text text style)]
    (.addChild container text)
    {:component container
     :set-value (fn [value] (aset text "text" value))}))
