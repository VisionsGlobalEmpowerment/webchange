(ns webchange.interpreter.renderer.scene.components.animation.utils.idle
  (:require
    [webchange.interpreter.renderer.scene.components.animation.utils :as utils]
    [webchange.interpreter.renderer.scene.components.animation.utils.state :as state-utils]
    [webchange.utils.scene-action-data :refer [animation-tracks]]))

(defn support-idle?
  [animation-resource]
  (-> (.-name animation-resource)
      (= "child")))

(defn- freq->rand
  [frequency-interval]
  (-> (- (last frequency-interval) (first frequency-interval))
      (* (rand))
      (+ (first frequency-interval))))

(defn- set-timeout
  [callback delay state key]
  (let [timeout-id (js/setTimeout callback delay)]
    (swap! state assoc-in [:state :timeouts key] timeout-id)))

(defn- reset-timeouts
  [state]
  (let [timeouts (-> @state (get-in [:state :timeouts]))]
    (doseq [[key id] timeouts]
      (swap! state update-in [:state :timeouts] dissoc key)
      (js/clearTimeout id))))

(defn- apply-idle
  [spine-object {:keys [with-item?]}]
  (when (and (utils/has-animation? spine-object "idle")
             (utils/has-animation? spine-object "idle_item"))
    (let [speed-interval [0.7 1.1]
          track (utils/set-animation spine-object (if with-item? "idle_item" "idle") {:track-index (:idle animation-tracks)
                                                                                      :loop?       true})]
      (set! (.-timeScale track) (freq->rand speed-interval))))
  spine-object)

(defn- make-blink
  [track frequency state]
  (set! (.-trackTime track) 0)
  (set-timeout #(make-blink track frequency state) (freq->rand frequency) state :make-blink))

(defn- apply-blink
  [spine-object state]
  (when (utils/has-animation? spine-object "blink")
    (let [frequency (->> [15 20]                            ;; times per minute
                         (map #(/ 60000 %))
                         (sort))]
      (set-timeout #(-> (utils/set-animation spine-object "blink" {:track-index (:eyes animation-tracks)
                                                                   :loop?       false})
                        (make-blink frequency state))
                   (freq->rand frequency)
                   state :init-blink)))
  spine-object)

(defn- make-boredom
  [spine-object animations frequency state]
  (let [animation (->> (count animations)
                       (rand-int)
                       (nth animations))]
    (utils/add-animation spine-object animation {:track-index (:hands animation-tracks)
                                                 :loop?       false
                                                 :force-set?  false})
    (set-timeout #(make-boredom spine-object animations frequency state) (freq->rand frequency) state :make-boredom)))

(defn- apply-boredom
  [spine-object state]
  (let [animations (->> ["track-boredom"
                         "track-dance"
                         "track-hi"
                         "track-shrug"]
                        (filter #(utils/has-animation? spine-object %)))
        frequency (->> [1 2]                                ;; times per minute
                       (map #(/ 60000 %))
                       (sort))]
    (when-not (empty? animations)
      (set-timeout #(make-boredom spine-object animations frequency state)
                   (freq->rand frequency)
                   state :init-boredom)))
  spine-object)

(defn enable-idle-animation
  ([state]
   (enable-idle-animation state nil))
  ([state char-state]
   (let [spine-object (:animation @state)]
     (-> spine-object
         (apply-idle {:with-item? (if (some? char-state) (state-utils/has-item-in-hands? char-state) false)})
         (apply-blink state)
         (apply-boredom state)))))

(defn disable-idle-animation
  [state]
  (let [spine-object (:animation @state)]
    (utils/set-empty-animation spine-object {:track-index  (:idle animation-tracks)
                                             :mix-duration 0.3})
    (reset-timeouts state)))

(defn reset-idle-animation
  [state char-state]
  (disable-idle-animation state)
  (enable-idle-animation state char-state))

(defn start-idle-animation?
  [props resource]
  (and (:idle-animation-enabled? props)
       (support-idle? resource)))
