(ns webchange.interpreter.renderer.scene.components.traffic-light.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.resources.manager :as resources]
    [webchange.interpreter.renderer.scene.components.traffic-light.wrapper :refer [wrap]]))

(def default-props {:x     {}
                    :y     {}
                    :scale {:default {:x 1 :y 1}}})

(defn- create-sprite
  [src {:keys [scale]}]
  (let [resource (resources/get-resource src)]
    (when (-> resource nil?)
      (-> (str "Resources for '" src "' were not loaded") js/Error. throw))
    (doto (Sprite. (.-texture resource))
      (utils/set-scale scale)
      (utils/set-position {:x -380 :y 0}))))

(defn- create-mask
  [{:keys [width height]}]
  (doto (Graphics.)
    (.beginFill 0x000000)
    (.drawRect 0 0 width height)
    (.endFill 0x000000)))

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x :y y})))

(def component-type "traffic-light")

(defn create
  [{:keys [parent type object-name] :as props}]
  (let [sprite (create-sprite "/images/demo/traffic-light.png" props)
        mask (create-mask {:width 380 :height 830})
        container (create-container props)]

    (aset sprite "mask" mask)
    (.addChild container sprite)
    (.addChild container mask)
    (.addChild parent container)

    (wrap type object-name container)))
