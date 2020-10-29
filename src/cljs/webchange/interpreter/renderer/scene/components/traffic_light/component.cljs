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

(defn- create-light
  [{:keys [src scale mask-y mask-height]}]
  (let [resource (resources/get-resource src)
        sprite (doto (Sprite. (.-texture resource))
                 (utils/set-scale scale)
                 (utils/set-position {:x 10 :y 0}))
        mask (doto (Graphics.)
               (.beginFill 0x000000)
               (.drawRect 0 mask-y 300 mask-height)
               (.endFill 0x000000))
        container (doto (Container.)
                    (utils/set-position {:x 0 :y 0}))]
    (aset sprite "mask" mask)
    (.addChild container sprite)
    (.addChild container mask)

    (utils/set-visibility container false)                  ;; Make invisible by default

    container))

(def component-type "traffic-light")

(defn create
  "Create `traffic-light` component.

  Props params:
  :x - component x-position.
  :y - component y-position.
  :scale - image scale. Default: {:x 1 :y 1}."
  [{:keys [parent type object-name scale] :as props}]
  (let [src "/images/demo/traffic-light.png"
        sprite (create-sprite "/images/demo/traffic-light.png" props)
        mask (create-mask {:width 380 :height 830})
        container (create-container props)

        red-light (create-light {:src         src
                                 :scale       scale
                                 :mask-y      30
                                 :mask-height 240})
        yellow-light (create-light {:src         src
                                    :scale       scale
                                    :mask-y      270
                                    :mask-height 240})
        green-light (create-light {:src         src
                                   :scale       scale
                                   :mask-y      510
                                   :mask-height 240})]

    (aset sprite "mask" mask)
    (.addChild container sprite)
    (.addChild container mask)
    (.addChild parent container)

    (.addChild container red-light)
    (.addChild container yellow-light)
    (.addChild container green-light)

    (wrap type object-name container {:red    red-light
                                      :yellow yellow-light
                                      :green  green-light})))
