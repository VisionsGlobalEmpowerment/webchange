(ns webchange.interpreter.renderer.scene.components.group.component
  (:require
    [webchange.interpreter.pixi :refer [Container]]
    [webchange.interpreter.renderer.scene.components.group.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(def default-props {:x        {}
                    :y        {}
                    :ref      {}
                    :on-click {}
                    :type     {:default "group"}})

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
    (utils/set-position {:x x
                         :y y})))

(def component-type "group")

(defn create
  [{:keys [parent type ref on-click] :as props}]
  (let [group (create-container props)
        wrapped-group (wrap type (:object-name props) group)]

    (.addChild parent group)

    (when-not (nil? on-click) (utils/set-handler group "click" on-click))
    (when-not (nil? ref) (ref wrapped-group))

    wrapped-group))
