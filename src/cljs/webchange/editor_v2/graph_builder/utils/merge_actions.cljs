(ns webchange.editor-v2.graph-builder.utils.merge-actions
  (:require
    [clojure.set :refer [intersection
                         union]]))

(defn- seq-intersection
  [seq-1 seq-2]
  (vec (intersection (set seq-1) (set seq-2))))

(defn merge-actions
  [map-1 map-2]
  (let [actions-to-merge (seq-intersection (keys map-1) (keys map-2))
        actions-merged (reduce
                         (fn [result action-name]
                           (let [action-1-data (get map-1 action-name)
                                 action-2-data (get map-2 action-name)]
                             (assoc result action-name (merge action-1-data
                                                              action-2-data
                                                              {:connections (union (set (:connections action-1-data))
                                                                                   (set (:connections action-2-data)))}))))
                         {}
                         actions-to-merge)]
    (merge map-1 map-2 actions-merged)))
