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
  [display-object position]
  (let [{:keys [x y]} (merge (get-position display-object)
                             (remove-nil-fields position))]
    (-> (.-position display-object)
        (.set x y))))

(defn get-scale
  [display-object]
  (let [scale (.-scale display-object)]
    {:x (.-x scale)
     :y (.-y scale)}))

(defn set-scale
  [display-object scale]
  (let [prepared-scale (if (number? scale)
                         {:x scale
                          :y scale}
                         {:x (or (:x scale) (:scale-x scale))
                          :y (or (:y scale) (:scale-y scale))})
        {:keys [x y]} (merge (get-scale display-object)
                             (remove-nil-fields prepared-scale))]
    (-> (.-scale display-object)
        (.set x y))))

(defn set-visibility
  [display-object visible?]
  (aset display-object "visible" visible?))

(defn set-handler
  [display-object event-name event-handler]
  (let [synonyms {"click" ["click" "tap"]}
        button-events ["click"]]
    (aset display-object "interactive" true)
    (when (some #{event-name} button-events)
      (aset display-object "buttonMode" true))                      ;; ToDo: Move "buttonMode" setting to components
    (if (contains? synonyms event-name)
      (doseq [event (get synonyms event-name)]
        (.on display-object event event-handler))
      (.on display-object event-name event-handler))))

(defn get-size
  [object]
  {:width  (.-width object)
   :height (.-height object)})

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
