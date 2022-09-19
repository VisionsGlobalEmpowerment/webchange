(ns webchange.ui.utils.get-data-attributes)

(defn get-data-attributes
  [data]
  (when (map? data)
    (->> data
         (map (fn [[field-name field-value]]
                [(->> field-name name (str "data-") keyword)
                 field-value]))
         (into {}))))
