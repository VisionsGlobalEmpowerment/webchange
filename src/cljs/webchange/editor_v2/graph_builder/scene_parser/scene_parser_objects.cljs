(ns webchange.editor-v2.graph-builder.scene-parser.scene-parser-objects
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node :refer [create-graph-node]]))

(defn parse-object
  [object-name object-data]
  (let [actions-data (map
                       (fn [[_ {:keys [id on]}]]
                         {:name    on
                          :handler id})
                       (:actions object-data))
        object-root-info (if (= "text" (:type object-data)) {:name :default :handler :default})]
    (assoc {} object-name (create-graph-node {:entity      :object
                                              :data        object-data
                                              :path        [object-name]
                                              :connections (conj actions-data object-root-info)}))))

(defn parse-objects
  [scene-data]
  (->> (:objects scene-data)
       (reduce
         (fn [result [object-name object-data]]
           (merge result (parse-object object-name object-data)))
         {})))
