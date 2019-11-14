(ns webchange.editor-v2.diagram.graph-builder.scene-parser.concepts-replacer.replacer
  (:require
    [webchange.editor-v2.diagram.graph-builder.utils.change-node :refer [rename-node]]
    [webchange.editor-v2.diagram.graph-builder.utils.node-children :refer [get-children]]
    [webchange.editor-v2.diagram.graph-builder.utils.root-nodes :refer [add-root-node
                                                                        get-root-nodes
                                                                        remove-root-node]]))

(defn get-concept-action
  [node-data concepts]
  (let [action-type (get-in node-data [:data :type])
        from-var (get-in node-data [:data :from-var])
        action-property (get-in from-var [0 :action-property])
        var-property (get-in from-var [0 :var-property])
        concept-ref? (and (= "action" action-type)
                          (= 1 (count from-var))
                          (nil? action-property)
                          (not (nil? var-property)))]
    (when concept-ref?
      (some (fn [{:keys [name type] :as concept}]
              (and (and (= name var-property)
                        (= type "action"))
                   (merge concept
                          {:name var-property}))) concepts))))

(defn replace-node
  [graph old-name new-name new-data]
  (let [merged-data (-> (get graph old-name)
                        (assoc :type (:type new-data))
                        (update-in [:data] merge new-data))]
    (-> graph
        (rename-node old-name new-name)
        (assoc new-name merged-data))))

(defn override-concepts-dfs
  "Counts how much phrase nodes contains current node's subtree and it's children"
  ([graph concepts]
   (override-concepts-dfs graph concepts [:root :root]))
  ([graph concepts [prev-node-name node-name]]
   (let [node-data (get graph node-name)
         graph (reduce
                 (fn [graph next-node-name]
                   (override-concepts-dfs graph concepts [node-name next-node-name]))
                 graph
                 (get-children node-data prev-node-name))]
     (let [concept-action (get-concept-action node-data concepts)]
       (if-not (nil? concept-action)
         (replace-node graph
                       node-name
                       (-> concept-action :name keyword)
                       (-> concept-action :template (merge {:concept-action :true})))
         graph)))))

(defn override-concept-actions
  [graph concepts]
  (if-not (nil? concepts)
    (let [graph (->> graph
                     (get-root-nodes)
                     (add-root-node graph))]
      (-> graph
          (override-concepts-dfs concepts)
          (remove-root-node)))
    graph))
