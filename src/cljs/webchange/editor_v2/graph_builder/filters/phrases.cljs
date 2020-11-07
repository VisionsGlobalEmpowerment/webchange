(ns webchange.editor-v2.graph-builder.filters.phrases
  (:require
    [webchange.editor-v2.graph-builder.utils.node-data :refer [phrase-node?]]))

(defn- remove-detached-nodes
  [graph]
  (reduce (fn [graph [node-name node-data]]
            (if (and (not (= (:entity node-data) :action))
                     (empty? (:connections node-data)))
              (dissoc graph node-name)
              graph))
          graph
          graph))

(defn- remove-node?
  [node-data]
  (and (= (:entity node-data) :action)
       (not (phrase-node? node-data))))

(defn- remove-node
  [graph removed-node-name]
  (let [removed-node-connections (get-in graph [removed-node-name :connections])
        parent-nodes (->> graph
                          (filter (fn [[_ node-data]]
                             (contains? (:connections node-data) removed-node-name)))
                          (map first))]
    (reduce (fn [graph parent-node-name]
              (let [parent-connections (get-in graph [parent-node-name :connections])]
                (if (contains? graph parent-node-name)
                  (assoc-in graph [parent-node-name :connections] (-> parent-connections
                                                                    (disj removed-node-name)
                                                                    (concat removed-node-connections)
                                                                    (set)))
                  graph)))
            (dissoc graph removed-node-name)
            parent-nodes)))

(defn remove-extra-nodes
  [graph remove?]
  (reduce (fn [graph [node-name node-data]]
            (if (remove? node-data)
              (remove-node graph node-name)
              graph))
          graph
          graph))

(defn- simplify-connections
  "Remove duplicated connection with the same handler but different previous nodes.
   Keep only handler. Name hardcoded with 'next' for diagram builder compatibility."
  [graph]
  (reduce (fn [graph [node-name node-data]]
            (let [simplified-connections (->> (:connections node-data)
                                              (map :handler)
                                              (distinct)
                                              (remove nil?) ;; ToDo: Remove when 'remove-extra-nodes' doesn't leave no-handler connections. Case: 'Swings' scene, ':box3' node
                                              (set))]
              (assoc-in graph [node-name :connections] simplified-connections)))
          graph
          graph))

(defn- add-connection-names
  [graph]
  (reduce (fn [graph [node-name node-data]]
            (let [named-connections (->> (:connections node-data)
                                         (filter (fn [handler]
                                                   (and (contains? graph handler)
                                                        (not (= handler node-name)))))
                                         (map (fn [handler]
                                                {:handler handler
                                                 :name    "next"}))
                                         (set))]
              (assoc-in graph [node-name :connections] named-connections)))
          graph
          graph))

(defn get-phrases-graph
  [scene-graph]
  (-> scene-graph
      (simplify-connections)
      (remove-extra-nodes remove-node?)
      (remove-detached-nodes)
      (add-connection-names)))
