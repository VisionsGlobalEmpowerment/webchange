(ns webchange.interpreter.renderer.scene.components.image.component
  (:require
    [webchange.interpreter.pixi :refer [Container Sprite]]
    [webchange.interpreter.renderer.scene.components.utils :as utils :refer [set-handler]]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]
    [webchange.interpreter.renderer.scene.components.image.wrapper :refer [wrap]]
    [webchange.resources.manager :as resources]))

(def default-props {:x        {}
                    :y        {}
                    :width    {}
                    :height   {}
                    :scale    {:default {:x 1 :y 1}}
                    :name     {}
                    :on-click {}
                    :ref      {}
                    :src      {:default nil}
                    :offset   {:default {:x 0 :y 0}}
                    :filters  {:default []}})

(defn- create-sprite
  [{:keys [src scale name width height]}]
  (let [resource (resources/get-resource src)]
    (when (and (-> resource nil?)
               (-> src nil? not)
               (-> src empty? not))
      (.log js/console (js/Error. (str "Resources for '" src "' were not loaded"))))
    (let [sprite (if (-> resource nil? not)
                   (Sprite. (.-texture resource))
                   (Sprite.))]
      (doto sprite
        (aset "name" (str name "-sprite"))
        (utils/set-not-nil-value "width" width)
        (utils/set-not-nil-value "height" height)
        (utils/set-scale scale)))))

(defn- create-sprite-container
  [{:keys [x y offset scale filters name]}]
  (let [position {:x (- x (* (:x offset) (:x scale)))
                  :y (- y (* (:y offset) (:y scale)))}]
    (doto (Container.)
      (aset "name" (str name "-sprite-container"))
      (utils/set-position position)
      (apply-filters filters))))

(def component-type "image")

(defn create
  [{:keys [parent type on-click ref object-name] :as props}]
  (let [image (create-sprite props)
        image-container (create-sprite-container props)
        wrapped-image (wrap type object-name image-container image)]

    (.addChild image-container image)
    (.addChild parent image-container)

    (when-not (nil? on-click) (set-handler image "click" on-click))
    (when-not (nil? ref) (ref wrapped-image))

    wrapped-image))
