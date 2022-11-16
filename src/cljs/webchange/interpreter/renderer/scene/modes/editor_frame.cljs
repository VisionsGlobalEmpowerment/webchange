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

(def resize-control-width 26)
(def resize-control-offset 13)

(def maximize-offset 250)

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

(def round #(-> % Math/round int))

(defn round-position
  [{:keys [x y]}]
  {:x (round x)
   :y (round y)})

(defn- fix-position
  [position props container]
  (let [{:keys [type]} props]
    (cond-> position
            (= type "image") (apply-img-origin props)
            (= type "text") (apply-text-align props container)
            :always (round-position))))

(defn- handle-frame-click
  [component params]
  (re-frame/dispatch [::editor/select-object (:object-name component) params]))

(defn- handle-drag
  [container props]
  (let [state (fix-position (utils/get-position container) props container)]
    (re-frame/dispatch [::editor/update-selected-object state])))

(defn- wrap
  [name sprite resize-controls]
  {:name     name
   :hide     (fn [] (aset sprite "visible" false)
               (aset resize-controls "visible" false))
   :show     (fn []
               (aset sprite "visible" true)
               (aset resize-controls "visible" true))
   :select   (fn []
               (aset sprite "visible" true)
               (aset resize-controls "visible" true)
               (aset sprite "tint" frame-selected-color))
   :deselect (fn []
               (aset sprite "visible" false)
               (aset resize-controls "visible" false))})

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
  [{:keys [x y width height]}]
  {:x      (- x frame-padding frame-width)
   :y      (- y frame-padding frame-width)
   :width  (+ width (* 2 (+ frame-padding frame-width)))
   :height (+ height (* 2 (+ frame-padding frame-width)))})

(defn- set-sprite-dimensions!
  [sprite {:keys [x y width height]}]
  (doto sprite
    (aset "width" (+ width (* 2 (+ frame-padding frame-width))))
    (aset "height" (+ height (* 2 (+ frame-padding frame-width))))
    (utils/set-position {:x (- x frame-padding frame-width)
                         :y (- y frame-padding frame-width)})))

(defn- update-sprite-dimensions!
  [sprite bounds]
  (->> (get-sprite-dimensions bounds)
       (set-sprite-dimensions! sprite)))

(defn- get-mask-dimensions
  [{:keys [x y]}]
  {:x (- x frame-padding)
   :y (- y frame-padding)})

(defn- set-mask-dimensions!
  [mask position]
  (utils/set-position mask position))

(defn- update-mask-dimensions!
  [mask bounds]
  (->> (get-mask-dimensions bounds)
       (set-mask-dimensions! mask)))

(defn- rectangle-with-offset
  [{:keys [x y width height]} offset]
  (Rectangle.
   (- x offset)
   (- y offset)
   (+ width (* 2 offset))
   (+ height (* 2 offset))))

(defn- update-editor-frame
  [{:keys [width height] :as bounds} sprite mask component-container]
  (update-sprite-dimensions! sprite bounds)

  (doto mask
    (draw-border width height frame-padding)
    (update-mask-dimensions! bounds)
    (draw-border width height frame-padding))

  (aset component-container "hitArea" (rectangle-with-offset bounds resize-control-offset)))

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

(defn- resize-control-element
  [x y cursor]
  (doto (Graphics.)
    (.beginFill frame-default-color)
    (.drawRect 0 0 resize-control-width resize-control-width)
    (.endFill)
    (aset "x" x)
    (aset "y" y)
    (aset "interactive" true)
    (aset "cursor" cursor)))

(defn- update-resize-control-positions!
  [{:keys [x y width height]} top-left top-right bottom-right bottom-left]
  (when top-left
    (doto top-left
      (aset "x" (- x resize-control-offset))
      (aset "y" (- y resize-control-offset))))

  (when top-right
    (doto top-right
      (aset "x" (+ x width (- resize-control-offset)))
      (aset "y" (- y resize-control-offset))))

  (when bottom-right
    (doto bottom-right
      (aset "x" (+ x width (- resize-control-offset)))
      (aset "y" (+ y height (- resize-control-offset)))))

  (when bottom-left
    (doto bottom-left
      (aset "x" (- x resize-control-offset))
      (aset "y" (+ y height (- resize-control-offset))))))

(defn- control-position
  [control]
  (-> (utils/get-position control)
      (update :x round)
      (update :y round)
      (update :x + resize-control-offset)
      (update :y + resize-control-offset)))

(defn- redraw-resize-elements
  [container wrapper sprite mask component-container]
  (.removeChildren container)
  (let [{:keys [get-bounds resize]} wrapper
        current-bounds (atom (get-bounds))
        {:keys [x y width height]} @current-bounds
        
        top-left (resize-control-element (- x resize-control-offset) (- y resize-control-offset) "nwse-resize")
        bottom-right (resize-control-element (+ x width (- resize-control-offset)) (+ y height (- resize-control-offset)) "nwse-resize")
        top-right (resize-control-element (+ x width (- resize-control-offset)) (- y resize-control-offset) "nesw-resize")
        bottom-left (resize-control-element (- x resize-control-offset) (+ y height (- resize-control-offset)) "nesw-resize")

        handle-drag-start #(let [bounds @current-bounds]
                             (aset component-container "hitArea" (rectangle-with-offset bounds maximize-offset)))
        handle-drag-end #(let [original-position (utils/get-position component-container)
                               inner-position (utils/get-position (:object wrapper))
                               inner-object (:object wrapper)]
                           (doto component-container
                             (aset "x" (+ (.-x inner-object) (.-x component-container)))
                             (aset "y" (+ (.-y inner-object) (.-y component-container))))
                           (set! (.-x inner-object) 0)
                           (set! (.-y inner-object) 0)
                           (reset! current-bounds (get-bounds))
                           (update-editor-frame @current-bounds sprite mask component-container)
                           (update-resize-control-positions! @current-bounds top-left top-right bottom-right bottom-left)
                           (re-frame/dispatch [::editor/update-selected-object (-> @current-bounds
                                                                                   (update :x + (:x inner-position) (:x original-position))
                                                                                   (update :y + (:y inner-position) (:y original-position))
                                                                                   (update :y - (:offset-y @current-bounds)))]))
        handle-drag-move (fn [get-new-bounds]
                           (fn []
                             (let [{:keys [offset-y]} @current-bounds
                                   new-bounds (get-new-bounds @current-bounds)
                                   original-position (utils/get-position component-container)]
                               (update-editor-frame new-bounds sprite mask component-container)
                               (update-resize-control-positions! new-bounds top-left top-right bottom-right bottom-left)
                               (resize (-> new-bounds
                                           (assoc :orig-x (:x original-position))
                                           (assoc :orig-y (:y original-position))
                                           (update :y - offset-y)))
                               (aset component-container "hitArea" (rectangle-with-offset new-bounds maximize-offset)))))]
    (enable-drag! top-left
                  {:on-drag-start handle-drag-start
                   :on-drag-move (handle-drag-move #(let [{:keys [x y width height]} @current-bounds
                                                          {c-x :x c-y :y} (control-position top-left)]
                                                      {:x c-x :y c-y
                                                       :width (- width (- c-x x)) :height (- height (- c-y y))}))
                   :on-drag-end handle-drag-end})
    (enable-drag! top-right
                  {:on-drag-start handle-drag-start
                   :on-drag-move (handle-drag-move #(let [{:keys [x y width height]} @current-bounds
                                                          {c-x :x c-y :y} (control-position top-right)]
                                                      {:x x :y c-y
                                                       :width (+ width (- c-x (+ width x))) :height (- height (- c-y y))}))
                   :on-drag-end handle-drag-end})
    (enable-drag! bottom-right
                  {:on-drag-start handle-drag-start
                   :on-drag-move (handle-drag-move #(let [{:keys [x y width height]} @current-bounds
                                                          {c-x :x c-y :y} (control-position bottom-right)]
                                                      {:x x :y y
                                                       :width (+ width (- c-x (+ width x))) :height (+ height (- c-y (+ height y)))}))
                   :on-drag-end handle-drag-end})
    (enable-drag! bottom-left
                  {:on-drag-start handle-drag-start
                   :on-drag-move (handle-drag-move #(let [{:keys [x y width height]} @current-bounds
                                                          {c-x :x c-y :y} (control-position bottom-left)]
                                                      {:x c-x :y y
                                                       :width (- width (- c-x x)) :height (+ height (- c-y (+ height y)))}))
                   :on-drag-end handle-drag-end})
    (.addChild container top-left)
    (.addChild container bottom-right)
    (.addChild container top-right)
    (.addChild container bottom-left)))

(defn- create-frame
  [component-container object-props wrapper]
  (let [{:keys [object get-bounds]} wrapper
        {:keys [x y width height] :as bounds} (get-bounds)
        sprite (doto (Sprite. WHITE)
                 (aset "visible" false)
                 (update-sprite-dimensions! bounds))

        mask (doto (Graphics.)
               (update-mask-dimensions! bounds)
               (draw-border width height frame-padding))
        resize (doto (Container.)
                 (aset "visible" false))]

    (when (instance? Container object)
      (utils/set-handler object "childAdded" #(update-editor-frame (get-bounds) sprite mask component-container))
      (utils/set-handler object "visibilityChanged" (fn [visible?] (utils/set-visibility component-container visible?))))

    (when (instance? Text object)
      (redraw-resize-elements resize wrapper sprite mask component-container)
      
      (utils/set-handler object "textChanged" #(update-editor-frame (get-bounds) sprite mask component-container))
      (utils/set-handler object "fontSizeChanged" #(update-editor-frame (get-bounds) sprite mask component-container))
      (utils/set-handler object "fontFamilyChanged" #(update-editor-frame (get-bounds) sprite mask component-container))
      (utils/set-handler object "textAlignChanged" #(do (update-text-frame-position % object component-container)
                                                        (update-editor-frame (get-bounds) sprite mask component-container)
                                                        (redraw-resize-elements resize wrapper sprite mask component-container))))

    (utils/set-handler object "scaleChanged" #(update-editor-frame (get-bounds) sprite mask component-container))
    (utils/set-handler object "srcChanged" #(update-editor-frame (get-bounds) sprite mask component-container))

    (when (some? (:visible object-props))
      (utils/set-visibility component-container (:visible object-props)))

    (aset sprite "mask" mask)
    (aset component-container "hitArea" (Rectangle.
                                         (- x resize-control-offset)
                                         (- y resize-control-offset)
                                         (+ width (* 2 resize-control-offset))
                                         (+ height (* 2 resize-control-offset))))
    (.addChild component-container mask)
    (.addChild component-container sprite)
    (.addChild component-container resize)
    (re-frame/dispatch [::editor/register-object (wrap (:object-name object-props) sprite resize)])))

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
  [props params]
  (let [container (Container.)]
    (when (selectable? props) (utils/set-handler container "click" #(handle-frame-click props params)))
    (when (draggable? props)
      (enable-drag! container {:on-drag-start        #(handle-frame-click props params)
                               :on-drag-end          #(handle-drag container (filter-props props))
                               :on-drag-move-options (drag-options props)}))
    container))

(defn- wrap-in-container
  [object props params]
  (let [container (create-editor-container props params)
        current-parent (.-parent object)
        index (.getChildIndex current-parent object)]
    (.addChildAt current-parent container index)
    (.addChild container object)

    (utils/set-position container (utils/get-position object))
    (utils/set-position object 0 0)

    container))

(defn add-editor-frame
  [wrapper {:keys [editable?] :as props} params]
  (when editable?
    (logger/trace-folded ["add editor frame for" (:object-name props)] props (:object wrapper))
    (let [container (wrap-in-container (:object wrapper) props params)]
      (create-frame container props wrapper))))
