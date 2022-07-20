(ns webchange.utils.uid)

(defn uuid
  []
  #?(:clj  (java.util.UUID/randomUUID)
     :cljs (random-uuid)))

(defn get-uid
  []
  (->> (uuid) (str) (take 8) (clojure.string/join "")))
