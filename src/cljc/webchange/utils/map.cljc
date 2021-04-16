(ns webchange.utils.map)

(defn ignore-keys
  [object keys-to-ignore]
  (->> object
       (filter (fn [[field-name]]
                 (->> keys-to-ignore (some #{field-name}) (not))))
       (into {})))
