(ns webchange.interpreter.utils)

(defn tag-name->tag-map
  [tag-name]
  (assoc {} (keyword (str tag-name "?")) true))

(defn tag-objects-map
  [m tag-name]
  (let [tag (tag-name->tag-map tag-name)]
    (reduce
      (fn [result [key val]]
        (assoc result key (merge val tag)))
      {}
      (seq m))))

(defn tag-objects-vec
  [v tag-name]
  (let [tag (tag-name->tag-map tag-name)]
    (map (fn [object] (merge object tag)) v)))

(defn add-scene-tag
  [scene tag-name]
  (cond-> scene
          (contains? scene :actions) (assoc :actions (tag-objects-map (:actions scene) tag-name))
          (contains? scene :objects) (assoc :objects (tag-objects-map (:objects scene) tag-name))
          (contains? scene :assets) (assoc :assets (tag-objects-vec (:assets scene) tag-name))))

(defn merge-scene-data
  [main-scene scenes]
  (let [all-scenes (concat [main-scene] scenes)
        has-field (fn [field] (some #(contains? % field) all-scenes))
        get-fields (fn [field] (map field all-scenes))]
    (cond-> main-scene
            (has-field :actions) (assoc :actions (apply merge (get-fields :actions)))
            (has-field :objects) (assoc :objects (apply merge (get-fields :objects)))
            (has-field :assets) (assoc :assets (vec (distinct (apply concat (get-fields :assets)))))
            (has-field :scene-objects) (assoc :scene-objects (vec (apply concat (get-fields :scene-objects)))))))
