(ns webchange.utils.session-storage)

(defn- get-storage
  []
  (.-sessionStorage js/window))

(defn clear
  []
  (-> (get-storage)
      (.clear)))

(defn get-item
  [key]
  (-> (get-storage)
      (.getItem key)))

(defn remove-item
  [key]
  (-> (get-storage)
      (.removeItem key)))

(defn set-item
  [key value]
  (-> (get-storage)
      (.setItem key value)))
