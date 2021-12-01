(ns webchange.interpreter.renderer.scene.filters.utils-pulsation)

(def default-params {:min-value 0
                     :max-value 1
                     :duration  800})

(defonce states (atom {}))

(defn- get-state!
  [animation-key time-diff]
  (let [state (get @states animation-key)]
    (if (nil? state)
      (let [new-state (atom {:current-frame 0})]
        (swap! states assoc animation-key new-state)
        new-state)
      (do (swap! state update :current-frame + time-diff)
          state))))

(defn- remove-state!
  [animation-key]
  (swap! states dissoc animation-key))

(defn- get-value
  "Get time interval [0..1] from current frame number."
  [state {:keys [duration]}]
  (let [{:keys [current-frame]} @state]
    (when (>= current-frame duration)
      (swap! state assoc :current-frame 0))
    (-> (/ current-frame duration)
        (Math/min 1)
        (Math/max 0))))

(defn- get-sin
  [value]
  (-> value
      (* Math/PI 2)
      (Math/sin)
      (+ 1)
      (/ 2)))

(defn- scale
  [value {:keys [min-value max-value]}]
  (-> value
      (* (- max-value min-value))
      (+ min-value)))

(defn pulsation
  ([animation-key params]
   (pulsation animation-key params 1))
  ([animation-key params time-diff]
   (let [custom-params (merge default-params params)
         state (get-state! animation-key time-diff)]
     (-> (get-value state custom-params)
         (get-sin)
         (scale custom-params)))))

(defn reset-pulsation
  [animation-key]
  (remove-state! animation-key))
