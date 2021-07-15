(ns webchange.interpreter.renderer.scene.modes.editor-frame
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor.events :as edit-scene]
    [webchange.interpreter.pixi :refer [Container Graphics Rectangle Sprite WHITE Text]]
    [webchange.interpreter.renderer.state.editor :as editor]
    [webchange.interpreter.renderer.scene.components.dragging :refer [enable-drag!]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.logger.index :as logger]
    [webchange.subs :as subs]))

(def frame-width 5)
(def frame-padding 10)
(def frame-default-color 0x59acff)
(def frame-selected-color 0xFFA500)

(re-frame/reg-event-fx
  ::change-position
  (fn [{:keys [db]} [_ x y]]
    (let [name (editor/selected-object db)
          current-scene (subs/current-scene db)
          state (-> (subs/scene-object db current-scene name)
                    (assoc :x x :y y))]
      {:dispatch-n (list [::edit-scene/update-object {:scene-id current-scene
                                                      :target   name
                                                      :state    state}]
                         [::edit-scene/update-current-scene-object {:target name
                                                                    :state  state}]
                         [::edit-scene/save-current-scene current-scene])})))

(defn- handle-frame-click
  [component]
  (re-frame/dispatch [::editor/select-object (:object-name component)]))

(defn- handle-drag
  [container]
  (let [{:keys [x y] :as position} (utils/get-position container)]
    (logger/trace-folded "change position on drag end" position)
    (re-frame/dispatch [::change-position x y])))

(defn- wrap
  [name sprite]
  {:name     name
   :hide     (fn [] (aset sprite "visible" false))
   :show     (fn [] (aset sprite "visible" true))
   :select   (fn [] (aset sprite "tint" frame-selected-color))
   :deselect (fn [] (aset sprite "tint" frame-default-color))})

(defn- get-object-local-bounds
  [object]
  (let [bounds (.getLocalBounds object)]
    {:x      (.-x bounds)
     :y      (.-y bounds)
     :width  (.-width bounds)
     :height (.-height bounds)}))

(defn- get-frame-position
  [object object-props]
  (let [local-bounds (get-object-local-bounds object)
        [origin-x origin-y] (-> (get-in object-props [:origin :type] "none-none")
                                (clojure.string/split #"-"))]
    (cond-> local-bounds
            (= origin-x "center") (update :x - (/ (:width local-bounds) 2))
            (= origin-y "center") (update :y - (/ (:height local-bounds) 2)))))

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

(defn- get-sprite-dimensions
  [object object-props]
  (let [{:keys [x y width height]} (get-frame-position object object-props)]
    {:x      (- x frame-padding frame-width)
     :y      (- y frame-padding frame-width)
     :width  (+ width (* 2 (+ frame-padding frame-width)))
     :height (+ height (* 2 (+ frame-padding frame-width)))}))

(defn- set-sprite-dimensions!
  [sprite {:keys [x y width height]}]
  (doto sprite
    (aset "width" (+ width (* 2 (+ frame-padding frame-width))))
    (aset "height" (+ height (* 2 (+ frame-padding frame-width))))
    (utils/set-position {:x (- x frame-padding frame-width)
                         :y (- y frame-padding frame-width)})))

(defn- update-sprite-dimensions!
  [sprite object object-props]
  (->> (get-sprite-dimensions object object-props)
       (set-sprite-dimensions! sprite)))

(defn- get-mask-dimensions
  [object object-props]
  (let [{:keys [x y]} (get-frame-position object object-props)]
    {:x (- x frame-padding)
     :y (- y frame-padding)}))

(defn- set-mask-dimensions!
  [mask position]
  (utils/set-position mask position))

(defn- update-mask-dimensions!
  [mask object object-props]
  (->> (get-mask-dimensions object object-props)
       (set-mask-dimensions! mask)))

(defn- update-editor-frame
  [object object-props sprite mask component-container]
  (let [{:keys [x y width height]} (get-frame-position object object-props)]
    (update-sprite-dimensions! sprite object object-props)

    (doto mask
      (draw-border width height frame-padding)
      (update-mask-dimensions! object object-props))

    (draw-border mask width height frame-padding)
    (aset component-container "hitArea" (Rectangle. x y width height))))

(defn- create-frame
  [component-container object-props object]
  (let [{:keys [x y width height]} (get-frame-position object object-props)
        sprite (doto (Sprite. WHITE)
                 (aset "tint" frame-default-color)
                 (update-sprite-dimensions! object object-props))

        mask (doto (Graphics.)
               (update-mask-dimensions! object object-props)
               (draw-border width height frame-padding))]

    (when (instance? Container object)
      (utils/set-handler object "childAdded" #(update-editor-frame object object-props sprite mask component-container))
      (utils/set-handler object "visibilityChanged" (fn [visible?] (utils/set-visibility component-container visible?))))

    (when (instance? Text object)
      (utils/set-handler object "textChanged" #(update-editor-frame object object-props sprite mask component-container))
      (utils/set-handler object "fontSizeChanged" #(update-editor-frame object object-props sprite mask component-container))
      (utils/set-handler object "fontFamilyChanged" #(update-editor-frame object object-props sprite mask component-container)))

    (print "object-props" object-props)

    (when (some? (:visible object-props))
      (utils/set-visibility component-container (:visible object-props)))

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
        current-parent (.-parent object)
        index (.getChildIndex current-parent object)]
    (.addChildAt current-parent container index)
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
