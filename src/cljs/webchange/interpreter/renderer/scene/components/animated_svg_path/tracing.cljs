(ns webchange.interpreter.renderer.scene.components.animated-svg-path.tracing
  (:require
    [webchange.interpreter.pixi :refer [Sprite Texture]]
    [webchange.interpreter.renderer.scene.components.animated-svg-path.path :refer [precision]]
    [webchange.interpreter.renderer.scene.components.utils :as utils]))

(def touch-distance 5)

(defn- scale-distance
  [distance {:keys [x]}]
  (* distance x))

(defn- scale-point
  [{px :x py :y} {sx :x sy :y}]
  {:x (* px sx)
   :y (* py sy)})

(defn- in-area
  [{p1-x :x p1-y :y} {p2-x :x p2-y :y} distance]
  (> distance (js/Math.sqrt (+
                              (* (- p1-x p2-x) (- p1-x p2-x))
                              (* (- p1-y p2-y) (- p1-y p2-y))))))

(defn- complete-path
  [points point-idx pointer scale]
  (loop [point-idx point-idx
         [cur-point & tail] (drop point-idx points)
         updated? false]
    (let [touch? (in-area pointer (scale-point cur-point scale) (scale-distance touch-distance scale))]
      (cond
        (and touch? tail) (recur (inc point-idx) tail true)
        touch? {:updated? true :path-finished true}
        :else {:updated? updated? :next-point-idx point-idx}))))

(defn- draw
  [state {:keys [path-idx point-idx]}]
  (let [{:keys [ctx texture paths width height]} @state
        idx (atom 0)]
    (.clearRect ctx 0 0 width height)
    (.setLineDash ctx #js [])

    (while (< @idx path-idx)
      (let [{:keys [path]} (get paths @idx)]
        (.stroke ctx (js/Path2D. path))
        (swap! idx inc)))

    (when-let [{:keys [path length]} (get paths path-idx)]
      (when (> point-idx 0)
        (.setLineDash ctx #js [(* point-idx precision) length])
        (.stroke ctx (js/Path2D. path))))

    (.update texture)))

(defn- drag
  [state scale pointer]
  (let [{:keys [paths] {:keys [path-idx point-idx] :or {path-idx 0 point-idx 0}} :next-point} @state
        points (some-> paths (nth path-idx nil) :points)
        {:keys [updated? path-finished next-point-idx]} (complete-path points point-idx pointer scale)
        next-point (if path-finished
                     {:path-idx  (inc path-idx)
                      :point-idx 0}
                     {:path-idx  path-idx
                      :point-idx next-point-idx})]
    (when updated?
      (swap! state assoc :next-point next-point)
      (draw state next-point))))

(defn- next-point-pos
  [state]
  (let [{:keys [paths] {:keys [path-idx point-idx] :or {path-idx 0 point-idx 0}} :next-point} @state
        points (some-> paths (nth path-idx nil) :points)]
    (-> points (nth point-idx))))

(defn- on-start
  [state scale]
  (fn [event]
    (this-as this
      (let [point (-> state (next-point-pos) (scale-point scale))
            pos (-> event .-data (.getLocalPosition (.-parent this)))
            offset {:x (- (:x point) (.-x pos))
                    :y (- (:y point) (.-y pos))}]
        (when (in-area {:x (.-x pos) :y (.-y pos)} point (scale-distance touch-distance scale))
          (set! (.-drag-offset this) offset)
          (set! (.-data this) (.-data event))
          (set! (.-drawing this) true))))))

(defn- on-end
  []
  (this-as this
    (set! (.-data this) nil)
    (set! (.-drawing this) false)))

(defn- on-move
  [state scale]
  (fn []
    (this-as this
      (when (.-drawing this)
        (let [{offset-x :x offset-y :y} (-> this .-drag-offset)
              pos (-> this .-data (.getLocalPosition (.-parent this)))
              pointer {:x (+ offset-x (.-x pos)) :y (+ offset-y (.-y pos))}]
          (drag state scale pointer))))))

(defn create-trigger
  [state {:keys [width height scale]}]
  (doto (Sprite. (.-EMPTY Texture))
    (utils/set-size {:width (* 2 width) :height (* 2 height)})
    (set! -interactive true)
    (.on "pointerdown" (on-start state scale))
    (.on "pointerup" on-end)
    (.on "pointerupoutside" on-end)
    (.on "pointermove" (on-move state scale))))
