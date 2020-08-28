(ns webchange.interpreter.renderer.image
  (:require
    [cljsjs.pixi]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.interpreter.renderer.common-utils :as utils :refer [check-rest-props get-specific-params set-handler]]
    [webchange.interpreter.renderer.filters :refer [apply-filters]]
    [webchange.interpreter.renderer.image-wrapper :refer [wrap]]
    [webchange.interpreter.resources-manager.loader :as resources]))

(def Container (.. js/PIXI -Container))
(def Sprite (.. js/PIXI -Sprite))

(def default-params {:x       :x
                     :y       :y
                     :width   :width
                     :height  :height
                     :scale   :scale
                     :name    :name
                     :src     {:name    :src
                               :default nil}
                     :visible {:name    :visible
                               :default true}
                     :offset  {:name    :offset
                               :default {:x 0 :y 0}}
                     :filters {:name    :filters
                               :default []}})

(def sprite-params (utils/pick-params default-params [:src :scale :name :width :height]))
(def sprite-container-params (utils/pick-params default-params [:x :y :scale :name :visible :offset :filters]))

(defn- create-sprite
  [{:keys [src scale name width height]}]
  (let [resource (resources/get-resource src)]
    (when (and (-> resource nil?)
               (-> src nil? not))
      (throw (js/Error. (str "Resources for '" src "' were not loaded"))))
    (let [sprite (if (-> resource nil? not)
                   (Sprite. (.-texture resource))
                   (Sprite.))]
      (doto sprite
        (aset "name" (str name "-sprite"))
        (utils/set-not-nil-value "width" width)
        (utils/set-not-nil-value "height" height)
        (utils/set-scale scale)))))

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
  [parent {:keys [on-click ref object-name] :as props}]
  (let [image (create-sprite (utils/get-specific-params props sprite-params))
        image-container (create-sprite-container (utils/get-specific-params props sprite-container-params))
        wrapped-image (wrap object-name image-container image-container)]

    (when-not (nil? on-click) (set-handler image "click" on-click))
    (when-not (nil? ref) (ref wrapped-image))

    (.addChild image-container image)
    (.addChild parent image-container)

    (check-rest-props (str "Image <" object-name ">")
                      props
                      sprite-params
                      sprite-container-params
                      [:name :object-name :on-click :parent :ref :src])

    (re-frame/dispatch [::state/register-object wrapped-image])))
