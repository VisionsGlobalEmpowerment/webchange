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
  [{:keys [x y width height] :as params
    :or   {x      0
           y      0
           width  0
           height 0}}]
  (print "create-mask" x y width height)
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

(re-frame/reg-event-fx
  ::execute-flip
  (fn [{:keys [db]} [_ {:keys [direction flipped-page flipped-page-back flipped-page-back-neighbor] :as params}]]
    (let [params (-> params
                     (merge {:flipped-page               (get-container db flipped-page)
                             :flipped-page-back          (get-container db flipped-page-back)
                             :flipped-page-back-neighbor (get-container db flipped-page-back-neighbor)}))]
      (case direction
        "forward" {:flip-forward params}
        "backward" {:flip-backward params}))))

(defn- set-z-index
  [object index]
  (when (some? object)
    (components-utils/set-z-index object index)))

(re-frame/reg-fx
  :flip-forward
  (fn [{:keys [flipped-page flipped-page-back flipped-page-back-neighbor left-page-position right-page-position page-size on-end]}]
    (let [flipped-page-position (components-utils/get-position flipped-page)

          flipped-page-size (components-utils/get-size flipped-page)
          flipped-page-back-size (components-utils/get-size flipped-page-back)

          flipped-page-mask-params flipped-page-size
          flipped-page-mask (create-mask flipped-page-mask-params)

          flipped-page-back-mask-params {:width  0
                                         :height (:height flipped-page-back-size)}
          flipped-page-back-mask (create-mask flipped-page-back-mask-params)
          flipped-page-back-initial-position (+ (:x flipped-page-position) (:width flipped-page-size))
          flipped-page-back-destination-position (- (:x flipped-page-position) (:width flipped-page-size))]

      (let [container (.-parent flipped-page)]
        (components-utils/set-sortable-children container true)
        (set-z-index flipped-page 2)
        (set-z-index flipped-page-back 3)
        (set-z-index flipped-page-back-neighbor 1))

      ;; Flipped front side
      (apply-mask flipped-page flipped-page-mask)
      (components-utils/set-position flipped-page right-page-position)

      ;; Flipped back side
      (print ">> Flipped back side")
      (print "set-position" right-page-position)
      (print "flipped-page-back-mask-params" flipped-page-back-mask-params)

      (apply-mask flipped-page-back flipped-page-back-mask)
      (components-utils/set-position flipped-page-back right-page-position)
      (components-utils/set-visibility flipped-page-back true)

      ;; Flipped back side neighbor
      (when (some? flipped-page-back-neighbor)
        (components-utils/set-position flipped-page-back-neighbor right-page-position)
        (components-utils/set-visibility flipped-page-back-neighbor true))

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
                                   (components-utils/set-position flipped-page-back {:x flipped-page-back-position}))
                    :on-end      on-end}))))

(re-frame/reg-fx
  :flip-backward
  (fn [{:keys [flipped-page flipped-page-back flipped-page-back-neighbor on-end left-page-position right-page-position page-size]}]
    (let [_ (print "flipped-page-front-mask")
          flipped-page-front-mask (create-mask page-size)
          flipped-page-back-mask (create-mask page-size)]

      (print "page-size" page-size)

      (let [container (.-parent flipped-page)]
        (components-utils/set-sortable-children container true)
        (set-z-index flipped-page 2)
        (set-z-index flipped-page-back 3)
        (set-z-index flipped-page-back-neighbor 1))

      ;; Flipped front side
      (components-utils/set-position flipped-page left-page-position)
      (components-utils/set-visibility flipped-page-back true)
      (apply-mask flipped-page flipped-page-front-mask)

      ;; Flipped back side
      (components-utils/set-position flipped-page-back left-page-position)
      (apply-mask flipped-page-back flipped-page-back-mask)
      (components-utils/set-visibility flipped-page-back true)

      ;; Flipped back side neighbor
      (when (some? flipped-page-back-neighbor)
        (components-utils/set-visibility flipped-page-back-neighbor true))

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
