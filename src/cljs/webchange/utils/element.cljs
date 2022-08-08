(ns webchange.utils.element
  (:require
    [webchange.utils.numbers :refer [try-parse-number]]))

(defn add-class
  [el class-name]
  (-> (.-classList el)
      (.add class-name)))

(defn create
  ([]
   (create {}))
  ([{:keys [el]
     :or   {el "div"}}]
   (js/document.createElement "div")))

(defn get-first-child
  [el]
  (.-firstChild el))

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

(defn closest
  [el selector]
  (.closest el selector))

(defn insert-before
  ([parent-el inserted-el]
   (insert-before parent-el inserted-el nil))
  ([parent-el inserted-el reference-el]
   (.insertBefore parent-el inserted-el reference-el)))

(defn remove
  [el]
  (.remove el))

(defn remove-children
  [el]
  (while (get-first-child el)
    (-> el get-first-child remove)))
