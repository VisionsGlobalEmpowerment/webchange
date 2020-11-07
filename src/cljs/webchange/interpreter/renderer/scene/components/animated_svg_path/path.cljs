(ns webchange.interpreter.renderer.scene.components.animated-svg-path.path
  (:require
    [webchange.common.svg-path.path-length :refer [path-length]]
    [webchange.common.svg-path.path-splitter :refer [split-path-str-with]]
    [webchange.common.svg-path.path-to-transitions :refer [get-moves-lengths]]))

(defn paths
  [data duration]
  (let [paths (split-path-str-with data ["M"])
        moves-lengths (get-moves-lengths data)
        path-lengths (map path-length paths)
        visible-length (reduce + path-lengths)
        total-length (+ visible-length (reduce + moves-lengths))
        move-time #(-> duration (* %) (/ total-length))
        ->path-info (fn [move-length path-length path]
                      {:path     path
                       :length   path-length
                       :delay    (move-time move-length)
                       :duration (move-time path-length)})]
    (->> (map ->path-info moves-lengths path-lengths paths)
         (into []))))
