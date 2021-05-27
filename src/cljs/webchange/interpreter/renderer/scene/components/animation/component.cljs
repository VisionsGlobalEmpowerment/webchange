(ns webchange.interpreter.renderer.scene.components.animation.component
  (:require
    [webchange.interpreter.pixi :refer [Container Spine]]
    [webchange.interpreter.renderer.scene.components.animation.utils :as utils]
    [webchange.interpreter.renderer.scene.components.animation.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :refer [set-handler]]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]
    [webchange.resources.manager :as resources]))

(def default-props {:x                {}
                    :y                {}
                    :offset           {}
                    :on-mount         {}
                    :ref              {}
                    :name             {}
                    :start            {}
                    :anim             {}
                    :skin             {}
                    :on-click         {}
                    :scale            {:default {:x 1 :y 1}}
                    :loop             {:default true}
                    :animation-start? {:alias :start}
                    :speed            {:default 1}
                    :skin-name        {:alias :skin :default "default"}
                    :animation-name   {:alias :anim}
                    :position         {:alias :anim-offset}
                    :filters          {}})

(defn- create-spine-animation
  [animation-resource {:keys [animation-start? speed offset position skin-name animation-name scale loop]}]
  (let [spine-data (.-spineData animation-resource)
        coordinates {:x (* (- (:x position) (:x offset)) (:x scale))
                     :y (* (- (:y position) (:y offset)) (:y scale))}]
    (doto (Spine. spine-data)
      (utils/set-skin skin-name)
      (utils/set-animation animation-name)
      (utils/set-position coordinates)
      (utils/set-scale scale)
      (utils/set-animation-speed speed)
      (utils/set-auto-update animation-start?)
      (utils/set-track-loop loop))))

(defn- create-animation-container
  [{:keys [x y]}]
  (let [position {:x x
                  :y y}]
    (doto (Container.)
      (utils/set-position position))))

(def component-type "animation")

(defn create
  "Create `animation` component. This component play animation from skeleton file for corresponding object.

  Props params:
  :x - component x-position. This is container position.
  :y - component y-position. This is container position.
  :offset - This is offset for animation position inside container.
  :position - This is position for animation position inside container.
  :scale - scale of animation. Default: {:x 1 :y 1}.
  Three previous props connected with coordinates in following order: (position.x - offset.x) * scale.x, (position.y - offset.y) * scale.y
  :name - component name that will be set to sprite and container with corresponding suffixes.
  :on-click - on click event handler.
  :start - if animation should start automatically
  :loop - Should animation start after end in loop. Default true
  :animation-start? - Alias for :start
  :ref - callback function that must be called with component wrapper.
  :anim - animation name
  :animation-name - Alias for :anim
  :on-mount - on mount event handler.
  :skin - object skin which should be used.
  :speed - animation speed
  :skin-name - alias for :skin. Default default
  "
  [{:keys [parent name on-click on-mount ref type filters] :as props}]
  (let [resource (resources/get-resource name)]
    (when (nil? resource)
      (-> (str "Resource for animation <" name "> is not defined") js/Error. throw))
    (let [animation (create-spine-animation resource props)
          animation-container (create-animation-container props)
          wrapped-animation (wrap type (:object-name props) animation-container animation)]

      (.addChild animation-container animation)
      (.addChild parent animation-container)

      (when-not (nil? on-click) (set-handler animation "click" on-click))
      (when-not (nil? on-mount) (on-mount wrapped-animation))
      (when-not (nil? ref) (ref wrapped-animation))

      (-> animation
          (.update 0))

      (apply-filters animation-container filters)

      wrapped-animation)))
