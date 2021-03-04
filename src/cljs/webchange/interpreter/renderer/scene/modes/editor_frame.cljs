(ns webchange.interpreter.renderer.scene.modes.editor-frame
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.change-skin.state :as skin]
    [webchange.interpreter.pixi :refer [Container Graphics Rectangle Sprite WHITE]]
    [webchange.interpreter.renderer.state.editor :as editor]
    [webchange.interpreter.renderer.scene.components.dragging :refer [enable-drag!]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.logger.index :as logger]))

(def frame-width 10)
(def frame-default-color 0x008000)
(def frame-selected-color 0xFFA500)

(defn- handle-frame-click
  [component]
  (re-frame/dispatch [::editor/select-object (:object-name component)]))

(defn- handle-drag
  [container]
  (let [{:keys [x y]} (utils/get-position container)]
    (re-frame/dispatch [::skin/change-position x y])))

(defn- wrap
  [name sprite]
  {:name     name
   :select   (fn [] (aset sprite "tint" frame-selected-color))
   :deselect (fn [] (aset sprite "tint" frame-default-color))})

(defn- draw-border
  [mask width height]
  (let [d (/ frame-width 2)]
    (doto mask
      (.clear)
      (.lineStyle frame-width 0xffffff)
      (.moveTo 0 d)
      (.lineTo (- width d) d)
      (.lineTo (- width d) (- height d))
      (.lineTo d (- height d))
      (.lineTo d 0))))

(defn- create-frame
  [component-container object-props object]
  (let [{:keys [x y width height]} (let [local-bounds (.getLocalBounds object)]
                                     {:x      (.-x local-bounds)
                                      :y      (.-y local-bounds)
                                      :width  (.-width local-bounds)
                                      :height (.-height local-bounds)})
        sprite (doto (Sprite. WHITE)
                 (aset "tint" frame-default-color)
                 (aset "width" width)
                 (aset "height" height)
                 (utils/set-position {:x x :y y}))
        mask (doto (Graphics.)
               (draw-border width height)
               (utils/set-position {:x x :y y}))]
    (if (instance? Container object)
      (utils/set-handler object "childAdded"
                         #(let [local-bounds (.getLocalBounds object)
                                width (.-width local-bounds)
                                height (.-height local-bounds)]
                            + (doto sprite
                                (aset "width" width)
                                (aset "height" height))
                            (draw-border mask width height)
                            (aset component-container "hitArea" (Rectangle. x y width height)))))

    (aset sprite "mask" mask)
    (aset component-container "hitArea" (Rectangle. x y width height))
    (.addChild component-container mask)
    (.addChild component-container sprite)
    (re-frame/dispatch [::editor/register-object (wrap (:object-name object-props) sprite)])))

(defn- create-editor-container
  [{:keys [parent x y] :as props}]
  (let [container (doto (Container.)
                    (utils/set-position {:x x :y y}))]
    (utils/set-handler container "click" #(handle-frame-click props))
    (enable-drag! container #(handle-drag container))
    (.addChild parent container)
    container))

(defn add-editor-frame
  [object {:keys [editable?] :as props}]
  (when editable?
    (logger/trace "add editor frame" (:object-name props))
    (let [component-container (create-editor-container props)
          current-parent (.-parent object)]
      (.removeChild current-parent object)
      (.addChild current-parent component-container)
      (.addChild component-container object)
      (utils/set-position object 0 0)
      (create-frame component-container props object))))
