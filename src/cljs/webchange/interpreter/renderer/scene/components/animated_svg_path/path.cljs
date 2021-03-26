(ns webchange.interpreter.renderer.scene.components.animated-svg-path.path
  (:require
    [webchange.common.svg-path.path-element :refer [length ->points]]
    [webchange.common.svg-path.path-splitter :refer [split-path-str-with]]
    [webchange.common.svg-path.path-to-transitions :refer [get-moves-lengths]]))

(def precision 0.5)

(defn paths
  [data duration]
  (let [paths (split-path-str-with data ["M"])
        moves-lengths (get-moves-lengths data)
        path-lengths (map length paths)
        path-points (map #(->points % precision) paths)
        visible-length (reduce + path-lengths)
        total-length (+ visible-length (reduce + moves-lengths))
        move-time #(-> duration (* %) (/ total-length))
        ->path-info (fn [move-length path-length path-points path]
                      {:path     path
                       :length   path-length
                       :points   path-points
                       :delay    (move-time move-length)
                       :duration (move-time path-length)})]
    (->> (map ->path-info moves-lengths path-lengths path-points paths)
         (into []))))
