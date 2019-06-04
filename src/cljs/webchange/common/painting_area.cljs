(ns webchange.common.painting-area
  (:require
    [react-konva :refer [Image]]))

(def tools {:pencil   {:lineCap    "butt"
                       :lineJoin   "miter"
                       :lineWidth  1
                       :shadowBlur 0
                       :globalCompositeOperation "source-over"}
            :felt-tip {:lineCap    "butt"
                       :lineJoin   "miter"
                       :lineWidth  8
                       :shadowBlur 1
                       :globalCompositeOperation "source-over"}
            :brush    {:lineCap    "round"
                       :lineJoin   "round"
                       :lineWidth  13
                       :shadowBlur 7
                       :globalCompositeOperation "source-over"}
            :eraser   {:lineCap    "round"
                       :lineJoin   "round"
                       :lineWidth  15
                       :shadowBlur 0
                       :globalCompositeOperation "destination-out"}})

(def default-props {:tool  "brush"
                    :color "#a5c613"})

(defonce state (atom {:canvas                nil
                      :canvas-context        nil
                      :on-change             #()
                      :image                 nil
                      :stage                 nil
                      :is-painting           false
                      :last-pointer-position nil}))

(defn get-tool-style
  [tool]
  (let [tool-key (keyword tool)]
    (if (contains? tools tool-key)
      (get tools tool-key)
      (throw (js/Error. (str "Tool '" tool "' is not supported for painting area component"))))))

(defn get-color-style
  [color]
  {:strokeStyle color
   :shadowColor color})

(defn apply-map-to-context
  [context params]
  (goog.object/forEach (clj->js params)
                       (fn [val key]
                         (aset context key val))))

(defn update-painting-style
  [context tool color]
  {:pre [(not (nil? context))]}
  (let [tool-style (if tool (get-tool-style tool) {})
        color-style (if color (get-color-style color) {})
        style (merge tool-style color-style)]
    (apply-map-to-context context style)))

(defn init-canvas
  [width height tool color]
  (let [canvas (.createElement js/document "canvas")
        canvas-context (.getContext canvas "2d")]
    (set! (.-width canvas) width)
    (set! (.-height canvas) height)
    (update-painting-style canvas-context
                           (or tool (:tool default-props))
                           (or color (:color default-props)))
    {:canvas         canvas
     :canvas-context canvas-context}))

(defn constructor
  [width height tool color on-change]
  (let [{:keys [canvas canvas-context]} (init-canvas width height tool color)]
    (swap! state assoc :canvas canvas)
    (swap! state assoc :canvas-context canvas-context)
    (if (->> on-change nil? not) (swap! state assoc :on-change on-change))))

(defn painting-area-render
  [{:keys [x y width height tool color on-change]}]
  (constructor width height tool color on-change)
  (let [canvas (:canvas @state)]
    [:> Image {:x      x
               :y      y
               :width  width
               :height height
               :image  canvas
               :ref    #(swap! state assoc :image %)}]))

(defn start-painting
  []
  (let [stage (:stage @state)
        pointer-position (.getPointerPosition stage)]
    (swap! state assoc :is-painting true)
    (swap! state assoc :last-pointer-position pointer-position)))

(defn finish-painting
  []
  (swap! state assoc :is-painting false))

(defn get-point-relative-coordinates
  [point image stage]
  {:x (-> (.-x point)
          (- (* (.x image) (.scaleX stage)))
          (/ (.scaleX stage)))
   :y (-> (.-y point)
          (- (* (.y image) (.scaleY stage)))
          (/ (.scaleY stage)))})

(defn get-draw-points
  [stage image last-pointer-position]
  (let [pointer-position (.getPointerPosition stage)]
    {:point1 (get-point-relative-coordinates last-pointer-position image stage)
     :point2 (get-point-relative-coordinates pointer-position image stage)
     :pointer-position pointer-position}))

(defn draw-line
  [canvas-context p1 p2]
  (.beginPath canvas-context)
  (.moveTo canvas-context (:x p1) (:y p1))
  (.lineTo canvas-context (:x p2) (:y p2))
  (.closePath canvas-context)
  (.stroke canvas-context))

(defn process-painting
  []
  (if (:is-painting @state)
    (let [stage (:stage @state)
          image (:image @state)
          last-pointer-position (:last-pointer-position @state)
          {:keys [point1 point2 pointer-position]} (get-draw-points stage image last-pointer-position)]
      (draw-line (:canvas-context @state) point1 point2)
      (swap! state assoc :last-pointer-position pointer-position)
      (.batchDraw (.getLayer image)))))

(defn component-did-mount
  []
  (let [image (:image @state)
        stage (.getStage image)]
    (swap! state assoc :stage stage)
    (.on image "mousedown touchstart" start-painting)
    (.addEventListener stage "mouseup touchend" finish-painting)
    (.addEventListener stage "mousemove touchmove" process-painting)))

(defn should-component-update
  [_ _ [_ next-props]]
  (let [color (:color next-props)
        tool (:tool next-props)
        canvas-context (:canvas-context @state)]
    (update-painting-style canvas-context tool color)
    false))

(defn component-will-unmount
  []
  (let [change-handler (:on-change @state)
        canvas (:canvas @state)]
    (change-handler (.toDataURL canvas))))

(def painting-area
  (with-meta painting-area-render
             {:should-component-update should-component-update
              :component-did-mount component-did-mount
              :component-will-unmount component-will-unmount}))

