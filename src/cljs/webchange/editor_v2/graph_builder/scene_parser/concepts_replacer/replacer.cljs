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
  [action-data concept-scheme]
  (if-let [concept-action-name (-> action-data
                                   (get-referenced-concept-action-name))]
    (->> concept-scheme
         (some
           (fn [{:keys [name type]}]
             (and (= name (clojure.core/name concept-action-name))
                  (= type "action"))))
         (boolean))
    false))

(defn get-concept-scheme-action
  [action-name concept-scheme]
  (let [scheme-action-name (clojure.core/name action-name)]
    (some
      (fn [{:keys [name type] :as concept}]
        (and (and (= name scheme-action-name)
                  (= type "action"))
             (:template concept)))
      concept-scheme)))

(defn get-concept-instance-action
  [action-name current-concept]
  (get-in current-concept [:data action-name]))

(defn get-concept-action
  [action-name prev-action next-actions concept-scheme current-concept copy-data]
  (let [action-data (if (nil? current-concept)
                      (get-concept-scheme-action action-name concept-scheme)
                      (get-concept-instance-action action-name current-concept))
        use-copy-name? (-> (:origin copy-data) nil? not)]
    (parse-actions-chain
      (assoc {} action-name action-data)
      {:action-name   (if use-copy-name?
                        (-> action-name
                            (get-copy-name (:counter copy-data))
                            (keyword))
                        action-name)
       :action-data   (if use-copy-name?
                        (merge action-data
                               {:origin action-name})
                        action-data)
       :parent-action nil
       :next-action   next-actions
       :prev-action   prev-action
       :sequence-path []})))

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

(defn replace-node
  [graph node-name new-nodes prev-node-name]
  (-> graph
      (update-prev-nodes node-name new-nodes)
      (update-next-nodes node-name new-nodes)
      (dissoc node-name)
      (merge (update-new-nodes new-nodes prev-node-name))))

;; ToDo probably concept actions should be placed after action referring to them instead of replace it.
(defn override-concepts-dfs
  ([graph concept-data]
   (override-concepts-dfs graph concept-data [:root :root]))
  ([graph {:keys [concept-scheme current-concept] :as concept-data} [prev-node-name node-name]]
   (let [node-data (get graph node-name)
         graph (reduce
                 (fn [graph next-node-name]
                   (override-concepts-dfs graph concept-data [node-name next-node-name]))
                 graph
                 (map :handler (get-children node-name node-data prev-node-name)))]
     (let [action-data (:data node-data)]
       (if (concept-action-ref? action-data concept-scheme)
         (let [new-nodes (-> action-data
                             (get-referenced-concept-action-name)
                             (get-concept-action nil
                                                 (map :handler (get-children node-name node-data prev-node-name))
                                                 concept-scheme
                                                 current-concept
                                                 {:origin  (get-in node-data [:origin])
                                                  :counter (get-in node-data [:copy-counter])}))]
           (replace-node graph node-name new-nodes prev-node-name))
         graph)))))

(defn override-concept-actions
  [graph concept-data]
  (if-not (nil? concept-data)
    (let [graph (->> graph
                     (get-root-nodes)
                     (add-root-node graph))]
      (-> graph
          (override-concepts-dfs concept-data)
          (remove-root-node)))
    graph))
