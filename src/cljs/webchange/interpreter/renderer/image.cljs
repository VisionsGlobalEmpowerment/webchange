(ns webchange.interpreter.renderer.image
  (:require
    [cljsjs.pixi]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.interpreter.renderer.common-utils :refer [check-rest-props get-specific-params set-handler]]
    [webchange.interpreter.renderer.filters :refer [apply-filters]]
    [webchange.interpreter.renderer.image-utils :as utils]
    [webchange.interpreter.renderer.image-wrapper :refer [wrap]]
    [webchange.interpreter.resources-manager.loader :as resources]))

(def Container (.. js/PIXI -Container))
(def Sprite (.. js/PIXI -Sprite))

(def default-image-props
  {:x 0
   :y 0})

(def sprite-params [:scale :name])
(def sprite-container-params [:x :y :scale :name
                              {:name    :visible
                               :default true}
                              {:name    :offset
                               :default {:x 0 :y 0}}
                              {:name    :filters
                               :default []}])

(defn- get-sprite-params
  [props]
  (get-specific-params props sprite-params))

(defn- get-sprite-container-params
  [props]
  (get-specific-params props sprite-container-params))

(defn- get-name
  [props]
  (str "Image <" (:name props) ">"))

(defn- create-sprite
  [image-resource {:keys [scale name]}]
  (doto (Sprite. (.-texture image-resource))
    (aset "name" (str name "-sprite"))
    (utils/set-scale scale)))

(defn- create-sprite-container
  [{:keys [x y offset visible scale filters name]}]
  (let [position {:x (- x (* (:x offset) (:x scale)))
                  :y (- y (* (:y offset) (:y scale)))}]
    (doto (Container.)
      (aset "name" (str name "-sprite-container"))
      (utils/set-position position)
      (utils/set-visibility visible)
      (apply-filters filters))))

(defn create-image
  [parent props]
  (let [{:keys [src on-click ref]} props
        resource (resources/get-resource src)]
    (when (nil? resource)
      (throw (js/Error. (str "Resources for '" src "' were not loaded"))))
    (let [image (create-sprite resource (get-sprite-params props))
          image-container (create-sprite-container (get-sprite-container-params props))
          wrapped-image (wrap (:object-name props) image-container image-container)]

      (when-not (nil? on-click) (set-handler image "click" on-click))
      (when-not (nil? ref) (ref wrapped-image))

      (.addChild image-container image)
      (.addChild parent image-container)

      (check-rest-props (get-name props)
                        props
                        sprite-params
                        sprite-container-params
                        [:name :object-name :on-click :parent :ref :src])

      (re-frame/dispatch [::state/register-object wrapped-image]))))
