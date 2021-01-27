(ns webchange.interpreter.renderer.scene.components.image.component
  (:require
    [webchange.interpreter.pixi :refer [Container Graphics Sprite WHITE]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]
    [webchange.interpreter.renderer.scene.components.image.utils :as image-utils]
    [webchange.interpreter.renderer.scene.components.image.wrapper :refer [wrap]]
    [webchange.resources.manager :as resources]
    [webchange.logger :as logger]))

(def default-props {:x             {}
                    :y             {}
                    :width         {}
                    :height        {}
                    :scale         {}
                    :name          {}
                    :on-click      {}
                    :ref           {}
                    :src           {:default nil}
                    :offset        {:default {:x 0 :y 0}}
                    :filters       {:default []}
                    :border-radius {}
                    :origin        {}
                    :image-size    {}
                    :max-width     {}
                    :max-height    {}
                    :min-width     {}
                    :min-height    {}})

(defn- create-sprite
  [{:keys [src object-name]}]
  (let [resource (resources/get-resource src)]
    (when (and (-> resource nil?)
               (-> src nil? not)
               (-> src empty? not))
      (logger/warn (js/Error. (str "Resources for " src " were not loaded"))))
    (doto (if (-> resource nil? not)
            (Sprite. (.-texture resource))
            (Sprite.))
      (aset "name" (str object-name "-sprite")))))

(defn- create-sprite-mask
  [{:keys [border-radius width height image-size]}]
  (cond
    (some? border-radius) (let [[lt rt rb lb] border-radius]
                            (doto (Graphics.)
                              (.beginFill 0x000000)
                              (.moveTo lt 0)
                              (.lineTo (- width rt) 0)      ; top
                              (.arc (- width rt) rt rt (* 0.75 Math/PI) 0) ; top-right
                              (.lineTo width (- height rb)) ; right
                              (.arc (- width rb) (- height rb) rb 0 (* 0.5 Math/PI)) ; bottom-right
                              (.lineTo lb height)           ; bottom
                              (.arc lb (- height lb) lb (* 0.5 Math/PI) Math/PI) ; bottom-left
                              (.lineTo 0 lt)                ; left
                              (.arc lt lt lt Math/PI (* 0.75 Math/PI)) ; top-left
                              (.endFill 0x000000)))
    (and (some? image-size)
         (some? width)
         (some? height)) (doto (Graphics.)
                           (.beginFill 0x000000)
                           (.drawRect 0 0 width height)
                           (.endFill 0x000000))))

(defn- create-sprite-container
  [{:keys [x y offset scale filters name]
    :or   {scale {:x 1 :y 1}}}]
  (let [position {:x (- x (* (:x offset) (:x scale)))
                  :y (- y (* (:y offset) (:y scale)))}]
    (doto (Container.)
      (aset "name" (str name "-sprite-container"))
      (utils/set-position position)
      (apply-filters filters))))

(def component-type "image")

(defn create
  "Create `image` component.

  Props params:
  :x - component x-position.
  :y - component y-position.
  :width - image width.
  :height - image height.
  :scale - image scale.
  :name - component name that will be set to sprite and container with corresponding suffixes.
  :on-click - on click event handler.
  :ref - callback function that must be called with component wrapper.
  :src - image src. Default: nil.
  :offset - container position offset. Default: {:x 0 :y 0}.
  :filters - filters params to be applied to sprite. Default: [].
  :border-radius - make rounded corners. Radius in pixels. Default: 0.
  :origin - where image pivot will be set. Can be '(left|center|right)-(top|center|bottom)'. Must be a value of `:type` field.
            Default: 'left-top'. Example: {:type 'center-center'}
  :max-width - max image width.
  :max-height - max image height.
  :min-width - min image width.
  :min-height - min image height."
  [{:keys [parent type on-click ref object-name] :as props}]
  (let [state (atom props)
        image (create-sprite props)
        image-mask (create-sprite-mask props)
        image-container (create-sprite-container props)
        wrapped-image (wrap type object-name image-container image state)]
    (.addChild image-container image)
    (.addChild parent image-container)

    (when (some? image-mask)
      (.addChild image-container image-mask)
      (aset image "mask" image-mask))

    (when-not (nil? on-click) (utils/set-handler image "click" on-click))
    (when-not (nil? ref) (ref wrapped-image))

    (image-utils/set-image-size image props)
    (image-utils/apply-origin image-container props)
    (image-utils/apply-boundaries image-container props)

    wrapped-image))
