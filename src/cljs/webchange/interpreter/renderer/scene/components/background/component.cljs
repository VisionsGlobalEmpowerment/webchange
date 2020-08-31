(ns webchange.interpreter.renderer.scene.components.background.component
  (:require
    [cljsjs.pixi]
    [webchange.interpreter.renderer.scene.components.utils :as utils :refer [check-rest-props get-specific-params]]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]
    [webchange.interpreter.renderer.resources :as resources]))

(def Container (.. js/PIXI -Container))
(def Sprite (.. js/PIXI -Sprite))

(def default-params {:name :name
                     :src  {:name    :src
                            :default nil}
                     :filters {:name    :filters
                               :default []}})

(def sprite-params (utils/pick-params default-params [:src :filters]))

(defn- create-sprite
  [{:keys [src filters]}]
  (let [resource (resources/get-resource src)]
    (when (and (-> resource nil?)
               (-> src nil? not))
      (throw (js/Error. (str "Resources for '" src "' were not loaded"))))
    (let [sprite (if (-> resource nil? not)
                   (Sprite. (.-texture resource))
                   (Sprite.))]
      (apply-filters sprite filters)
      sprite)))

(def component-type "background")

(defn create
  [parent {:keys [object-name] :as props}]
  (let [image (create-sprite (utils/get-specific-params props sprite-params))]
    (.addChild parent image)
    (check-rest-props (str "Background <" object-name ">")
                      props
                      sprite-params
                      [:name :object-name :parent])))
