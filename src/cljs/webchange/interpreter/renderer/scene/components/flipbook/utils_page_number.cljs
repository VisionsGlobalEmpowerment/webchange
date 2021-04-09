(ns webchange.interpreter.renderer.scene.components.flipbook.utils-page-number
  (:require
    [webchange.interpreter.renderer.scene.app :refer [get-stage]]
    [webchange.interpreter.renderer.scene.components.utils :refer [set-text set-visibility]]))

(defn- get-object-by-name
  [name]
  (-> (get-stage)
      (.getChildByName name true)))

(defn- set-pages-numbers-visibility
  [value]
  (let [pages-numbers-group (get-object-by-name "page-numbers")]
    (when (some? pages-numbers-group)
      (set-visibility pages-numbers-group value))))

(defn- set-page-number-value
  [side value]
  (let [page-number-name (str side "-page-number")
        pages-numbers-object (get-object-by-name page-number-name)]
    (when (some? pages-numbers-object)
      (set-text pages-numbers-object value))))

(defn hide-pages-numbers
  []
  (set-pages-numbers-visibility false))

(defn show-pages-numbers
  []
  (set-pages-numbers-visibility true))

(defn set-pages-numbers
  [page-numbers]
  (doseq [side [:left :right]]
    (let [page-number (get page-numbers side "")]
      (set-page-number-value (clojure.core/name side) (if (= page-number 0) "" page-number)))))
