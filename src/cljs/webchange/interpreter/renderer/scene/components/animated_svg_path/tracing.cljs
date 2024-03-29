(ns webchange.interpreter.renderer.scene.components.animated-svg-path.tracing
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.pixi :refer [Sprite Texture Graphics]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.path :refer [precision]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.interpreter.renderer.scene.components.debug :as debug]
    [webchange.logger.index :as logger]))

(def touch-distance 25)

(defn- scale-distance
  [distance {:keys [x]}]
  (* distance x))

(defn- scale-point
  [{px :x py :y} {sx :x sy :y}]
  {:x (* px sx)
   :y (* py sy)})

(defn- translate-point
  [{px :x py :y} {tx :x ty :y}]
  {:x (+ px tx)
   :y (+ py ty)})

(defn- in-area
  [{p1-x :x p1-y :y} {p2-x :x p2-y :y} distance]
  (> distance (js/Math.sqrt (+
                              (* (- p1-x p2-x) (- p1-x p2-x))
                              (* (- p1-y p2-y) (- p1-y p2-y))))))

(defn- complete-path
  [points point-idx pointer scale offset]
  (loop [point-idx point-idx
         [cur-point & tail] (drop point-idx points)
         updated? false]
    (let [point (-> cur-point (translate-point offset) (scale-point scale))
          touch? (in-area pointer point (scale-distance touch-distance scale))]
      (cond
        (and touch? tail) (recur (inc point-idx) tail true)
        touch? {:updated? true :path-finished true}
        :else {:updated? updated? :next-point-idx point-idx}))))

(def hint (atom nil))

(defn- get-direction [points index]
  (let [index (if (= index (dec (count points)))
                (dec index)
                index)
        {x1 :x y1 :y} (nth points index)
        {x2 :x y2 :y} (nth points (inc index))
        vx (- x2 x1)
        vy (- y2 y1)]
    (* (Math/atan2 vy vx) (/ 180.0 Math/PI))))

(defn draw
  [state {:keys [path-idx point-idx]} dimensions]
  (let [{:keys [ctx texture paths width height]} @state
        idx (atom 0)]
    (.clearRect ctx 0 0 width height)
    (.setLineDash ctx #js [])

    (while (< @idx path-idx)
      (let [{:keys [path]} (get paths @idx)]
        (.stroke ctx (js/Path2D. path))
        (swap! idx inc)))

    (when-let [{:keys [path length points]} (get paths path-idx)]
      (.save ctx)
      (.setLineDash ctx #js [7 7])
      (set! ctx -strokeStyle "#00c3ff")
      (set! ctx -lineWidth 4)
      (.stroke ctx (js/Path2D. path))
      (.restore ctx)

      (when (> point-idx 0)
        (.setLineDash ctx #js [(* point-idx precision) length])
        (.stroke ctx (js/Path2D. path)))

      (if-let [arrow (:arrow @hint)]
        (let [dot (:dot @hint)
              {:keys [x y]} (nth points point-idx)
              dx (:x dimensions)
              dy (:y dimensions)
              ds (:scale dimensions)]
          (if (< (count points) 7)
            (do
              ((:set-position arrow) {:x -100 :y 100})
              ((:set-position dot) {:x (+ dx (* ds x)) :y (+ dy (* ds y))}))
            (do
              ((:set-position dot) {:x -100 :y 100})
              ((:set-rotation arrow) (get-direction points point-idx))
              ((:set-position arrow) {:x (+ dx (* ds x)) :y (+ dy (* ds y))}))))))
    (.update texture)))

(defn- drag
  [state {:keys [x y scale offset on-finish]} pointer]
  (let [{:keys [paths] {:keys [path-idx point-idx] :or {path-idx 0 point-idx 0}} :next-point} @state
        points (some-> paths (nth path-idx nil) :points)
        {:keys [updated? path-finished next-point-idx]} (complete-path points point-idx pointer scale offset)
        next-point (if path-finished
                     {:path-idx  (inc path-idx)
                      :point-idx 0}
                     {:path-idx  path-idx
                      :point-idx next-point-idx})
        all-paths-finished (and path-finished (= (inc path-idx) (count paths)))
        dimensions {:x x :y y :scale (:x scale)}]
    (when updated?
      (swap! state assoc :next-point next-point)
      (draw state next-point dimensions))

    (when (and all-paths-finished on-finish)
      (on-finish))))

(defn- next-point-pos
  [state]
  (let [{:keys [paths] {:keys [path-idx point-idx] :or {path-idx 0 point-idx 0}} :next-point} @state
        points (some-> paths (nth path-idx nil) :points)]
    (-> points (nth point-idx))))

(defn- on-start
  [state {:keys [scale offset] :as props}]
  (fn [event]
    (this-as this
      (let [point (-> state (next-point-pos) (translate-point offset) (scale-point scale))
            pos (-> event .-data (.getLocalPosition (.-parent this)))
            drag-offset {:x (- (:x point) (.-x pos))
                         :y (- (:y point) (.-y pos))}]
        (debug/mark :finger (.-x pos) (.-y pos) (scale-distance touch-distance scale) 0xff0000 (.-parent this))
        (debug/mark :next (:x point) (:y point) 4 0x00ff00 (.-parent this))

        (logger/trace-folded "animated-svg-path tracing on-start" this)

        (when (and (:active @state) (in-area {:x (.-x pos) :y (.-y pos)} point (scale-distance touch-distance scale)))
          (set! (.-drag-offset this) drag-offset)
          (set! (.-data this) (.-data event))
          (set! (.-drawing this) true)

          (let [{offset-x :x offset-y :y} (-> this .-drag-offset)
                pos (-> this .-data (.getLocalPosition (.-parent this)))
                pointer {:x (+ offset-x (.-x pos)) :y (+ offset-y (.-y pos))}]
            (drag state props pointer)))))))

(defn- on-end
  []
  (this-as this
    (set! (.-data this) nil)
    (set! (.-drawing this) false)))

(defn- on-move
  [state props]
  (fn []
    (this-as this
      (when (and (:enable? @state)
                 (.-drawing this))
        (let [{offset-x :x offset-y :y} (-> this .-drag-offset)
              pos (-> this .-data (.getLocalPosition (.-parent this)))
              pointer {:x (+ offset-x (.-x pos)) :y (+ offset-y (.-y pos))}]
          (drag state props pointer))))))

(defn create-trigger
  [state {:keys [x y width height scale] :as props}]
  (when (:active @state)
    (draw state {:path-idx 0 :point-idx 0} {:x x :y y :scale (:x scale)}))
  (doto (Sprite. (.-EMPTY Texture))
    (utils/set-size {:width (* width (:x scale)) :height (* height (:y scale))})
    (set! -interactive true)
    (.on "pointerdown" (on-start state props))
    (.on "pointerup" on-end)
    (.on "pointerupoutside" on-end)
    (.on "pointermove" (on-move state props))))
