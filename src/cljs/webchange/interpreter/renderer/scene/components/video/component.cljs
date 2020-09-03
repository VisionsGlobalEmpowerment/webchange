(ns webchange.interpreter.renderer.scene.components.video.component
  (:require
    [webchange.interpreter.renderer.pixi :refer [Sprite]]
    [webchange.interpreter.renderer.resources :as resources]
    [webchange.interpreter.renderer.scene.components.video.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(def default-props {:x      {}
                    :y      {}
                    :src    {}
                    :width  {}
                    :height {}})

(defn- create-sprite
  [{:keys [src x y width height]}]
  (let [resource (resources/get-resource src)]
    (when (and (-> resource nil?)
               (-> src nil? not))
      (.log js/console (js/Error. (str "Resources for '" src "' were not loaded"))))
    (let [sprite (if (-> resource nil? not)
                   (Sprite. (.-texture resource))
                   (Sprite.))]
      (doto sprite
        (utils/set-position {:x x :y y})
        (utils/set-not-nil-value "width" width)
        (utils/set-not-nil-value "height" height)))))

(def component-type "video")

(defn create
  [parent {:keys [type object-name] :as props}]
  (let [sprite (create-sprite props)]
    (.addChild parent sprite)
    (wrap type object-name sprite)))
