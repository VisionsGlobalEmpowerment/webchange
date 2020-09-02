(ns webchange.interpreter.renderer.scene.components.layered-background.component
  (:require
    [webchange.interpreter.renderer.pixi :refer [Container Sprite]]
    [webchange.interpreter.renderer.resources :as resources]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-filters]]
    [webchange.interpreter.renderer.scene.components.layered-background.wrapper :refer [wrap]]))

(def default-props {:name    {}
                    :background {:default nil}
                    :decoration {:default nil}
                    :surface {:default nil}
                    :filters {:default []}})


(defn- create-sprite
  [layer-name {:keys [filters] :as props}]
  (if-let [src (get-in props [layer-name :src])]
    (let [resource (resources/get-resource src)]
      (when (and (-> resource nil?)
                 (-> src nil? not))
        (throw (js/Error. (str "Resources for '" src "' were not loaded"))))
      (let [sprite (if (-> resource nil? not)
                     (Sprite. (.-texture resource))
                     (Sprite.))]
        (apply-filters sprite filters)
        sprite))))

(def component-type "layered-background")

(defn create
  [parent {:keys [type object-name] :as props}]
  (if-let [background (create-sprite :background props)]
    (.addChild parent background))
  (if-let [background (create-sprite :surface props)]
    (.addChild parent background))
  (if-let [background (create-sprite :decoration props)]
    (.addChild parent background))
  (wrap type object-name))
