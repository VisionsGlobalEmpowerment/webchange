(ns webchange.utils.map)

(defn ignore-keys
  [object keys-to-ignore]
  (->> object
       (filter (fn [[field-name]]
                 (->> keys-to-ignore (some #{field-name}) (not))))
       (into {})))

(defn remove-nil-fields
  [object]
  (apply merge (for [[k v] object :when (not (nil? v))] {k v})))

(defn map-keys
  [data keys-map]
  (->> (keys keys-map)
       (select-keys data)
       (map (fn [[field-name field-value]]
              [(get keys-map field-name) field-value]))
       (into {})))

(defn map->list
  [data]
  (->> data
       (map vector)
       (reduce concat)
       (reduce concat)
       (vec)))

(comment
  (map->list {:a 1 :b 2}))