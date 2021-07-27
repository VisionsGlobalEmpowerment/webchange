(ns webchange.utils.deep-merge)

(defn deep-merge [v & vs]
  (letfn [(rec-merge [v1 v2]
            (if (and (map? v1) (map? v2))
              (merge-with deep-merge v1 v2)
              (or v2 v1)))]
    (if (some identity vs)
      (reduce #(rec-merge %1 %2) v vs)
      v)))
