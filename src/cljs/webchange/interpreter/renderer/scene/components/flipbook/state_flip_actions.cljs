(ns webchange.interpreter.renderer.scene.components.flipbook.state-flip-actions
  (:require
    ["gsap/umd/TweenMax" :refer [TweenMax]]
    [re-frame.core :as re-frame]
    [webchange.interpreter.pixi :refer [Graphics]]
    [webchange.interpreter.renderer.scene.components.utils :as components-utils]
    [webchange.interpreter.renderer.state.scene :as scene]))

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
        transition (or
                     (some-> db (get-in [:transitions scene-id transition-id]) deref)
                     (scene/get-scene-object db (keyword transition-id)))]
    (when (some? transition)
      (:object transition))))

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

(defn- set-z-indexes
  [spreads indexes]
  (let [current-page (or (get-in spreads [:current :left])
                         (get-in spreads [:current :right]))
        container (.-parent current-page)]
    (components-utils/set-sortable-children container true)
    (doseq [path [[:current :left]
                  [:current :right]
                  [:next :left]
                  [:next :right]]]
      (set-z-index (get-in spreads path)
                   (get-in indexes path)))
    (components-utils/sort-children container)))

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
      (set-z-indexes {:current current-spread
                      :next    next-spread}
                     {:current {:left  0
                                :right 2}
                      :next    {:left  3
                                :right 1}})

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
      (set-position (:right next-spread) right-page-position)
      (set-visibility (:right next-spread) true)

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
  (fn [{:keys [current-spread next-spread page-dimensions on-end]}]
    (let [{:keys [left-page-position right-page-position page-size]} page-dimensions

          current-left-mask-params page-size
          current-left-mask (create-mask current-left-mask-params)

          next-right-mask-params (assoc page-size :width 0)
          next-right-mask (create-mask next-right-mask-params)

          next-right-initial-position (- (:x left-page-position) (:width page-size))
          next-right-destination-position (:x right-page-position)]
      (set-z-indexes {:current current-spread
                      :next    next-spread}
                     {:current {:left  2
                                :right 0}
                      :next    {:left  1
                                :right 3}})

      ;; current left page
      (apply-mask (:left current-spread) current-left-mask)
      (set-position (:left current-spread) left-page-position)
      (set-visibility (:left current-spread) true)

      ;; current right page
      (set-position (:right current-spread) right-page-position)
      (set-visibility (:right current-spread) true)

      ;; next left page
      (set-position (:left next-spread) left-page-position)
      (set-visibility (:left next-spread) true)

      ;; next right page
      (apply-mask (:right next-spread) next-right-mask)
      (set-position (:right next-spread) (assoc left-page-position :x next-right-initial-position))
      (set-visibility (:right next-spread) true)

      ; Animate
      (interpolate {:from        {:flipped-page-width         (:width current-left-mask-params)
                                  :flipped-page-back-position next-right-initial-position
                                  :flipped-page-back-width    0}
                    :to          {:flipped-page-width         0
                                  :flipped-page-back-position next-right-destination-position
                                  :flipped-page-back-width    (:width page-size)}
                    :on-progress (fn [{:keys [flipped-page-width flipped-page-back-position flipped-page-back-width]}]
                                   (update-mask current-left-mask (-> next-right-mask-params
                                                                      (assoc :x flipped-page-back-width)
                                                                      (assoc :width flipped-page-width)))
                                   (update-mask next-right-mask (-> next-right-mask-params
                                                                    (assoc :x flipped-page-width)
                                                                    (assoc :width flipped-page-back-width)))
                                   (set-position (:right next-spread) {:x flipped-page-back-position}))
                    :on-end      (fn []
                                   (set-visibility (:left current-spread) false)
                                   (set-visibility (:right current-spread) false)
                                   (on-end))}))))
