(ns webchange.interpreter.renderer.scene.components.flipbook.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.pixi :refer [Graphics]]))

(defn- interpolate
  [{:keys [from to duration on-progress on-end]}]
  (let [container (clj->js from)
        tween-params (-> to
                         (assoc :onUpdate (fn [] (on-progress (-> container
                                                                  (js->clj :keywordize-keys true)
                                                                  (select-keys (keys from))))))
                         (assoc :onComplete (fn [] (on-end)))
                         (clj->js))]
    (TweenMax.to container duration tween-params)))

(defn- create-mask
  [{:keys [x y width height]
    :or   {x      0
           y      0
           width  0
           height 0}}]
  (doto (Graphics.)
    (.beginFill 0x000000)
    (.drawRect x y width height)
    (.endFill 0x000000)))

(defn- apply-mask
  [container mask]
  (.addChild container mask)
  (aset container "mask" mask))

(defn- update-mask
  [mask {:keys [x y width height]
         :or   {x      0
                y      0
                width  0
                height 0}}]
  (.clear mask)
  (.beginFill mask 0x000000)
  (.drawRect mask x y width height))

(defn- get-position
  [container]
  {:x (.. container -position -x)
   :y (.. container -position -y)})

(defn- get-size
  [container]
  (let [bounds (.getLocalBounds container)]
    {:width  (.-width bounds)
     :height (.-height bounds)}))

(defn- set-position
  [display-object position]
  (let [{:keys [x y]} position]
    (-> (.-position display-object)
        (.set x y))))

(defn- set-position-x
  [display-object position]
  (set! (.. display-object -position -x) position))

(defn- set-visibility
  [display-object value]
  (set! (.-visible display-object) value))

(re-frame/reg-fx
  :flip-toward
  (fn [{:keys [target-1 target-2 target-3 on-end]}]
    (let [target-1-position (get-position target-1)

          target-1-size (get-size target-1)
          target-2-size (get-size target-2)

          target-1-mask-params target-1-size
          target-1-mask (create-mask target-1-mask-params)

          target-2-mask-params {:width  0
                                :height (:height target-2-size)}
          target-2-mask (create-mask target-2-mask-params)
          target-2-initial-position (+ (:x target-1-position) (:width target-1-size))
          target-2-destination-position (- (:x target-1-position) (:width target-1-size))]

      ; Set initial position
      (apply-mask target-1 target-1-mask)

      (set-position target-2 (assoc target-1-position :x target-2-initial-position))
      (apply-mask target-2 target-2-mask)
      (set-visibility target-2 true)

      (when (some? target-3)
        (set-visibility target-3 true))

      ; Animate
      (interpolate {:duration    1
                    :from        {:target-1-width    (:width target-1-mask-params)
                                  :target-2-position target-2-initial-position
                                  :target-2-width    0}
                    :to          {:target-1-width    0
                                  :target-2-position target-2-destination-position
                                  :target-2-width    (:width target-2-size)}
                    :on-progress (fn [{:keys [target-1-width target-2-position target-2-width]}]
                                   (update-mask target-1-mask (assoc target-2-mask-params :width target-1-width))
                                   (update-mask target-2-mask (assoc target-2-mask-params :width target-2-width))
                                   (set-position-x target-2 target-2-position))
                    :on-end      on-end}))))
