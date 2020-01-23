(ns webchange.editor-v2.graph-builder.graph-normalizer.open-cycles
  (:require
    [webchange.editor-v2.graph-builder.utils.change-node :refer [remove-connection]]
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]))

(defn get-cycles-dfs
  [graph [prev-node-name node-name] cycles seq-path used-map]
  (let [node-data (get graph node-name)
        children (get-children node-name node-data prev-node-name seq-path)]
    (reduce
      (fn [[graph cycles used-map] {:keys [handler sequence] :as connection}]
        (let [new-seq-path (if-not (= sequence (last seq-path)) (conj seq-path sequence) seq-path)
              cycled? (boolean (some #{handler} new-seq-path))]
          (if cycled?
            [graph (conj cycles {:node       node-name
                                 :connection connection})]
            (get-cycles-dfs graph [node-name handler] cycles new-seq-path used-map))))
      [graph cycles (assoc-in used-map [prev-node-name node-name (last seq-path)] true)]
      children)))

(defn get-cycles
  [graph]
  (-> graph
      (get-cycles-dfs [:root :root] [] [] {})
      (second)))

(defn fix-cycles
  [graph cycles]
  (reduce
    (fn [graph {:keys [node connection]}]
      (-> graph
          (assoc-in [node :connections] (->> (get-in graph [node :connections])
                                             (map (fn [node-connection]
                                                    (if (= node-connection connection)
                                                      (-> node-connection
                                                          (assoc :cycle-to (:handler node-connection))
                                                          (dissoc :handler))
                                                      node-connection)))
                                             (set)))
          (remove-connection (:handler connection) {:previous node})))
    graph
    cycles))

(defn open-cycles
  [graph]
  (->> graph
       (get-cycles)
       (fix-cycles graph)))
