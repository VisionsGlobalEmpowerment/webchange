(ns webchange.admin.widgets.utils)

(defn get-uid
  []
  (->> (random-uuid) (str) (take 8) (clojure.string/join "")))
