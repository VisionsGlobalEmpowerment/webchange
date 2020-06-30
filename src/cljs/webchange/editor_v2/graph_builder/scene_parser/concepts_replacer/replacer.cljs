(ns webchange.editor-v2.graph-builder.scene-parser.concepts-replacer.replacer
  (:require
    [webchange.editor-v2.graph-builder.scene-parser.actions-parser.interface :refer [parse-actions-chain]]
    [webchange.editor-v2.graph-builder.graph-normalizer.utils :refer [get-copy-name]]
    [webchange.editor-v2.graph-builder.utils.change-node :refer [get-nodes-by-next-node-name
                                                                 change-parent-node-connections
                                                                 change-child-node-connections]]
    [webchange.editor-v2.graph-builder.utils.insert-sub-graph :refer [insert-sub-graph]]
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
    (if-not (nil? concept)
      (->> (:data concept)
           (some
             (fn [[name]]
               (= name concept-action-name)))
           (boolean))
      true)
    false))

(defn get-concept-action
  [action-name current-concept {:keys [copy-counter]}]
  (let [use-copy-name? (-> copy-counter nil? not)
        action-parsed-name (if use-copy-name?
                             (-> action-name (get-copy-name copy-counter) (keyword))
                             action-name)
        action-data (get-in current-concept [:data action-name])]
    (parse-actions-chain
      (assoc {} action-name action-data)
      {:action-name   action-parsed-name
       :action-data   (if use-copy-name?
                        (merge action-data
                               {:origin-name action-name})
                        action-data)
       :parent-action nil
       :next-action   :next-action
       :prev-action   :prev-action
       :path          [action-name]
       :sequence-path []})))

(defn insert-concept-nodes
  [graph prev-node-name node-name concept-nodes-data]
  (let [node-data (get graph node-name)
        node-children (get-children node-name node-data prev-node-name)
        start-node-connections (if (= 0 (count node-children))
                                 [{:previous prev-node-name}]
                                 node-children)
        updated-concept-nodes-data (reduce (fn [graph [node-name]]
                                             (update-in graph [node-name :data] merge {:concept-action true}))
                                           concept-nodes-data
                                           concept-nodes-data)]
    (insert-sub-graph graph node-name start-node-connections updated-concept-nodes-data)))

(defn used-connection?
  [used-map node-name]
  (contains? used-map node-name))

(defn add-to-used-map
  [used-map node-name]
  (assoc used-map node-name true))

(defn override-concepts-dfs
  ([graph current-concept concept-actions-copy-counter]
   (first (override-concepts-dfs graph current-concept [:root :root] {} concept-actions-copy-counter)))
  ([graph current-concept [prev-node-name node-name] used-map concept-actions-copy-counter]
   (let [node-data (get graph node-name)
         [graph
          concept-actions-copy-counter
          used-map] (reduce
                      (fn [[graph concept-actions-copy-counter used-map] {:keys [handler]}]
                        (if-not (used-connection? used-map handler)
                          (override-concepts-dfs graph current-concept [node-name handler] (add-to-used-map used-map handler) concept-actions-copy-counter)
                          [graph concept-actions-copy-counter used-map]))
                      [graph concept-actions-copy-counter used-map]
                      (get-children node-name node-data prev-node-name))]
     (let [action-data (:data node-data)]
       (if (concept-action-ref? action-data current-concept)
         (let [referenced-action-name (get-referenced-concept-action-name action-data)
               concept-nodes-data (get-concept-action referenced-action-name
                                                      current-concept
                                                      {:copy-counter (get concept-actions-copy-counter referenced-action-name)})]
           [(insert-concept-nodes graph prev-node-name node-name concept-nodes-data)
            (update concept-actions-copy-counter referenced-action-name inc)
            used-map])
         [graph concept-actions-copy-counter used-map])))))

(defn graph-has-concepts?
  [graph]
  (let [has-concept-ref? (->> graph
                              (some (fn [[_ {:keys [data]}]] (concept-action-ref? data nil)))
                              (boolean))
        has-concept-action? (->> graph
                                (some (fn [[_ {:keys [data]}]] (:concept-action data)))
                                (boolean))]
    (or has-concept-ref? has-concept-action?)))

(defn get-concept-actions-copy-counter
  [graph current-concept]
  (->> graph
       (reduce (fn [result [_ {:keys [data]}]]
                 (if (concept-action-ref? data current-concept)
                   (update result (get-referenced-concept-action-name data) inc)
                   result))
               {})
       (filter (fn [[_ value]]
                 (<= 2 value)))
       (map first)
       (reduce (fn [result name]
                 (assoc result name 0))
               {})))

(defn override-concept-actions
  [graph {:keys [current-concept]}]
  (if-not (nil? current-concept)
    (let [graph (->> graph
                     (get-root-nodes)
                     (add-root-node graph))
          concept-actions-copy-counter (get-concept-actions-copy-counter graph current-concept)]
      (-> graph
          (override-concepts-dfs current-concept concept-actions-copy-counter)
          (remove-root-node)))
    graph))
