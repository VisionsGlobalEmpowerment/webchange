(ns webchange.utils.element
  (:require
    [webchange.utils.numbers :refer [try-parse-number]]))

(defn add-class
  [el class-name]
  (-> (.-classList el)
      (.add class-name)))

(defn remove-class
  [el class-name]
  (-> (.-classList el)
      (.remove class-name)))

(defn get-style
  [el]
  (or (.-currentStyle el)
      (.getComputedStyle js/window el)))

(defn get-style-prop
  ([el prop-name]
   (get-style-prop el prop-name {}))
  ([el prop-name {:keys [parse?] :or {parse? false}}]
   (cond-> (-> (get-style el)
               (aget prop-name))
           parse? (try-parse-number true))))
