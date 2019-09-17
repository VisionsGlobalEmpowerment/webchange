(ns webchange.interpreter.utils)

(defn merge-scene-data
  [main-scene scenes]
  (let [all-scenes (concat [main-scene] scenes)
        has-field (fn [field] (some #(contains? % field) all-scenes))
        get-fields (fn [field] (map field all-scenes))]
    (cond-> main-scene
            (has-field :actions) (assoc :actions (apply merge (get-fields :actions)))
            (has-field :objects) (assoc :objects (apply merge (get-fields :objects)))
            (has-field :assets) (assoc :assets (vec (distinct (apply concat (get-fields :assets)))))
            (has-field :scene-objects) (assoc :scene-objects (vec (apply concat (get-fields :scene-objects))))
            )))
