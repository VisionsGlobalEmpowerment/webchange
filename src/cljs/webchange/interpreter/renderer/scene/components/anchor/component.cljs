(ns webchange.interpreter.renderer.scene.components.anchor.component
  (:require
    [webchange.interpreter.pixi :refer [Container Sprite Texture]]
    [webchange.interpreter.renderer.scene.components.anchor.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.resources.manager :as resources]
    [webchange.logger.index :as logger]))

(def src "/raw/img/cross.png")

(def default-props {:x   {}
                    :y   {}
                    :ref {}})

(defn- create-sprite
  [{:keys [mode object-name]}]
  (let [resource (resources/get-resource src)]
    (when (-> resource nil?)
      (logger/warn (js/Error. (str "Resources for " src " were not loaded"))))
    (doto (if (-> resource nil? not)
            (Sprite. (.-texture resource))
            (Sprite.))
      (aset "name" (str object-name "-sprite"))
      (utils/set-visibility (= mode ::modes/editor)))))

(defn- create-sprite-container
  [{:keys [x y offset scale name]
    :or   {scale  {:x 1 :y 1}
           offset {:x 0 :y 0}}}]
  (let [position {:x (- x (* (:x offset) (:x scale)))
                  :y (- y (* (:y offset) (:y scale)))}]
    (doto (Container.)
      (aset "name" (str name "-sprite-container"))
      (utils/set-position position))))

(def component-type "anchor")

(defn create
  [{:keys [parent type object-name ref] :as props}]
  (let [image (create-sprite props)
        image-container (create-sprite-container props)

        wrapped-anchor (wrap type object-name image-container)]

    (.addChild image-container image)
    (.addChild parent image-container)

    (when-not (nil? ref) (ref wrapped-anchor))

    wrapped-anchor))
