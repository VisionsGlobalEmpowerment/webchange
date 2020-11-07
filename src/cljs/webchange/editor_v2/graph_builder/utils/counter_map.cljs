(ns webchange.editor-v2.graph-builder.utils.counter-map)

(defn add-to-map
  [map name]
  (let [key (keyword name)
        current-value (get map key)
        new-value (inc current-value)]
    [(assoc map key new-value) new-value]))

(defn map-has-name?
  [map name]
  (->> (keyword name)
       (contains? map)))
