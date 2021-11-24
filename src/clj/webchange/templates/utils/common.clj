(ns webchange.templates.utils.common)

(defn init-metadata
  [metadata template args]
  (cond-> template
          (:actions metadata) (assoc-in [:metadata :actions] (:actions metadata))
          true (assoc-in [:metadata :next-action-index] 0)
          true (assoc-in [:metadata :unique-suffix] 0)))

(defn get-unique-suffix
  [scene-data]
  (get-in scene-data [:metadata :unique-suffix]))

(defn unique-suffix-first?
  [scene-data]
  (= 0 (get-unique-suffix scene-data)))

(defn get-prev-unique-suffix
  [scene-data]
  (dec (get-unique-suffix scene-data)))

(defn make-name-unique
  [scene-data name]
  (keyword (str name "-" (get-unique-suffix scene-data))))

(defn make-name-unique-by-suffix
  [name unique-suffix]
  (keyword (str name "-" unique-suffix)))

(defn make-prev-name-unique
  [scene-data name]
  (keyword (str name "-" (get-prev-unique-suffix scene-data))))

(defn get-replace-params
  [scene-data]
  (let [old-action-name (get-in scene-data [:metadata :last-insert])
        unique-suffix (get-unique-suffix scene-data)]
    {:old-action-name old-action-name
     :unique-suffix   unique-suffix
     :new-action-name (str old-action-name "-" unique-suffix)}))

(defn update-unique-suffix
  [scene-data]
  (update-in scene-data [:metadata :unique-suffix] inc))

(defn merge-new-action
  [scene-data actions {:keys [old-action-name new-action-name]}]
  (-> scene-data
      (assoc-in [:actions (keyword new-action-name)] (get-in scene-data [:actions (keyword old-action-name)]))
      (update-in [:actions] merge actions)
      (assoc-in [:metadata :last-insert] new-action-name)
      (update-unique-suffix)))

(defn remove-scene-object
  [scene-data scene-object]
  (let [scene-objects (:scene-objects scene-data)]
    (->> scene-objects
         (reduce (fn [result objects]
                   (if-let [current-objects (->> objects
                                                 (remove #(= scene-object %))
                                                 seq)]
                     (conj result (vec current-objects))
                     result)) [])
         (assoc scene-data :scene-objects))))

(defn remove-actions-by-key-value
  [actions key value]
  (vec (remove nil? (map (fn [action]
                           (if (not (= (key action) value))
                             action)
                           ) actions))))

(defn remove-object
  [scene-data object-name]
  (-> scene-data
      (assoc :objects (dissoc (:objects scene-data) object-name))
      (remove-scene-object (name object-name))))

(defn remove-asset
  [scene-data src]
  (-> scene-data
      (assoc :assets (vec (remove nil? (map (fn [asset] (if (not (= (:url asset) src)) asset)) (:assets scene-data)))))
      )
  )

(defn remove-action-from-tracks
  [scene-data scene-action-name]
  (assoc-in scene-data [:metadata :tracks] (reduce (fn [result track]
                                                     (let [current-track
                                                           (assoc track :nodes
                                                                        (remove-actions-by-key-value (:nodes track) :action-id (name scene-action-name))
                                                                        )]
                                                       (conj result current-track)
                                                       )) [] (get-in scene-data [:metadata :tracks]))))

(defn add-scene-object
  [scene-data scene-objects]
  (let [to-add (vec (filter (fn [object]
                              (= 0 (count (filter #(= % object) (flatten (:scene-objects scene-data)))))
                              ) scene-objects))]
    (if (not (= 0 (count to-add)))
      (-> scene-data
          (update-in [:scene-objects] conj to-add))
      scene-data)))

(defn add-track-action
  [scene-data {:keys [track-name type action-id]}]
  (let
    [step {:type type, :action-id action-id}
     item (->> (get-in scene-data [:metadata :tracks])
               (map-indexed vector)
               (filter #(= (:title (second %)) track-name))
               first)]
    (cond-> scene-data
            (not (get-in scene-data [:metadata :tracks])) (assoc-in [:metadata :tracks] [])
            item (update-in [:metadata :tracks (first item) :nodes] conj step)
            (not item) (update-in [:metadata :tracks] conj {:title track-name :nodes [step]}))))

(defn add-track-actions
  [scene-data action-names type track-name]
  (reduce (fn [scene-data action-name]
            (add-track-action scene-data {
                                          :track-name track-name
                                          :type       type
                                          :action-id  action-name
                                          })
            ) scene-data action-names))

(defn add-available-action
  [scene-data action-name effect-name]
  (update-in scene-data [:metadata :available-actions] concat [{:action action-name
                                                                :name   effect-name}]))

(defn add-highlight
  [scene-data object-name effect-name]
  (let [action-name (str "highlight-" object-name)]
    (-> scene-data
        (update-in [:objects (keyword object-name) :filters] concat [{:name "brightness" :value 0}
                                                                     {:name "glow" :outer-strength 0 :color 0xffd700}])
        (assoc-in [:actions (keyword action-name)] {:type               "transition"
                                                    :transition-id      object-name
                                                    :return-immediately true
                                                    :from               {:brightness 0 :glow 0}
                                                    :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.5 :repeat 5}})
        (add-available-action action-name effect-name))))

(defn add-blink
  [scene-data object-name effect-name]
  (let [action-name (str "blink-" object-name)]
    (-> scene-data
        (update-in [:objects (keyword object-name) :filters] concat [{:name "brightness" :value 0}
                                                                     {:name "glow" :outer-strength 0 :color 0xffd700}])
        (assoc-in [:actions (keyword action-name)] {:type               "transition"
                                                    :transition-id      object-name
                                                    :return-immediately true
                                                    :from               {:brightness 0 :glow 0}
                                                    :to                 {:brightness 1 :glow 10 :yoyo true :duration 0.2 :repeat 1}})
        (add-available-action action-name effect-name))))
