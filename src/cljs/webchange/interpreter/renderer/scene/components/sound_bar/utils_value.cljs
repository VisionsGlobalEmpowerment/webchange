(ns webchange.interpreter.renderer.scene.components.sound-bar.utils-value)

(def bars-number 16)
(def max-value 16)
(def fading-speed 0.08)

(defn- get-with-chance
  [chance]
  (if (-> (rand) (< chance)) true false))

(defn- set-value!
  [state]
  (->> (:raw-value @state)
       (map Math/ceil)
       (swap! state assoc :value)))

(defn update-value!
  [state]
  (->> (:raw-value @state)
       (map (fn [bar-value]
              (let [mid-value (/ max-value 2)
                    chance-to-increase (if (> bar-value mid-value)
                                         0
                                         (->> (/ bar-value mid-value)
                                              (- 1)
                                              (* 0.5)))]
                (if (get-with-chance chance-to-increase)
                  (+ bar-value (rand 5))
                  (- bar-value fading-speed)))))
       (swap! state assoc :raw-value))
  (set-value! state))

(defn init-value!
  [state]
  (->> (repeat (/ max-value 2))
       (take bars-number)
       (swap! state assoc :raw-value))
  (set-value! state))
