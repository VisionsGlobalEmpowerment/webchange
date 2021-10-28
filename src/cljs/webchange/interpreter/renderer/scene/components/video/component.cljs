(ns webchange.interpreter.renderer.scene.components.video.component
  (:require
    [webchange.interpreter.pixi :refer [Sprite]]
    [webchange.resources.manager :as resources]
    [webchange.interpreter.renderer.scene.components.video.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.video.utils :as video-utils]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(def default-props {:x      {}
                    :y      {}
                    :src    {}
                    :width  {}
                    :height {}
                    :volume {}})

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
  "Create `video` component.

  Props params:
  :x - component x-position.
  :y - component y-position.
  :width - image width.
  :height - image height.
  :src - image src. Default: nil."
  [{:keys [parent type object-name] :as props}]
  (let [sprite (create-sprite props)
        state (atom {:props props})]
    (.addChild parent sprite)
    (wrap type object-name sprite state)))
