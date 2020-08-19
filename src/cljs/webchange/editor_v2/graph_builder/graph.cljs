(ns webchange.editor-v2.graph-builder.graph
  (:require
    [clojure.pprint :as p]
    [webchange.editor-v2.graph-builder.filters.phrases :refer [get-phrases-graph]]
    [webchange.editor-v2.graph-builder.filters.translation :refer [get-translation-graph]]
    [webchange.editor-v2.graph-builder.scene-parser.concepts-replacer.replacer :refer [override-concept-actions
                                                                                       graph-has-concepts?]]
    [webchange.editor-v2.graph-builder.scene-parser.scene-parser :refer [parse-data]]
    [webchange.editor-v2.graph-builder.graph-normalizer.graph-normalizer :refer [normalize-graph]]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [get-root-nodes]]))

(defn parse-scene
  "Return a map of prepared nodes
  Each node has:
  :data - raw data of original entity (action, trigger, object)
  :path - a vector of names and indexes to find original entity in the scene (e.g. [:pick-correct 2], where :pick-correct is a name of action, 2 is an index of sequence-data element)
  :entity - type of the node :trigger, :object, :action
  :children - a vector of node names inside current node (in 'action', 'test', 'sequence', 'parallel' etc. action types)
  :connections - a set of outgoing connections, each with :handler (next node name), :previous (previous node name), :sequence (parent node name in case of 'sequence' or 'parallel' action)"
  ([scene-data]
   (parse-scene scene-data {}))
  ([scene-data {:keys [start-node concept-data]}]
   (-> scene-data
       (parse-data start-node)
       (normalize-graph)
       (override-concept-actions concept-data))))

(defn print-result
  [graph diagram-mode]
  (when (= :phrases diagram-mode)
    (->> graph
         (reduce (fn [result [node-name node-data]]
                   (assoc result node-name (dissoc node-data :data)))
                 {})
         (p/pprint))))

(defn get-diagram-graph
  ([scene-data diagram-mode]
   (get-diagram-graph scene-data diagram-mode {}))
  ([scene-data diagram-mode params]
   (let [result (cond
                  (= diagram-mode :translation) (let [graph (-> scene-data
                                                                (parse-scene (select-keys params [:start-node :concept-data])))
                                                      translation-graph (get-translation-graph graph)
                                                      root-node (get translation-graph (first (get-root-nodes translation-graph)))]
                                                  {:data          translation-graph
                                                   :root          root-node
                                                   :has-concepts? (graph-has-concepts? graph)})
                  :default (-> scene-data
                               (parse-scene (select-keys params [:start-node]))))]
     result)))
