(ns webchange.editor-v2.graph-builder.utils.insert-sub-graph
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.utils.create-graph-node :refer [create-graph-node-connection]]))

(defn find-nodes-by-connection
  [graph connection-field connection-value]
  (->> graph
       (filter
         (fn [[_ {:keys [connections]}]]
           (some
             (fn [connection]
               (= connection-value (get connection connection-field)))
             connections)))
       (map first)))

(defn update-start-node-connections
  [graph start-node-name start-node-connections sub-graph-first-nodes]
  (let [connections (get-in graph [start-node-name :connections])
        new-connections (if (= 0 (count connections))
                          (->> start-node-connections
                               (map (fn [{:keys [previous]}]
                                      (map (fn [sub-graph-first-node]
                                             (create-graph-node-connection {:previous previous
                                                                            :handler  sub-graph-first-node}))
                                           sub-graph-first-nodes)))
                               (flatten)
                               (set))
                          (->> connections
                               (map (fn [connection]
                                      (if (some #{connection} start-node-connections)
                                        (map (fn [sub-graph-first-node]
                                               (assoc connection :handler sub-graph-first-node))
                                             sub-graph-first-nodes)
                                        connection)))
                               (flatten)
                               (set)))]
    (assoc-in graph [start-node-name :connections] new-connections)))

(defn update-start-node-children
  [graph start-node-name start-node-connections sub-graph-last-nodes]
  (->> (map :handler start-node-connections)
       (reduce (fn [graph node-name]
                 (if-not (nil? node-name)
                   (assoc-in graph
                             [node-name :connections]
                             (->> (get-in graph [node-name :connections])
                                  (map (fn [{:keys [previous] :as connection}]
                                         (if (= previous start-node-name)
                                           (map (fn [sub-graph-last-node]
                                                  (assoc connection :previous sub-graph-last-node))
                                                sub-graph-last-nodes)
                                           connection)))
                                  (flatten)
                                  (set)))
                   graph))
               graph)))

(defn update-previous-actions
  [graph nodes-to-update sub-graph-prev-action new-previous-node]
  (reduce (fn [graph node-to-update]
            (assoc-in graph
                      [node-to-update :connections]
                      (->> (get-in graph [node-to-update :connections])
                           (map (fn [{:keys [previous] :as connection}]
                                  (if (= previous sub-graph-prev-action)
                                    (assoc connection :previous new-previous-node)
                                    connection)))
                           (set))))
          graph
          nodes-to-update))

(defn update-next-actions
  [graph nodes-to-update sub-graph-next-action new-last-nodes]
  (reduce (fn [graph node-to-update]
            (assoc-in graph
                      [node-to-update :connections]
                      (if-not (nil? new-last-nodes)
                        (->> (get-in graph [node-to-update :connections])
                             (map (fn [{:keys [handler] :as connection}]
                                    (if (= handler sub-graph-next-action)
                                      (map (fn [new-last-node]
                                             (assoc connection :handler new-last-node))
                                           new-last-nodes)
                                      connection)))
                             (flatten)
                             (set))
                        #{})))
          graph
          nodes-to-update))

(defn insert-sub-graph
  ([graph start-node-name start-node-connections sub-graph]
   (insert-sub-graph graph start-node-name start-node-connections sub-graph :prev-action :next-action))
  ([graph start-node-name start-node-connections sub-graph sub-graph-prev-action sub-graph-next-action]
   (let [sub-graph-first-nodes (find-nodes-by-connection sub-graph :previous sub-graph-prev-action)
         sub-graph-last-nodes (find-nodes-by-connection sub-graph :handler sub-graph-next-action)
         start-node-children (->> start-node-connections
                                  (map :handler)
                                  (filter (complement nil?)))]
     (-> graph
         (update-start-node-connections start-node-name start-node-connections sub-graph-first-nodes)
         (update-start-node-children start-node-name start-node-connections sub-graph-last-nodes)
         (merge (-> sub-graph
                    (update-previous-actions sub-graph-first-nodes sub-graph-prev-action start-node-name)
                    (update-next-actions sub-graph-last-nodes sub-graph-next-action start-node-children)))))))
