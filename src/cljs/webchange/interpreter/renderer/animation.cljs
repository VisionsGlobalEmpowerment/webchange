(ns webchange.interpreter.renderer.animation
  (:require
    [cljsjs.pixi]
    [pixi-spine]
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.interpreter.renderer.animation-utils :as utils]
    [webchange.interpreter.renderer.animation-wrapper :refer [wrap]]
    [webchange.interpreter.renderer.common-utils :refer [get-specific-params check-rest-props set-handler check-not-updated-props]]
    [webchange.interpreter.resources-manager.loader :as resources]))

(def Container (.. js/PIXI -Container))
(def Spine (.. js/PIXI -spine -Spine))

(def spine-animation-params [:offset :scale
                             {:name  :animation-start?
                              :alias :start}
                             {:name    :speed
                              :default 1}
                             {:name  :skin-name
                              :alias :skin}
                             {:name  :animation-name
                              :alias :anim}
                             {:name  :position
                              :alias :anim-offset}])
(def animation-container-params [:x :y])

(defn- get-spine-animation-params
  [props]
  (get-specific-params props spine-animation-params))

(defn- get-animation-container-params
  [props]
  (get-specific-params props animation-container-params))

(defn- create-spine-animation
  [animation-resource {:keys [animation-start? speed offset position skin-name animation-name scale]}]
  (let [spine-data (.-spineData animation-resource)
        coordinates {:x (* (- (:x position) (:x offset)) (:x scale))
                     :y (* (- (:y position) (:y offset)) (:y scale))}]
    (doto (Spine. spine-data)
      (utils/set-skin skin-name)
      (utils/set-animation animation-name)
      (utils/set-position coordinates)
      (utils/set-scale scale)
      (utils/set-animation-speed (if animation-start? speed 0)))))

(defn- create-animation-container
  [{:keys [x y]}]
  (let [position {:x x
                  :y y}]
    (doto (Container.)
      (utils/set-position position))))

(defn- get-name
  [props]
  (str "Animation <" (:name props) ">"))

(defn create-animation
  [parent props]
  (let [{:keys [name on-click on-mount ref] :as props} props
        resource (resources/get-resource name)]
    (when (nil? resource)
      (-> (str "Resource for animation <" name "> is not defined") js/Error. throw))
    (let [animation (create-spine-animation resource (get-spine-animation-params props))
          animation-container (create-animation-container (get-animation-container-params props))
          wrapped-animation (wrap (:object-name props) animation-container animation)]

      (when-not (nil? on-click) (set-handler animation "click" on-click))
      (when-not (nil? on-mount) (on-mount wrapped-animation))
      (when-not (nil? ref) (ref wrapped-animation))

      (.addChild animation-container animation)
      (.addChild parent animation-container)

      (check-rest-props (get-name props)
                        props
                        spine-animation-params
                        animation-container-params
                        [:name :object-name :on-click :on-mount :parent :ref])

      (re-frame/dispatch [::state/register-object wrapped-animation]))))
