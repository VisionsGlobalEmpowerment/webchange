(ns webchange.interpreter.renderer.scene.components.dragging
  (:require
    [reagent.core :as r]
    [webchange.interpreter.interactions :as interactions]))

(def empty-position {:x 0 :y 0})
(def mouse-position-data (r/atom {:x 0 :y 0}))

(defn get-mouse-position []
  @mouse-position-data)

(defn position-empty? []
  (= empty-position @mouse-position-data))

(defn- on-drag-start
  [event]
  (when-not @interactions/user-interactions-blocked?
    (this-as this
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
      (set! (.-dragging this) true)
      (when (and (.-dragging this) (.-on-drag-start-handler this))
        ((.-on-drag-start-handler this))))))

(defn- on-drag-end
  []
  (this-as this
    (when (and (.-dragging this) (.-on-drag-end-handler this))
      ((.-on-drag-end-handler this)))
    (reset! mouse-position-data empty-position)
    (set! (.-data this) nil)
    (set! (.-alpha this) 1)
    (set! (.-dragging this) false)))

(defn- on-drag-move
  []
  (this-as this
    (when (.-dragging this)
      (let [new-position (-> this .-data (.getLocalPosition (.-parent this)))
            {offset-x :x offset-y :y} (-> this .-drag-offset)]
        (reset! mouse-position-data {:x (-> this .-data .-global .-x) :y (-> this .-data .-global .-y)})
        (set! (.-x this) (+ offset-x (.-x new-position)))
        (set! (.-y this) (+ offset-y (.-y new-position))))
      (when (.-on-drag-move-handler this)
        ((.-on-drag-move-handler this))))))

(defn- throttle-by-callback
  [handler]
  (let [in-progress? (atom false)]
    (fn [this]
      (when-not @in-progress?
        (reset! in-progress? true)
        (handler this #(reset! in-progress? false))))))

(defn- throttle
  [handler options]
  (case (:throttle options)
    "action-done" (throttle-by-callback handler)
    handler))

(defn enable-drag!
  [object {on-drag-end-handler   :on-drag-end
           on-drag-start-handler :on-drag-start
           on-drag-move-handler  :on-drag-move
           on-drag-move-options  :on-drag-move-options}]
  (doto object
    (set! -interactive true)
    (set! -on-drag-end-handler on-drag-end-handler)
    (set! -on-drag-start-handler on-drag-start-handler)
    (set! -on-drag-move-handler (throttle on-drag-move-handler on-drag-move-options))
    (.on "pointerdown" on-drag-start)
    (.on "pointerup" on-drag-end)
    (.on "pointerupoutside" on-drag-end)
    (.on "pointermove" on-drag-move)))
