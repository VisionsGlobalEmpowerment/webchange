(ns webchange.interpreter.renderer.scene.components.counter.component
  (:require
    [webchange.interpreter.pixi :refer [Container Text]]
    [webchange.interpreter.renderer.scene.components.counter.number :refer [number]]
    [webchange.interpreter.renderer.scene.components.counter.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(def default-props {:x          {}
                    :y          {}
                    :init-value {:default 0}
                    :digits     {:default 1}
                    :color      {:default 0xff9000}
                    :ref        {}})

(defn get-text-style
  [{:keys [color]}]
  {:fill        color
   :font-family "Luckiest Guy"
   :font-size   68
   :font-weight "normal"})

(def component-type "counter")

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x
                         :y y})))

(defn create
  [{:keys [parent type ref object-name init-value] :as props}]
  (let [state (atom {:value init-value})
        container (create-container props)
        text-style (get-text-style props)
        {:keys [component set-value]} (number {:x 0 :text init-value :style text-style})
        wrapped-counter (wrap type object-name container state {:set-value set-value})]
    (.addChild container component)
    (.addChild parent container)

    (when-not (nil? ref) (ref wrapped-counter))

    wrapped-counter))
