(ns webchange.editor-v2.translator.translator-form.dialog.utils
  (:require
    [webchange.editor-v2.graph-builder.utils.node-children :refer [get-children]]
    [webchange.editor-v2.graph-builder.utils.root-nodes :refer [add-root-node
                                                                get-root-nodes
                                                                remove-root-node]]
    [webchange.editor-v2.translator.translator-form.utils :refer [node-data->phrase-data]]))

(defn- get-dialog-data-dfs
  ([graph]
   (first (get-dialog-data-dfs graph [:root :root] {} [])))
  ([graph [prev-node-name node-name] used-map result]
   (let [node-data (get graph node-name)]
     (reduce
       (fn [[result used-map] {:keys [handler]}]
         (if-not (contains? used-map handler)
           (get-dialog-data-dfs graph [node-name handler] (assoc used-map handler true) result)
           [result used-map]))
       (let [phrase-data (-> node-data node-data->phrase-data)]
         [(if-not (= node-name :root)
            (conj result phrase-data)
            result)
          used-map])
       (get-children node-name node-data prev-node-name)))))

(defn get-dialog-data
  [phrase-node graph]
  (let [phrase-node? (-> (:data phrase-node)
                         (contains? :phrase-text))]
    (if phrase-node?
      [(-> phrase-node node-data->phrase-data)]
      (->> graph
           (get-root-nodes)
           (add-root-node graph)
           (get-dialog-data-dfs)))))