(ns webchange.interpreter.renderer.scene.components.background.component
  (:require
    [webchange.interpreter.renderer.pixi :refer [Container Sprite]]
    [webchange.interpreter.renderer.resources :as resources]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]
    [webchange.interpreter.renderer.scene.components.background.wrapper :refer [wrap]]))

(def default-props {:name    {}
                    :src     {:default nil}
                    :filters {:default []}})


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
  [parent {:keys [type object-name] :as props}]
  (let [image (create-sprite props)]
    (.addChild parent image)
    (wrap type object-name)))
