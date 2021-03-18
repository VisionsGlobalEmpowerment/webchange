(ns webchange.interpreter.renderer.scene.modes.editor-frame
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.change-skin.state :as skin]
    [webchange.interpreter.pixi :refer [Container Graphics Rectangle Sprite WHITE]]
    [webchange.interpreter.renderer.state.editor :as editor]
    [webchange.interpreter.renderer.scene.components.dragging :refer [enable-drag!]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.logger.index :as logger]))

(def frame-width 5)
(def frame-padding 30)
(def frame-default-color 0x59acff)
(def frame-selected-color 0xFFA500)

(defn- handle-frame-click
  [component]
  (re-frame/dispatch [::editor/select-object (:object-name component)]))

(defn- handle-drag
  [container]
  (let [{:keys [x y] :as position} (utils/get-position container)]
    (logger/trace-folded "change position on drag end" position)
    (re-frame/dispatch [::skin/change-position x y])))

(defn- wrap
  [name sprite]
  {:name     name
   :select   (fn [] (aset sprite "tint" frame-selected-color))
   :deselect (fn [] (aset sprite "tint" frame-default-color))})

(defn- draw-border
  [mask width height padding]
  (let [top-left {:x frame-width
                  :y frame-width}
        top-right {:x (+ (- width frame-width) (* padding 2))
                   :y frame-width}
        bottom-right {:x (+ (- width frame-width) (* padding 2))
                      :y (+ (- height frame-width) (* padding 2))}
        bottom-left {:x frame-width
                     :y (+ (- height frame-width) (* padding 2))}]
    (doto mask
      (.clear)
      (.lineStyle frame-width 0xffffff)
      (.moveTo (:x top-left) (:y top-left))
      (.lineTo (:x top-right) (:y top-right))
      (.lineTo (:x bottom-right) (:y bottom-right))
      (.lineTo (:x bottom-left) (:y bottom-left))
      (.lineTo (:x top-left) (- (:y top-left) (/ frame-width 2))))))

(defn- create-frame
  [component-container object-props object]
  (let [{:keys [x y width height]} (let [local-bounds (.getLocalBounds object)]
                                     {:x      (.-x local-bounds)
                                      :y      (.-y local-bounds)
                                      :width  (.-width local-bounds)
                                      :height (.-height local-bounds)})
        sprite (doto (Sprite. WHITE)
                 (aset "tint" frame-default-color)
                 (aset "width" (+ width (* 2 (+ frame-padding frame-width))))
                 (aset "height" (+ height (* 2 (+ frame-padding frame-width))))
                 (utils/set-position {:x (- x frame-padding frame-width)
                                      :y (- y frame-padding frame-width)}))
        mask (doto (Graphics.)
               (draw-border width height frame-padding)
               (utils/set-position {:x (- x frame-padding)
                                    :y (- y frame-padding)}))]
    (if (instance? Container object)
      (utils/set-handler object "childAdded"
                         #(let [local-bounds (.getLocalBounds object)
                                width (.-width local-bounds)
                                height (.-height local-bounds)]
                            + (doto sprite
                                (aset "width" width)
                                (aset "height" height))
                            (draw-border mask width height frame-padding)
                            (aset component-container "hitArea" (Rectangle. x y width height)))))

    (aset sprite "mask" mask)
    (aset component-container "hitArea" (Rectangle. x y width height))
    (.addChild component-container mask)
    (.addChild component-container sprite)
    (re-frame/dispatch [::editor/register-object (wrap (:object-name object-props) sprite)])))

(defn- selectable?
  [{:keys [editable?]}]
  (or (= editable? true)
      (= (:select editable?) true)))

(defn- draggable?
  [{:keys [editable?]}]
  (or (= editable? true)
      (= (:drag editable?) true)))

(defn- create-editor-container
  [props]
  (let [container (Container.)]

    (when (selectable? props) (utils/set-handler container "click" #(handle-frame-click props)))
    (when (draggable? props) (enable-drag! container #(handle-drag container)))

    container))

(defn- wrap-in-container
  [object props]
  (let [container (create-editor-container props)
        current-parent (.-parent object)]
    (.removeChild current-parent object)
    (.addChild current-parent container)
    (.addChild container object)

    (utils/set-position container (utils/get-position object))
    (utils/set-position object 0 0)

    container))

(defn add-editor-frame
  [object {:keys [editable?] :as props}]
  (when editable?
    (logger/trace-folded ["add editor frame for" (:object-name props)] props object)
    (let [container (wrap-in-container object props)]
      (create-frame container props object))))
