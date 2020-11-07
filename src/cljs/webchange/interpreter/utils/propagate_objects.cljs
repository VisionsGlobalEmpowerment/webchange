(ns webchange.interpreter.utils.propagate-objects)

(defn- populate-string
  [string context]
  (if-let [matches (re-seq #"\{(.*?)\}" string)]
    (reduce (fn [result [sub-string field-name]]
              (let [value-path (map keyword (clojure.string/split field-name "."))]
                (clojure.string/replace result
                                        sub-string
                                        (get-in context value-path))))
            string
            matches)
    string))

(defn- populate
  [value context]
  (cond
    (string? value) (populate-string value context)
    (map? value) (reduce (fn [result [field-name field-value]]
                           (assoc result field-name (populate field-value context)))
                         value
                         value)
    (sequential? value) (map #(populate % context) value)
    :else value))

(def dimension-changers {:dx (fn [object current-dimension] (-> object
                                                                (assoc :x (+ (:x current-dimension)
                                                                             (:dx object)))
                                                                (dissoc :dx)))
                         :dy (fn [object current-dimension] (-> object
                                                                (assoc :y (+ (:y current-dimension)
                                                                             (:dy object)))
                                                                (dissoc :dy)))})

(defn- change-dimension
  [object current-dimension]
  (if (map? object)
    (reduce (fn [result [field-name field-value]]
              (if (contains? dimension-changers field-name)
                ((get dimension-changers field-name) result current-dimension)
                (assoc result field-name (cond
                                           (map? field-value) (change-dimension field-value current-dimension)
                                           (sequential? field-value) (map #(change-dimension % current-dimension) field-value)
                                           :else field-value))))
            object
            object)
    object))

(defn- get-lines-number
  [{:keys [container-width container-height item-width item-height items-number]}]
  (let [container-size-ratio (/ container-width container-height)
        max-lines-number (Math/floor (/ container-height item-height))
        items-in-line (Math/floor (/ container-width item-width))]
    (loop [lines-number 1]
      (let [block-size-ratio (/ (* items-in-line item-width)
                                (* lines-number item-height))]
        (cond
          (>= (* lines-number items-in-line) items-number) lines-number
          (= lines-number max-lines-number) lines-number
          (< block-size-ratio container-size-ratio) (dec lines-number)
          :else (recur (inc lines-number)))))))

(defn- get-items-places
  [{:keys [items-number] :as params}]
  (let [lines-number (get-lines-number params)
        items-in-line (int (Math/ceil (/ items-number lines-number)))
        last-line-padding (int (Math/floor (/ (- items-in-line (mod items-number items-in-line)) 2)))]
    (loop [counter 0
           x 0
           y 0
           result []]
      (if (= counter items-number)
        [result lines-number items-in-line]
        (let [last-in-line? (= x (dec items-in-line))
              next-y (if last-in-line? (inc y) y)
              next-y-last? (= next-y (dec lines-number))
              next-x (if last-in-line?
                       (if next-y-last?
                         last-line-padding
                         0)
                       (inc x))]
          (recur (inc counter) next-x next-y (conj result {:x x :y y})))))))

(defn get-propagated-objects
  [propagate-data items]
  (let [container-width (:width propagate-data)
        container-height (:height propagate-data)
        item-width (:el-width propagate-data)
        item-height (:el-height propagate-data)
        [items-places lines-number items-in-line] (get-items-places {:container-width  container-width
                                                                     :container-height container-height
                                                                     :item-width       item-width
                                                                     :item-height      item-height
                                                                     :items-number     (count items)})
        free-space-h (- container-width (* items-in-line item-width))
        step-h (/ free-space-h items-in-line)

        free-space-v (- container-height (* lines-number item-height))
        step-v (/ free-space-v lines-number)]
    (->> items
         (map-indexed
           (fn [index item]
             (let [current-position {:x (+ (/ step-h 2)
                                           (* (:x (nth items-places index))
                                              (+ step-h (:el-width propagate-data))))
                                     :y (+ (/ step-v 2)
                                           (* (:y (nth items-places index))
                                              (+ step-v (:el-height propagate-data))))}]
               (->> (:el propagate-data)
                    (map (fn [object-template]
                           (-> object-template
                               (populate item)
                               (change-dimension current-position))))
                    (reduce (fn [[objects scene-objects] {:keys [name data add-to-scene-objects]}]
                              [(assoc objects (keyword name) data)
                               (if-not (= add-to-scene-objects false)
                                 (conj scene-objects name)
                                 scene-objects)])
                            [{} []])))))
         (reduce (fn [result [objects scene-objects]]
                   (-> result
                       (update-in [:objects] merge objects)
                       (update-in [:scene-objects] concat scene-objects)))
                 {:objects       {}
                  :scene-objects []}))))

(defn replace-object
  [scene-objects object-to-replace new-objects]
  (map (fn [layer-objects]
         (->> layer-objects
              (map (fn [object] (if (= object object-to-replace)
                                  new-objects
                                  object)))
              (flatten)))
       scene-objects))
