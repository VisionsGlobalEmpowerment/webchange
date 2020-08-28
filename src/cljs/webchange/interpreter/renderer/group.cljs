(ns webchange.interpreter.renderer.group
  (:require
    [cljsjs.pixi]
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.interpreter.renderer.animation :refer [create-animation]]
    [webchange.interpreter.renderer.button :refer [create-button]]
    [webchange.interpreter.renderer.image :refer [create-image]]
    [webchange.interpreter.renderer.progress-bar :refer [create-progress-bar]]
    [webchange.interpreter.renderer.rectangle :refer [create-rectangle]]
    [webchange.interpreter.renderer.slider :refer [create-slider]]
    [webchange.interpreter.renderer.text :refer [create-text]]
    [webchange.interpreter.renderer.group-wrapper :refer [wrap]]
    [webchange.interpreter.renderer.common-utils :as utils]
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

(defn create-group
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
        (case type
          "animation" (create-animation group child-props)
          "background" (create-image group child-props)
          "button" (create-button group child-props)
          "image" (create-image group child-props)
          "group" (create-group group child-props)
          "progress-bar" (create-progress-bar group child-props)
          "rectangle" (create-rectangle group child-props)
          "slider" (create-slider group child-props)
          "text" (create-text group child-props)
          (logger/warn "[Container]" (str "Object with type <" type "> can not be drawn because it is not defined")))))

    (utils/check-rest-props (str "Group <" (:object-name props) ">")
                            props
                            container-params
                            [:ref :name :object-name :on-click :children :parent])

    (re-frame/dispatch [::state/register-object wrapped-group])))
