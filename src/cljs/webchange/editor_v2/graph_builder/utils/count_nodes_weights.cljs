(ns webchange.editor-v2.graph-builder.utils.count-nodes-weights
  (:require
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]))

(defn count-nodes-weights
  ([graph get-weight]
   (count-nodes-weights graph [:root :root] {} get-weight))
  ([graph [prev-node-name node-name] result get-weight]
   (let [node-data (get graph node-name)
         children (map :handler (get-children node-name node-data prev-node-name))
         result (reduce
                  (fn [result next-node-name]
                    (count-nodes-weights graph [node-name next-node-name] result get-weight))
                  result
                  children)]
     (let [current-value (get-weight node-data)
           children-values (flatten (map #(first (get result %)) children))
           children-sum (or (apply + children-values) 0)]
       (assoc result node-name (concat [(+ current-value children-sum)] children-values))))))

(defn weight-changer?
  [node-weights]
  (let [[node-weight & children-weights] node-weights
        max-children-weight (if (< 0 (count children-weights)) (apply max children-weights) 0)]
    (< max-children-weight node-weight)))
