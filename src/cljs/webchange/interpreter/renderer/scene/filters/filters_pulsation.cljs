(ns webchange.interpreter.renderer.scene.filters.filters-pulsation)

(def intervals {:play 2 :pause 6})
(def interval-duration 800)

(def scale-diff 0.1)
(def rotation-angle 15)

(defn- init-state!
  [state frame interval-duration]
  (when (nil? @state)
    (reset! state {:interval-number     0
                   :next-interval-frame (+ frame interval-duration)})))

(defn- milestone-frame?
  [frame state]
  (>= frame (:next-interval-frame @state)))

(defn- play-interval?
  [intervals state]
  (< (:interval-number @state) (:play intervals)))

(defn- next-interval!
  [state frame intervals interval-duration]
  (let [current-interval (:interval-number @state)
        next-interval (if (< current-interval (dec (+ (:play intervals)
                                                      (:pause intervals))))
                        (inc current-interval)
                        0)
        next-frame (+ frame interval-duration)]
    (reset! state {:interval-number     next-interval
                   :next-interval-frame next-frame})))

(defn- get-time
  "Get time interval [0..1] from current frame number."
  [current-frame duration state]
  (let [milestone-frame (:next-interval-frame @state)]
    (/ (- duration (- milestone-frame current-frame)) duration)))

(defn- apply-intervals!
  "Count time cycles with 'interval-duration' length.
   Return current time value if it's a 'play' interval. Return 0 if it's a 'pause' interval."
  [current-value frame state intervals interval-duration]
  (let [initial-value? (milestone-frame? frame state)]
    (when initial-value?
      (next-interval! state frame intervals interval-duration))
    (if (and (play-interval? intervals state)
             (not initial-value?))
      current-value
      0)))

(defn- get-time-cycled
  "Transform time interval [0..1] to [0..1..-1..0]."
  [time]
  (Math/sin (* time 2 Math/PI)))

(defn- apply-transformation
  [object {:keys [scale rotation]}]
  (when-not (nil? scale)
      (-> (.-scale object) (.set scale)))
  (when-not (nil? rotation)
      (.setRotation object rotation)))

(defn animation-eager
  [object frame state]
  (init-state! state (:time frame) interval-duration)
  (let [time (-> (get-time (:time frame) interval-duration state)
                 (apply-intervals! (:time frame) state intervals interval-duration)
                 (get-time-cycled))
        scale (+ 1 (* (+ 1 time) scale-diff))
        rotation (* rotation-angle time)]
    (apply-transformation object {;:rotation rotation
                                  :scale scale})))
