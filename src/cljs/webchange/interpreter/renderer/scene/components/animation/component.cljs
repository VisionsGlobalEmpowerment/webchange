(ns webchange.interpreter.renderer.scene.components.animation.component
  (:require
    [webchange.interpreter.renderer.pixi :refer [Container Spine]]
    [webchange.interpreter.renderer.scene.components.animation.utils :as utils]
    [webchange.interpreter.renderer.scene.components.animation.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :refer [set-handler]]
    [webchange.interpreter.renderer.resources :as resources]))

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
                    :position         {:alias :anim-offset}})

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
  [{:keys [parent name on-click on-mount ref type] :as props}]
  (let [resource (resources/get-resource name)]
    (when (nil? resource)
      (-> (str "Resource for animation <" name "> is not defined") js/Error. throw))
    (let [animation (create-spine-animation resource props)
          animation-container (create-animation-container props)
          wrapped-animation (wrap type (:object-name props) animation-container animation)]

      (when-not (nil? on-click) (set-handler animation "click" on-click))
      (when-not (nil? on-mount) (on-mount wrapped-animation))
      (when-not (nil? ref) (ref wrapped-animation))

      (.addChild animation-container animation)
      (.addChild parent animation-container)

      (-> animation
          (.update 0))

      wrapped-animation)))
