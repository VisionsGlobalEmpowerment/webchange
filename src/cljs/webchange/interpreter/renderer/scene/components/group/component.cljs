(ns webchange.interpreter.renderer.scene.components.group.component
  (:require
    [cljsjs.pixi]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.interpreter.renderer.scene.components.index :refer [components]]
    [webchange.interpreter.renderer.scene.components.group.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.logger :as logger]))

(def Container (.. js/PIXI -Container))

(def container-params [:x :y
                       {:name    :visible
                        :default true}])

(defn- get-container-params
  [props]
  (utils/get-specific-params props container-params))

(defn- create-container
  [{:keys [x y visible]}]
  (doto (Container.)
    (utils/set-visibility visible)
    (utils/set-position {:x x
                         :y y})))

(def component-type "group")

(defn create
  [parent {:keys [ref on-click children] :as props}]
  (let [group (create-container (get-container-params props))
        wrapped-group (wrap (:object-name props) group)]

    (when-not (nil? on-click) (utils/set-handler group "click" on-click))
    (when-not (nil? ref) (ref wrapped-group))

    (.addChild parent group)

    (doseq [{:keys [type] :as child} children]
      (let [child-props (-> child
                            (merge {:parent group})
                            (dissoc :type))]
        (if (= type component-type)
          (create group child-props)
          (let [constructor (get components type)]
            (if-not (nil? constructor)
              (constructor group child-props)
              (logger/warn "[Container]" (str "Object with type <" type "> can not be drawn because it is not defined")))))))

    (utils/check-rest-props (str "Group <" (:object-name props) ">")
                            props
                            container-params
                            [:ref :name :object-name :on-click :children :parent])

    (re-frame/dispatch [::state/register-object wrapped-group])))
