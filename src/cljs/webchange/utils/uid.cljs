(ns webchange.utils.uid)

(defn get-uid
  []
  (->> (random-uuid) (str) (take 8) (clojure.string/join "")))
