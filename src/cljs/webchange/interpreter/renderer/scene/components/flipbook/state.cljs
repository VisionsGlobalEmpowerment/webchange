(ns webchange.interpreter.renderer.scene.components.flipbook.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.pixi :refer [Graphics]]))

(defn- interpolate
  [{:keys [from to duration on-progress on-end]
    :or   {duration 10}}]
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
  :flip-forward
  (fn [{:keys [flipped-page flipped-page-back flipped-page-back-neighbor on-end]}]
    (let [flipped-page-position (get-position flipped-page)

          flipped-page-size (get-size flipped-page)
          flipped-page-back-size (get-size flipped-page-back)

          flipped-page-mask-params flipped-page-size
          flipped-page-mask (create-mask flipped-page-mask-params)

          flipped-page-back-mask-params {:width  0
                                         :height (:height flipped-page-back-size)}
          flipped-page-back-mask (create-mask flipped-page-back-mask-params)
          flipped-page-back-initial-position (+ (:x flipped-page-position) (:width flipped-page-size))
          flipped-page-back-destination-position (- (:x flipped-page-position) (:width flipped-page-size))]

      (let [container (.-parent flipped-page)]
        (set! (.-sortableChildren container) true)
        (set! (.-zIndex flipped-page) 2)
        (set! (.-zIndex flipped-page-back) 3)
        (set! (.-zIndex flipped-page-back-neighbor) 1))

      ; Set initial position
      (apply-mask flipped-page flipped-page-mask)

      (set-position flipped-page-back (assoc flipped-page-position :x flipped-page-back-initial-position))
      (apply-mask flipped-page-back flipped-page-back-mask)
      (set-visibility flipped-page-back true)

      (when (some? flipped-page-back-neighbor)
        (set-visibility flipped-page-back-neighbor true))

      ; Animate
      (interpolate {:from        {:flipped-page-width         (:width flipped-page-mask-params)
                                  :flipped-page-back-position flipped-page-back-initial-position
                                  :flipped-page-back-width    0}
                    :to          {:flipped-page-width         0
                                  :flipped-page-back-position flipped-page-back-destination-position
                                  :flipped-page-back-width    (:width flipped-page-back-size)}
                    :on-progress (fn [{:keys [flipped-page-width flipped-page-back-position flipped-page-back-width]}]
                                   (update-mask flipped-page-mask (assoc flipped-page-back-mask-params :width flipped-page-width))
                                   (update-mask flipped-page-back-mask (assoc flipped-page-back-mask-params :width flipped-page-back-width))
                                   (set-position-x flipped-page-back flipped-page-back-position))
                    :on-end      on-end}))))

(re-frame/reg-fx
  :flip-backward
  (fn [{:keys [flipped-page flipped-page-back flipped-page-back-neighbor on-end]}]
    (let [page-size (get-size flipped-page)
          left-page-position (-> flipped-page get-position)
          right-page-position (update left-page-position :x + (:width page-size))

          flipped-page-front-mask (create-mask page-size)
          flipped-page-back-mask (create-mask (-> page-size
                                                  (assoc :x 200)
                                                  (assoc :width 100)))]

      (let [container (.-parent flipped-page)]
        (set! (.-sortableChildren container) true)
        (set! (.-zIndex flipped-page) 2)
        (set! (.-zIndex flipped-page-back) 3)
        (set! (.-zIndex flipped-page-back-neighbor) 1))

      ;; Flipped front side
      (apply-mask flipped-page flipped-page-front-mask)

      ;; Flipped back side
      (set-position flipped-page-back left-page-position)
      (apply-mask flipped-page-back flipped-page-back-mask)
      (set-visibility flipped-page-back true)

      ;; Flipped back side neighbor
      (when (some? flipped-page-back-neighbor)
        (set-visibility flipped-page-back-neighbor true))

      ; Animate
      (interpolate {:from        {;; Flipped front side
                                  :front-page-mask-x     0
                                  :front-page-mask-width (:width page-size)
                                  ;; Flipped back side
                                  :back-page-x           (:x left-page-position)
                                  :back-page-mask-x      (:width page-size)
                                  :back-page-mask-width  0}
                    :to          {;; Flipped front side
                                  :front-page-mask-x     (:width page-size)
                                  :front-page-mask-width 0
                                  ;; Flipped back side
                                  :back-page-x           (:x right-page-position)
                                  :back-page-mask-x      0
                                  :back-page-mask-width  (:width page-size)}
                    :on-progress (fn [{:keys [front-page-mask-x
                                              front-page-mask-width

                                              back-page-x
                                              back-page-mask-x
                                              back-page-mask-width]}]
                                   ;; Flipped front side
                                   (update-mask flipped-page-front-mask (-> page-size
                                                                            (assoc :x front-page-mask-x)
                                                                            (assoc :width front-page-mask-width)))

                                   ;; Flipped back side
                                   (set-position-x flipped-page-back (- back-page-x back-page-mask-x))
                                   (update-mask flipped-page-back-mask (-> page-size
                                                                           (assoc :x back-page-mask-x)
                                                                           (assoc :width back-page-mask-width))))
                    :on-end      on-end}))))
