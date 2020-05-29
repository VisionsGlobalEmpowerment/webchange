(ns webchange.editor-v2.graph-builder.filters.phrases
  (:require
    [webchange.editor-v2.graph-builder.utils.change-node :refer [remove-node]]
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
                                              (map (fn [handler]
                                                     {:handler handler
                                                      :name    "next"}))
                                              (set))]
              (assoc-in graph [node-name :connections] simplified-connections)))
          graph
          graph))

(defn get-phrases-graph
  [scene-graph]
  (-> scene-graph
      (remove-extra-nodes remove-node?)
      (remove-detached-nodes)
      (simplify-connections)))
