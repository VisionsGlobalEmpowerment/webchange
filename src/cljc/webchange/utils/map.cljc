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
