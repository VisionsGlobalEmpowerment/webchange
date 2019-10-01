(ns webchange.editor-v2.diagram-builder.diagram-nodes
  (:require
    [webchange.editor-v2.diagram-builder.diagram-nodes-utils :refer [connect-nodes
                                                                     get-action-node
                                                                     get-object-node
                                                                     set-node-position]]))

(defn get-objects-links
  [objects-data]
  (->> (seq objects-data)
       (reduce
         (fn [result [object-name object-data]]
           (concat result (map (fn [out] (merge out {:from object-name})) (:outs object-data))))
         [])))

(defn get-diagram-nodes
  [scene-data]
  (let [objects (->> (:objects scene-data)
                     (seq)
                     (reduce
                       (fn [result [object-name object-data]]
                         (assoc result object-name (get-object-node object-name object-data)))
                       {}))
        actions (->> (:actions scene-data)
                     (seq)
                     (reduce
                       (fn [result [action-name action-data]]
                         (assoc result action-name (get-action-node action-name action-data)))
                       {}))
        nodes (merge objects actions)
        objects-links (map (fn [{:keys [from event handler]}]
                             (let [node-from (get nodes from)
                                   node-to (get nodes handler)]

                               (connect-nodes node-from (name event) node-to "in")))
                           (get-objects-links (:objects scene-data)))
        actions-links (map (fn [{:keys [from event handler]}]
                             (let [node-from (get nodes from)
                                   node-to (get nodes handler)]

                               (connect-nodes node-from (name event) node-to "in")))
                           (get-objects-links (:actions scene-data)))
        ]
    {:nodes (vals nodes)
     :links (concat objects-links actions-links)}))
