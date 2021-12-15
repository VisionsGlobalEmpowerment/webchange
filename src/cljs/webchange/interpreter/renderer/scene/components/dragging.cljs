(ns webchange.interpreter.renderer.scene.components.dragging
  (:require
    [reagent.core :as r]
    [webchange.interpreter.interactions :as interactions]
    [webchange.interpreter.renderer.scene.components.dragging-utils :as u]))

(def empty-position {:x 0 :y 0})
(def mouse-position-data (r/atom {:x 0 :y 0}))

(defn get-mouse-position []
  @mouse-position-data)

(defn position-empty? []
  (= empty-position @mouse-position-data))

(defn- on-drag-start
  [this event]
  (when-not @interactions/user-interactions-blocked?
    (let [pointer-position (-> event .-data (.getLocalPosition (.-parent this)))
          pointer-x (.-x pointer-position)
          pointer-y (.-y pointer-position)
          object-x (-> this .-position .-x)
          object-y (-> this .-position .-y)
          drag-offset {:x (- object-x pointer-x) :y (- object-y pointer-y)}]
      (reset! mouse-position-data {:x (-> event .-data .-global .-x) :y (-> event .-data .-global .-y)})
      (set! (.-drag-offset this) drag-offset))
    (set! (.-data this) (.-data event))
    (set! (.-alpha this) 0.9)
    (u/set-prop this "dragging" true)
    (u/call-handler this "drag-start-handler")))

(defn- on-drag-end
  [this _]
  (u/call-handler this "drag-end-handler")
  (reset! mouse-position-data empty-position)
  (set! (.-data this) nil)
  (set! (.-alpha this) 1)
  (u/set-prop this "dragging" false))

(defn- on-drag-move
  [this _]
  (let [new-position (-> this .-data (.getLocalPosition (.-parent this)))
        {offset-x :x offset-y :y} (-> this .-drag-offset)]
    (reset! mouse-position-data {:x (-> this .-data .-global .-x) :y (-> this .-data .-global .-y)})
    (set! (.-x this) (+ offset-x (.-x new-position)))
    (set! (.-y this) (+ offset-y (.-y new-position))))
  (u/call-handler this "drag-move-handler"))

;; ---

(defn- get-cursor-position
  [event object]
  (-> event .-data (.getLocalPosition (.-parent object))))

(defn- get-distance
  [p1 p2]
  (if (and (some? p1) (some? p2))
    (let [x (- (.-x p2) (.-x p1))
          y (- (.-y p2) (.-y p1))]
      (Math/sqrt (+ (Math/pow x 2)
                    (Math/pow y 2))))
    0))

(defn- handle-pointer-down
  [event]
  (this-as this
    (u/set-prop this "object-picked" true)
    (u/set-prop this "cursor-start-position" (get-cursor-position event this))))

(defn- handle-pointer-up
  [event]
  (this-as this
    (if-not (u/get-prop this "object-moved")
      (u/call-click-handler this [event])
      (on-drag-end this event))
    (u/set-prop this "object-moved" false)
    (u/set-prop this "object-picked" false)))

(defn- handle-pointer-move
  [event]
  (this-as this
    (let [object-picked? (u/get-prop this "object-picked")
          object-moved? (u/get-prop this "object-moved")
          object-moved-enough? (< 2 (get-distance (get-cursor-position event this)
                                                  (u/get-prop this "cursor-start-position")))]
      (when (and object-picked?
                 object-moved-enough?)
        (u/set-prop this "object-moved" true)
        (if object-moved?
          (on-drag-move this event)
          (on-drag-start this event))))))

(defn enable-drag!
  [object {on-drag-end-handler   :on-drag-end
           on-drag-start-handler :on-drag-start
           on-drag-move-handler  :on-drag-move
           on-drag-move-options  :on-drag-move-options}]
  (doto object
    (set! -interactive true)

    (u/hide-click-handler)
    (u/set-handler "drag-start-handler" on-drag-start-handler)
    (u/set-handler "drag-move-handler" (u/throttle-handler on-drag-move-handler on-drag-move-options))
    (u/set-handler "drag-end-handler" on-drag-end-handler)

    (.on "pointerdown" handle-pointer-down)
    (.on "pointerup" handle-pointer-up)
    (.on "pointerupoutside" handle-pointer-up)
    (.on "pointermove" handle-pointer-move)))
