(ns webchange.interpreter.renderer.scene.components.flipbook.state-flip-actions
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.pixi :refer [Graphics]]
    [webchange.interpreter.renderer.scene.components.utils :as components-utils]))

(defn- interpolate
  [{:keys [from to duration on-progress on-end]
    :or   {duration 1}}]
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

(defn- find-mask
  [container]
  (.find (.-children container)
         (fn [child]
           (.-isMask child))))

(defn- remove-mask
  [container]
  (.removeChild container (find-mask container))
  (aset container "mask" nil))

(defn- apply-mask
  [container mask]
  (remove-mask container)
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
  (.drawRect mask x y width height)
  (.endFill mask 0x000000))

(defn- get-container
  [db transition-id]
  (let [scene-id (:current-scene db)
        transition (get-in db [:transitions scene-id transition-id])]
    (when (some? transition)
      (:object @transition))))

(defn- spread-names->containers
  [db {:keys [left right]}]
  {:left  (get-container db left)
   :right (get-container db right)})

(re-frame/reg-event-fx
  ::execute-flip
  (fn [{:keys [db]} [_ {:keys [direction current-spread next-spread] :as params}]]
    (let [params (-> params
                     (merge {:current-spread (spread-names->containers db current-spread)
                             :next-spread    (spread-names->containers db next-spread)}))]
      (case direction
        "forward" {:flip-forward params}
        "backward" {:flip-backward params}))))

(defn- set-position
  [object position]
  (when (some? object)
    (components-utils/set-position object position)))

(defn- set-visibility
  [object visible?]
  (when (some? object)
    (components-utils/set-visibility object visible?)))

(defn- set-z-index
  [object index]
  (when (some? object)
    (components-utils/set-z-index object index)))

(re-frame/reg-fx
  :flip-forward
  (fn [{:keys [current-spread next-spread page-dimensions on-end]}]
    (let [{:keys [left-page-position right-page-position page-size]} page-dimensions

          current-right-mask-params page-size
          current-right-mask (create-mask current-right-mask-params)

          next-left-mask-params (assoc page-size :width 0)
          next-left-mask (create-mask next-left-mask-params)

          next-left-initial-position (+ (:x right-page-position) (:width page-size))
          next-left-destination-position (:x left-page-position)]

      (let [container (.-parent (:right current-spread))]
        (components-utils/set-sortable-children container true)
        (set-z-index (:left current-spread) 0)
        (set-z-index (:right current-spread) 2)
        (set-z-index (:left next-spread) 3)
        (set-z-index (:right next-spread) 1)
        (components-utils/sort-children container))

      ;; current left page
      (set-position (:left current-spread) left-page-position)
      (set-visibility (:left current-spread) true)

      ;; current right page
      (apply-mask (:right current-spread) current-right-mask)
      (set-position (:right current-spread) right-page-position)
      (set-visibility (:right current-spread) true)

      ;; next left page
      (apply-mask (:left next-spread) next-left-mask)
      (set-position (:left next-spread) (assoc right-page-position :x next-left-initial-position))
      (set-visibility (:left next-spread) true)

      ;; next right page
      (when (some? (:right next-spread))
        (set-position (:right next-spread) right-page-position)
        (set-visibility (:right next-spread) true))

      ; Animate
      (interpolate {:from        {:flipped-page-width         (:width current-right-mask-params)
                                  :flipped-page-back-position next-left-initial-position
                                  :flipped-page-back-width    0}
                    :to          {:flipped-page-width         0
                                  :flipped-page-back-position next-left-destination-position
                                  :flipped-page-back-width    (:width page-size)}
                    :on-progress (fn [{:keys [flipped-page-width flipped-page-back-position flipped-page-back-width]}]
                                   (update-mask current-right-mask (assoc next-left-mask-params :width flipped-page-width))
                                   (update-mask next-left-mask (assoc next-left-mask-params :width flipped-page-back-width))
                                   (set-position (:left next-spread) {:x flipped-page-back-position}))
                    :on-end      (fn []
                                   (set-visibility (:left current-spread) false)
                                   (set-visibility (:right current-spread) false)
                                   (on-end))}))))

(re-frame/reg-fx
  :flip-backward
  (fn [{:keys [flipped-page flipped-page-back flipped-page-back-neighbor on-end left-page-position right-page-position page-size]}]
    (let [flipped-page-front-mask (create-mask page-size)
          flipped-page-back-mask (create-mask page-size)]

      (let [container (.-parent flipped-page)]
        (components-utils/set-sortable-children container true)
        (set-z-index flipped-page 2)
        (set-z-index flipped-page-back 3)
        (set-z-index flipped-page-back-neighbor 1))

      ;; Flipped front side
      (set-position flipped-page left-page-position)
      (set-visibility flipped-page-back true)
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
                                   (components-utils/set-position flipped-page-back {:x (- back-page-x back-page-mask-x)})
                                   (update-mask flipped-page-back-mask (-> page-size
                                                                           (assoc :x back-page-mask-x)
                                                                           (assoc :width back-page-mask-width))))
                    :on-end      on-end}))))
