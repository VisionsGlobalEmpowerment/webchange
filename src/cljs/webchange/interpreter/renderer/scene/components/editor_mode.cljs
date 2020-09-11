(ns webchange.interpreter.renderer.scene.components.editor-mode
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor.events :as events]
    [webchange.editor-v2.scene.state.skin :as skin]
    [webchange.interpreter.renderer.pixi :refer [Container]]
    [webchange.interpreter.renderer.scene.components.dragging :refer [enable-drag!]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.filters.filters :refer [apply-outline-filter]]))

(defn- handle-frame-click
  [component]
  (re-frame/dispatch [::events/select-current-scene-object (:object-name component)]))

(defn- handle-drag
  [container]
  (let [{:keys [x y]} (utils/get-position container)]
    (re-frame/dispatch [::skin/change-position x y])))

(defn- create-frame
  [{:keys [parent x y] :as props}]
  (let [container (doto (Container.)
                    (utils/set-position {:x x :y y}))]
    (apply-outline-filter container {:width 3})
    (utils/set-handler container "click" #(handle-frame-click props))
    (enable-drag! container #(handle-drag container))
    (.addChild parent container)))

(defn- add-editor-frame
  [{:keys [editable?] :as props}]
  (if editable?
    (let [component-frame (create-frame props)]
      (-> props
          (assoc :x 0)
          (assoc :y 0)
          (assoc :parent component-frame)))
    props))

(defn- modify-common-props
  [props]
  (-> props
      (assoc :visible true)
      (dissoc :filters)
      (dissoc :on-click)))

(defn enable
  [{:keys [type] :as props}]
  (-> (case type
        "animation" (-> props
                        (add-editor-frame)
                        (assoc :start false))
        props)
      (modify-common-props)))
