(ns webchange.editor-v2.graph-builder.scene-parser.scene-parser-objects
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node :refer [create-graph-node]]))

(defn parse-object
  [object-name object-data]
  (let [actions-data (map
                       (fn [[_ {:keys [id on]}]]
                         {:name    on
                          :handler id})
                       (:actions object-data))]
    (assoc {} object-name (create-graph-node {:data        object-data
                                              :path        [object-name]
                                              :connections actions-data}))))

(defn parse-objects
  [scene-data]
  (->> (:objects scene-data)
       (reduce
         (fn [result [object-name object-data]]
           (merge result (parse-object object-name object-data)))
         {})))
