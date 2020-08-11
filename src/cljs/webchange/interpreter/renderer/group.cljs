(ns webchange.interpreter.renderer.group
  (:require
    [cljsjs.pixi]
    [webchange.interpreter.renderer.animation :refer [create-animation]]
    [webchange.interpreter.renderer.image :refer [create-image]]
    [webchange.interpreter.renderer.group-utils :as utils]
    [webchange.interpreter.renderer.group-wrapper :refer [wrap]]
    [webchange.interpreter.renderer.common-utils :refer [get-specific-params check-rest-props check-not-updated-props]]
    [webchange.logger :as logger]))

(def Container (.. js/PIXI -Container))

(def container-params [:x :y])

(defn- get-container-params
  [props]
  (get-specific-params props container-params))

(defn- get-name
  [props]
  (str "Group <" (:name props) ">"))

(defn- create-container
  [{:keys [x y]}]
  (doto (Container.)
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
          (logger/warn "[Container]" (str "Object with type " type " can not be drawn because it is not defined")))))

    (check-rest-props (get-name props)
                      props
                      container-params
                      [:ref :name :object-name :children :parent])))


