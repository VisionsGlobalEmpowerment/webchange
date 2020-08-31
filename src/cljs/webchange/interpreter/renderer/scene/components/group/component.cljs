(ns webchange.interpreter.renderer.scene.components.group.component
  (:require
    [cljsjs.pixi]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.interpreter.renderer.scene.components.index :refer [components]]
    [webchange.interpreter.renderer.scene.components.group.wrapper :refer [wrap]]
    [webchange.interpreter.renderer.scene.components.props-utils :refer [get-props]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(def Container (.. js/PIXI -Container))

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

(defn- create-child-components
  [type group child-props]
  (let [{:keys [constructor default-props]} (get components type)]
    (when (nil? constructor)
      (-> (str "Object with type <" type "> can not be drawn because it is not defined") js/Error. throw))
    (when (nil? default-props)
      (-> (str "Default props for <" type "> are not defined") js/Error. throw))
    (let [component-wrapper (constructor group (get-props type child-props default-props))]
      (when (nil? component-wrapper)
        (-> (str "Constructor for <" type "> did not return component wrapper") js/Error. throw))
      (re-frame/dispatch [::state/register-object component-wrapper]))))

(def component-type "group")

(defn create
  [parent current-props]
  (let [{:keys [type ref on-click children] :as props} (get-props component-type current-props default-props)
        group (create-container props)
        wrapped-group (wrap type (:object-name props) group)]

    (when-not (nil? on-click) (utils/set-handler group "click" on-click))
    (when-not (nil? ref) (ref wrapped-group))

    (.addChild parent group)

    (doseq [{:keys [type] :as child} children]
      (let [child-props (assoc child :parent group)]
        (if (= type component-type)
          (create group (get-props type child-props default-props))
          (create-child-components type group child-props))))

    (re-frame/dispatch [::state/register-object wrapped-group])))
