(ns webchange.interpreter.renderer.scene.components.group.component
  (:require
    [webchange.interpreter.renderer.pixi :refer [Container]]
    [webchange.interpreter.renderer.scene.components.group.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(def default-props {:x        {}
                    :y        {}
                    :ref      {}
                    :on-click {}
                    :type     {:default "group"}
                    :visible  {:default true}})

(defn- create-container
  [{:keys [x y visible]}]
  (doto (Container.)
    (utils/set-visibility visible)
    (utils/set-position {:x x
                         :y y})))

(def component-type "group")

(defn create
  [parent {:keys [type ref on-click] :as props}]
  (let [group (create-container props)
        wrapped-group (wrap type (:object-name props) group)]

    (when-not (nil? on-click) (utils/set-handler group "click" on-click))
    (when-not (nil? ref) (ref wrapped-group))

    (.addChild parent group)

    wrapped-group))
