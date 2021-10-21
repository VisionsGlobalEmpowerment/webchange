(ns webchange.interpreter.renderer.scene.components.utils)

(defn remove-nil-fields [object]
  (apply merge (for [[k v] object :when (not (nil? v))] {k v})))

(defn set-not-nil-value
  [object name value]
  (when (-> value nil? not)
    (aset object name value)))

(defn get-position
  [display-object]
  (let [position (.-position display-object)]
    {:x (.-x position)
     :y (.-y position)}))

(defn get-stage-position
  [display-object]
  (let [stage? (nil? (.-parent display-object))]
    (if stage?
      {:x 0
       :y 0}
      (let [position (get-position display-object)
            parent-position (get-stage-position (.-parent display-object))]
        {:x (+ (:x position) (:x parent-position))
         :y (+ (:y position) (:y parent-position))}))))

(defn set-position
  ([display-object x y]
   (set-position display-object {:x x :y y}))
  ([display-object position]
   (let [{:keys [x y]} (merge (get-position display-object)
                              (remove-nil-fields position))]
     (-> (.-position display-object)
         (.set x y)))))

(defn get-scale
  ([display-object]
   (get-scale display-object {}))
  ([display-object {:keys [abs?] :or {abs? false}}]
   (let [scale (.-scale display-object)
         with-abs #(if abs? (Math/abs %) %)]
     {:x (with-abs (.-x scale))
      :y (with-abs (.-y scale))})))

(defn set-scale
  [display-object scale]
  (let [current-scale (get-scale display-object)
        prepared-scale (if (number? scale)
                         {:x scale
                          :y scale}
                         {:x (or (:x scale) (:scale-x scale) (:x current-scale))
                          :y (or (:y scale) (:scale-y scale) (:y current-scale))})
        {:keys [x y]} (merge (get-scale display-object)
                             (remove-nil-fields prepared-scale))]
    (-> (.-scale display-object)
        (.set x y))))

(defn get-pivot
  [display-object]
  (let [scale (.-pivot display-object)]
    {:x (.-x scale)
     :y (.-y scale)}))

(defn set-visibility
  [display-object visible?]
  (aset display-object "visible" visible?))

(defn get-visibility
  [display-object]
  (aget display-object "visible"))

(defn set-handler
  [display-object event-name event-handler]
  (let [synonyms {"click" ["click" "tap"]}
        button-events ["click"]]
    (aset display-object "interactive" true)
    (when (some #{event-name} button-events)
      (aset display-object "buttonMode" true))              ;; ToDo: Move "buttonMode" setting to components
    (if (contains? synonyms event-name)
      (doseq [event (get synonyms event-name)]
        (.on display-object event event-handler))
      (.on display-object event-name event-handler))))

(defn get-size
  [object]
  (let [bounds (.getLocalBounds object)]
    {:width  (.-width bounds)
     :height (.-height bounds)}))

(defn set-size
  [object {:keys [width height]}]
  (when width
    (set! (.-width object) width))
  (when height
    (set! (.-height object) height)))

(defn get-rotation
  [object]
  (.-angle object))

(defn set-rotation
  [object rotation]
  (when rotation
    (set! (.-angle object) rotation)))

(defn get-opacity
  [object]
  (.-alpha object))

(defn set-opacity
  [object alpha]
  (when alpha
    (set! (.-alpha object) alpha)))

(defn get-z-index
  [object]
  (.-zIndex object))

(defn set-z-index
  [object index]
  (when (number? index)
    (set! (.-zIndex object) index)))

(defn set-sortable-children
  [object value]
  (set! (.-sortableChildren object) value))

(defn sort-children
  [object]
  (.sortChildren object))

(defn emit
  [object event & args]
  (.apply (.-emit object) object (-> [event] (concat args) (clj->js))))

(defn set-text
  [text-object value]
  (aset text-object "text" value))
