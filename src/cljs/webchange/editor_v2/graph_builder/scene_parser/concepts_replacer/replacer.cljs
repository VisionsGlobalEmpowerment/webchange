(ns webchange.editor-v2.graph-builder.scene-parser.concepts-replacer.replacer
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.scene-parser-actions :refer [parse-actions-chain]]
    [webchange.editor-v2.graph-builder.graph-normalizer.utils :refer [get-copy-name]]
    [webchange.editor-v2.graph-builder.utils.change-node :refer [get-nodes-by-next-node-name
                                                                 change-parent-node-connections
                                                                 change-child-node-connections]]
    [webchange.editor-v2.graph-builder.utils.node-siblings :refer [get-node-ins
                                                                   get-node-outs]]
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [add-root-node
                                                                get-root-nodes
                                                                remove-root-node]]))

(defn get-referenced-concept-action-name
  [action-data]
  (let [from-var (get-in action-data [:from-var])
        action-name (get-in from-var [0 :var-property])
        like-concept-ref? (and (= "action" (get-in action-data [:type]))
                               (= 1 (count from-var))
                               (nil? (get-in from-var [0 :action-property]))
                               (not (nil? action-name)))]
    (when like-concept-ref?
      (keyword action-name))))

(defn concept-action-ref?
  [action-data concept]
  (if-let [concept-action-name (-> action-data
                                   (get-referenced-concept-action-name))]
    (->> (:data concept)
         (some
           (fn [[name]]
             (= name concept-action-name)))
         (boolean))
    false))

(defn get-concept-action
  [action-name current-concept {:keys [copy-counter]}]
  (let [use-copy-name? (-> copy-counter nil? not)
        action-parsed-name (if use-copy-name?
                             (-> action-name (get-copy-name copy-counter) (keyword))
                             action-name)
        action-data (get-in current-concept [:data action-name])]
    [action-parsed-name (parse-actions-chain
                          (assoc {} action-name action-data)
                          {:action-name   action-parsed-name
                           :action-data   (if use-copy-name?
                                            (merge action-data
                                                   {:origin-name action-name})
                                            action-data)
                           :parent-action nil
                           :next-action   :end-action
                           :prev-action   :start-action
                           :sequence-path []})]))

(defn update-prev-nodes
  [graph replacing-node-name new-nodes-graph]
  (let [replacing-node-ins (-> (get-node-ins graph replacing-node-name) keys)]
    (reduce
      (fn [graph node-in]
        (reduce
          (fn [graph first-node-name]
            (change-parent-node-connections graph node-in replacing-node-name [first-node-name]))
          graph
          (get-root-nodes new-nodes-graph)))
      graph
      replacing-node-ins)))

(defn update-next-nodes
  [graph replacing-node-name new-nodes-graph]
  (let [replacing-node-data (get graph replacing-node-name)
        replacing-node-outs (-> replacing-node-data get-node-outs keys)]
    (reduce
      (fn [graph node-out]
        (let [last-node-names (get-nodes-by-next-node-name new-nodes-graph node-out)]
          (change-child-node-connections graph node-out replacing-node-name last-node-names)))
      graph
      replacing-node-outs)))

(defn update-new-nodes
  [new-nodes prev-node-name]
  (reduce
    (fn [result [node-name node-data]]
      (assoc result node-name (-> node-data
                                  (assoc-in [:data :concept-action] true)
                                  (assoc-in [:connections] (->> (:connections node-data)
                                                                (map (fn [{:keys [previous] :as connection}]
                                                                       (if (= :root previous)
                                                                         (assoc connection :previous prev-node-name)
                                                                         connection)))
                                                                (set))))))
    {}
    new-nodes))

(defn insert-concept-nodes
  [graph prev-node-name node-name concept-first-node-name concept-nodes-data]
  (let [node-data (get graph node-name)
        node-children (get-children node-name node-data prev-node-name)]
    (println ">> insert-concept-nodes" node-name)
    (println "concept-first-node-name" concept-first-node-name)
    (println "node-children" node-children)
    (println "concept-nodes-data" concept-nodes-data)
    graph)

  ;(-> graph
  ;    (update-prev-nodes node-name new-nodes)
  ;    (update-next-nodes node-name new-nodes)
  ;    (dissoc node-name)
  ;    (merge (update-new-nodes new-nodes prev-node-name)))
  )

(defn override-concepts-dfs
  ([graph current-concept]
   (override-concepts-dfs graph current-concept [:root :root]))
  ([graph current-concept [prev-node-name node-name]]
   (let [node-data (get graph node-name)
         graph (reduce
                 (fn [graph {:keys [handler]}]
                   (override-concepts-dfs graph current-concept [node-name handler]))
                 graph
                 (get-children node-name node-data prev-node-name))]
     (let [action-data (:data node-data)]
       (if (concept-action-ref? action-data current-concept)
         (let [[concept-first-node-name
                concept-nodes-data] (-> action-data
                                        (get-referenced-concept-action-name)
                                        (get-concept-action current-concept
                                                            {:copy-counter (get-in node-data [:copy-counter])}))]
           (insert-concept-nodes graph prev-node-name node-name concept-first-node-name concept-nodes-data))
         graph)))))

(defn override-concept-actions
  [graph {:keys [current-concept]}]
  (if-not (nil? current-concept)
    (let [graph (->> graph
                     (get-root-nodes)
                     (add-root-node graph))]
      (-> graph
          (override-concepts-dfs current-concept)
          (remove-root-node)))
    graph))
