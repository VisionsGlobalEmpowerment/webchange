(ns webchange.interpreter.renderer.scene.components.timer.clock.delimiter
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

(defn- animate
  [counter text]
  (swap! counter not)
  (utils/set-visibility text @counter))

(defn delimiter
  [{:keys [style] :as props}]
  (let [counter (atom true)
        interval-id (atom nil)
        container (create-container props)
        text (create-text ":" style)
        animate-handler (partial animate counter text)]
    (.addChild container text)
    (utils/set-handler container "remove" (fn [] (js/clearInterval @interval-id)))
    {:component     container
     :set-activated (fn [value]
                      (js/clearInterval @interval-id)
                      (when value
                        (reset! interval-id (js/setInterval animate-handler 500))))}))
