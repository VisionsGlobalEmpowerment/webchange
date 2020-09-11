(ns webchange.interpreter.renderer.scene.components.editor-mode
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor.events :as events]
    [webchange.editor-v2.scene.state.skin :as skin]
    [webchange.interpreter.renderer.pixi :refer [Container Graphics Rectangle Sprite WHITE]]
    [webchange.interpreter.renderer.scene.components.dragging :refer [enable-drag!]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(defn- handle-frame-click
  [component]
  (re-frame/dispatch [::events/select-current-scene-object (:object-name component)]))

(defn- handle-drag
  [container]
  (let [{:keys [x y]} (utils/get-position container)]
    (re-frame/dispatch [::skin/change-position x y])))

(defn- create-frame
  [component-container wrapper]
  (let [{:keys [x y width height]} (let [local-bounds (.getLocalBounds (:object wrapper))]
                                     {:x      (.-x local-bounds)
                                      :y      (.-y local-bounds)
                                      :width  (.-width local-bounds)
                                      :height (.-height local-bounds)})
        frame-color 0x008000
        frame-width 10
        sprite (doto (Sprite. WHITE)
                 (aset "tint" frame-color)
                 (aset "width" width)
                 (aset "height" height)
                 (utils/set-position {:x x :y y}))
        mask (let [d (/ frame-width 2)]
               (doto (Graphics.)
               (.lineStyle frame-width 0xffffff)
               (.moveTo 0 d)
               (.lineTo (- width d) d)
               (.lineTo (- width d) (- height d))
               (.lineTo d (- height d))
               (.lineTo d 0)
               (utils/set-position {:x x :y y})))]
    (aset sprite "mask" mask)
    (aset component-container "hitArea" (Rectangle. x y width height))
    (.addChild component-container mask)
    (.addChild component-container sprite)))

(defn- create-editor-container
  [{:keys [parent x y] :as props}]
  (let [container (doto (Container.)
                    (utils/set-position {:x x :y y}))]
    (utils/set-handler container "click" #(handle-frame-click props))
    (enable-drag! container #(handle-drag container))
    (.addChild parent container)))

(defn- add-editor-frame
  [{:keys [editable?] :as props}]
  (if editable?
    (let [component-container (create-editor-container props)]
      (-> props
          (assoc :x 0)
          (assoc :y 0)
          (assoc :parent component-container)
          (assoc :ref #(create-frame component-container %))))
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
