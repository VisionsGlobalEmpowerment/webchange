(ns webchange.editor-v2.diagram.scene-parser.duplicates-replicator.usages-counter
  (:require
    [webchange.editor-v2.diagram.scene-parser.duplicates-replicator.utils :refer [add-to-map
                                                                                  add-root-node
                                                                                  remove-root-node
                                                                                  get-next-children]]))

(defn count-node
  [graph node-name prev-prev-node-name counter-map]
  (let [count-node? (or (nil? prev-prev-node-name)
                        (not (= "parallel" (get-in graph [prev-prev-node-name :type])))
                        (nil? (get counter-map node-name)))]
    (if count-node?
      (first (add-to-map counter-map node-name))
      counter-map))
  )

(defn count-usages-dfs
  "Depth-first-search"
  ([graph]
   (count-usages-dfs graph [nil :root :root] {}))
  ([graph [prev-prev-node-name prev-node-name node-name] result]
   (let [node-data (get graph node-name)]
     (reduce
       (fn [result next-node-name]
         (count-usages-dfs graph [prev-node-name node-name next-node-name] result))
       (count-node graph node-name prev-prev-node-name result)
       (get-next-children node-data prev-node-name)))))

(defn count-node-usages
  [parsed-data start-nodes]
  (-> parsed-data
      (add-root-node start-nodes)
      (count-usages-dfs)
      (remove-root-node)))

(defn get-reused-nodes
  [usages-map]
  (reduce
    (fn [result [node-name node-usages]]
      (if (> node-usages 1)
        (assoc result node-name 0)
        result))
    {}
    usages-map))

(defn get-reuse-map
  [parsed-data root-nodes]
  (-> (count-node-usages parsed-data root-nodes)
      (get-reused-nodes)))
