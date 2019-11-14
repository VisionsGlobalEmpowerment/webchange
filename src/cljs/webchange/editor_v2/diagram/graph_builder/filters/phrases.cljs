(ns webchange.editor-v2.diagram.graph-builder.filters.phrases
  (:require
    [webchange.editor-v2.diagram.graph-builder.utils.node-children :refer [get-children]]
    [webchange.editor-v2.diagram.graph-builder.utils.change-node :refer [remove-node]]
    [webchange.editor-v2.diagram.graph-builder.utils.root-nodes :refer [add-root-node
                                                                        get-root-nodes
                                                                        remove-root-node]]))

(defn weight-changer-node?
  [node-name weights]
  (let [[node-weight & children-weights] (get weights node-name)]
    (> node-weight (apply max children-weights))))

(defn root-node-data?
  [node-data]
  (contains? (:connections node-data) :root))

(defn not-empty-tree-root?
  [node-name node-data weights]
  (let [[node-weight] (get weights node-name)]
    (and (root-node-data? node-data)
         (> node-weight 0))))

(defn should-remove-node?
  [node-name node-data weights]
  (and (not (weight-changer-node? node-name weights))
       (not (not-empty-tree-root? node-name node-data weights))))

(defn remove-extra-nodes-dfs
  ([graph weights]
   (remove-extra-nodes-dfs graph [:root :root] weights))
  ([graph [prev-node-name node-name] weights]
   (let [node-data (get graph node-name)
         remove? (should-remove-node? node-name node-data weights)]
     (reduce
       (fn [graph next-node-name]
         (remove-extra-nodes-dfs graph [(if remove? prev-node-name node-name) next-node-name] weights))
       (if remove?
         (remove-node graph node-name)
         graph)
       (get-children node-data prev-node-name)))))

(defn remove-extra-nodes
  [graph subtree-phrase-weights]
  (remove-extra-nodes-dfs graph subtree-phrase-weights))

(defn phrase-node-data?
  [node-data]
  (contains? (:data node-data) :phrase))

(defn count-subtree-phrases
  "Counts how much phrase nodes contains current node's subtree and it's children"
  ([graph]
   (count-subtree-phrases graph [:root :root] {}))
  ([graph [prev-node-name node-name] result]
   (let [node-data (get graph node-name)
         children (get-children node-data prev-node-name)
         result (reduce
                  (fn [result next-node-name]
                    (count-subtree-phrases graph [node-name next-node-name] result))
                  result
                  children)]
     (let [current-value (if (phrase-node-data? node-data) 1 0)
           children-values (flatten (map #(first (get result %)) children))
           children-sum (or (apply + children-values) 0)]
       (assoc result node-name (concat [(+ current-value children-sum)] children-values))))))

(defn get-phrases-graph
  [scene-graph]
  (let [graph (->> scene-graph
                   (get-root-nodes)
                   (add-root-node scene-graph))
        subtree-phrase-weights (count-subtree-phrases graph)]
    (-> graph
        (remove-extra-nodes subtree-phrase-weights)
        (remove-root-node))))
