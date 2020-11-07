(ns webchange.interpreter.renderer.scene.components.dragging)

(defn- on-drag-start
  [event]
  (this-as this
    (let [pointer-position (-> event .-data (.getLocalPosition (.-parent this)))
          pointer-x (.-x pointer-position)
          pointer-y (.-y pointer-position)
          object-x (-> this .-position .-x)
          object-y (-> this .-position .-y)
          drag-offset {:x (- object-x pointer-x) :y (- object-y pointer-y)}]
      (set! (.-drag-offset this) drag-offset))
    (set! (.-data this) (.-data event))
    (set! (.-alpha this) 0.9)
    (set! (.-dragging this) true)))

(defn- on-drag-end
  []
  (this-as this
    (when (and (.-dragging this) (.-on-drag-end-handler this))
      ((.-on-drag-end-handler this)))
    (set! (.-data this) nil)
    (set! (.-alpha this) 1)
    (set! (.-dragging this) false)))

(defn- on-drag-move
  []
  (this-as this
    (when (.-dragging this)
      (let [new-position (-> this .-data (.getLocalPosition (.-parent this)))
            {offset-x :x offset-y :y} (-> this .-drag-offset)]
        (set! (.-x this) (+ offset-x (.-x new-position)))
        (set! (.-y this) (+ offset-y (.-y new-position)))))))

(defn enable-drag!
  [object on-drag-end-handler]
  (doto object
    (set! -interactive true)
    (set! -on-drag-end-handler on-drag-end-handler)
    (.on "pointerdown" on-drag-start)
    (.on "pointerup" on-drag-end)
    (.on "pointerupoutside" on-drag-end)
    (.on "pointermove" on-drag-move)))
