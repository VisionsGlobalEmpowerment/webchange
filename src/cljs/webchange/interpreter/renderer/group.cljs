(ns webchange.interpreter.renderer.group
  (:require
    [cljsjs.pixi]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.interpreter.renderer.animation :refer [create-animation]]
    [webchange.interpreter.renderer.image :refer [create-image]]
    [webchange.interpreter.renderer.slider :refer [create-slider]]
    [webchange.interpreter.renderer.group-utils :as utils]
    [webchange.interpreter.renderer.group-wrapper :refer [wrap]]
    [webchange.interpreter.renderer.common-utils :refer [get-specific-params check-rest-props]]
    [webchange.logger :as logger]))

(def Container (.. js/PIXI -Container))

(def container-params [:x :y
                       {:name    :visible
                        :default true}])

(defn- get-container-params
  [props]
  (get-specific-params props container-params))

(defn- get-name
  [props]
  (str "Group <" (:name props) ">"))

(defn- create-container
  [{:keys [x y visible]}]
  (doto (Container.)
    (utils/set-visibility visible)
    (utils/set-position {:x x
                         :y y})))

(defn create-group
  [parent {:keys [ref children] :as props}]
  (let [group (create-container (get-container-params props))
        wrapped-group (wrap (:object-name props) group)]

    (when-not (nil? ref) (ref wrapped-group))

    (.addChild parent group)

    (doseq [{:keys [type] :as child} children]
      (let [child-props (-> child
                            (merge {:parent group})
                            (dissoc :type))]
        (case type
          "animation" (create-animation group child-props)
          "background" (create-image group child-props)
          "image" (create-image group child-props)
          "group" (create-group group child-props)
          "slider" (create-slider group child-props)
          (logger/warn "[Container]" (str "Object with type " type " can not be drawn because it is not defined")))))

    (check-rest-props (get-name props)
                      props
                      container-params
                      [:ref :name :object-name :children :parent])

    (re-frame/dispatch [::state/register-object wrapped-group])))
