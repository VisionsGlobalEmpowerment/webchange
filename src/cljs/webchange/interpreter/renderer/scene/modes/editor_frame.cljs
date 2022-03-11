(ns webchange.interpreter.renderer.scene.modes.editor-frame
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor.events :as edit-scene]
    [webchange.interpreter.pixi :refer [Container Graphics Rectangle Sprite WHITE Text]]
    [webchange.interpreter.renderer.state.editor :as editor]
    [webchange.interpreter.renderer.scene.components.dragging :refer [enable-drag!]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.logger.index :as logger]
    [webchange.state.state-flipbook :as state-flipbook]
    [webchange.subs :as subs]))

(def frame-width 5)
(def frame-padding 10)
(def frame-default-color 0x59acff)
(def frame-selected-color 0xFFA500)

(defn- apply-img-origin
  [position props]
  (let [{:keys [origin width height scale]} props
        {scale-x :x scale-y :y} scale
        [h v] (clojure.string/split (:type origin) #"-")]
    (-> position
        (update :x + (-> (case h
                           "center" (/ width 2)
                           "right" width
                           0)
                         (* scale-x)))
        (update :y + (-> (case v
                           "center" (/ height 2)
                           "bottom" height
                           0)
                         (* scale-y))))))

(defn- apply-text-align
  [position {:keys [align width]} container]
  (let [local-bounds (.getLocalBounds container)
        x-align (aget container "x-align")]
    (-> position
        (update :x - (case (or x-align align)
                       "center" (/ width 2)
                       "right" (- width
                                  (.-width local-bounds)
                                  (.-x local-bounds))
                       0)))))

(defn round-position
  [{:keys [x y]}]
  (let [round #(-> % Math/round int)]
    {:x (round x)
     :y (round y)}))

(defn- fix-position
  [position props container]
  (let [{:keys [type]} props]
    (cond-> position
            (= type "image") (apply-img-origin props)
            (= type "text") (apply-text-align props container)
            :always (round-position))))

(re-frame/reg-event-fx
  ::change-position
  (fn [{:keys [db]} [_ position container props]]
    (let [name (editor/selected-object db)
          current-scene (subs/current-scene db)
          init-state (subs/scene-object db current-scene name)
          state (->> (fix-position position props container)
                     (merge init-state))
          dx (Math/abs (- (:x state) (:x init-state)))
          dy (Math/abs (- (:y state) (:y init-state)))
          delta 1]
      (when (or (> dx delta) (> dy delta))
        {:dispatch-n (list [::edit-scene/update-object {:scene-id current-scene
                                                        :target   name
                                                        :state    state}]
                           [::edit-scene/update-current-scene-object {:target name
                                                                      :state  state}]
                           [::edit-scene/save-current-scene current-scene]
                           [::state-flipbook/generate-stages-screenshots {:only-current-stage? true}])}))))

(defn- handle-frame-click
  [component]
  (re-frame/dispatch [::editor/select-object (:object-name component)]))

(defn- handle-drag
  [container props]
  (re-frame/dispatch [::change-position (utils/get-position container) container props]))

(defn- wrap
  [name sprite]
  {:name     name
   :hide     (fn [] (aset sprite "visible" false))
   :show     (fn [] (aset sprite "visible" true))
   :select   (fn []
               (aset sprite "visible" true)
               (aset sprite "tint" frame-selected-color))
   :deselect (fn []
               (aset sprite "visible" false))})

(defn- get-object-local-bounds
  [object]
  (let [bounds (.getLocalBounds object)]
    {:x      (.-x bounds)
     :y      (.-y bounds)
     :width  (.-width bounds)
     :height (.-height bounds)}))

(defn- get-frame-position
  [object]
  (let [local-bounds (get-object-local-bounds object)
        pivot (utils/get-pivot object)
        scale (utils/get-scale object {:abs? true})
        pos (utils/get-position object)]
    (-> local-bounds
        (update :width * (:x scale))
        (update :height * (:y scale))
        (update :x + (:x pos))
        (update :y + (:y pos))
        (update :x - (* (:x pivot) (:x scale)))
        (update :y - (* (:y pivot) (:y scale))))))

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
  [object]
  (let [{:keys [x y width height]} (get-frame-position object)]
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
  [sprite object]
  (->> (get-sprite-dimensions object)
       (set-sprite-dimensions! sprite)))

(defn- get-mask-dimensions
  [object]
  (let [{:keys [x y]} (get-frame-position object)]
    {:x (- x frame-padding)
     :y (- y frame-padding)}))

(defn- set-mask-dimensions!
  [mask position]
  (utils/set-position mask position))

(defn- update-mask-dimensions!
  [mask object]
  (->> (get-mask-dimensions object)
       (set-mask-dimensions! mask)))

(defn- update-editor-frame
  [object sprite mask component-container]
  (let [{:keys [x y width height]} (get-frame-position object)]
    (update-sprite-dimensions! sprite object)

    (doto mask
      (draw-border width height frame-padding)
      (update-mask-dimensions! object))

    (draw-border mask width height frame-padding)
    (aset component-container "hitArea" (Rectangle. x y width height))))

(defn- update-text-frame-position
  [state text-object frame-container]
  (let [{:keys [align x width]} (:props @state)
        {:keys [anchor-x position-x]}
        (case align
          "left" {:anchor-x   0
                  :position-x x}
          "center" {:anchor-x   0.5
                    :position-x (+ x (/ width 2))}
          "right" {:anchor-x   1
                   :position-x (+ x width)})]
    (aset frame-container "x-align" align)
    (aset (.-anchor text-object) "x" anchor-x)
    (utils/set-position frame-container {:x position-x})))

(defn- create-frame
  [component-container object-props object]
  (let [{:keys [x y width height]} (get-frame-position object)
        sprite (doto (Sprite. WHITE)
                 (aset "visible" false)
                 (update-sprite-dimensions! object))

        mask (doto (Graphics.)
               (update-mask-dimensions! object)
               (draw-border width height frame-padding))]

    (when (instance? Container object)
      (utils/set-handler object "childAdded" #(update-editor-frame object sprite mask component-container))
      (utils/set-handler object "visibilityChanged" (fn [visible?] (utils/set-visibility component-container visible?))))

    (when (instance? Text object)
      (utils/set-handler object "textChanged" #(update-editor-frame object sprite mask component-container))
      (utils/set-handler object "fontSizeChanged" #(update-editor-frame object sprite mask component-container))
      (utils/set-handler object "fontFamilyChanged" #(update-editor-frame object sprite mask component-container))
      (utils/set-handler object "textAlignChanged" #(do (update-text-frame-position % object component-container)
                                                        (update-editor-frame object sprite mask component-container))))

    (utils/set-handler object "scaleChanged" #(update-editor-frame object sprite mask component-container))
    (utils/set-handler object "srcChanged" #(update-editor-frame object sprite mask component-container))

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

(defn- drag-options
  [{{:keys [restrict-x restrict-y]} :editable?}]
  (when (or restrict-x restrict-y)
    {:restrict-x restrict-x
     :restrict-y restrict-y}))

(defn- filter-props
  [props]
  (->> props
       (filter (fn [[_ value]]
                 (or (number? value)
                     (string? value)
                     (map? value))))
       (into {})))

(defn- create-editor-container
  [props]
  (let [container (Container.)]
    (when (selectable? props) (utils/set-handler container "click" #(handle-frame-click props)))
    (when (draggable? props)
      (enable-drag! container {:on-drag-start        #(handle-frame-click props)
                               :on-drag-end          #(handle-drag container (filter-props props))
                               :on-drag-move-options (drag-options props)}))
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
