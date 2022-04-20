(ns webchange.utils.map)

(defn ignore-keys
  [object keys-to-ignore]
  (->> object
       (filter (fn [[field-name]]
                 (->> keys-to-ignore (some #{field-name}) (not))))
       (into {})))

(defn remove-fields
  [object predicate]
  (apply merge (for [[k v] object :when (not (predicate v))] {k v})))

(defn remove-nil-fields
  [object]
  (remove-fields object nil?))

(defn remove-empty-fields
  [object]
  (remove-fields object empty?))

(defn map-keys
  [data keys-map]
  (->> (keys keys-map)
       (select-keys data)
       (map (fn [[field-name field-value]]
              [(get keys-map field-name) field-value]))
       (into {})))
