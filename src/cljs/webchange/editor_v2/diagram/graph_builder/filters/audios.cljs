(ns webchange.editor-v2.diagram.graph-builder.filters.audios
  (:require
    [webchange.editor-v2.diagram.graph-builder.utils.node-children :refer [get-children]]
    [webchange.editor-v2.diagram.graph-builder.utils.root-nodes :refer [add-root-node
                                                                        get-root-nodes
                                                                        remove-root-node]]))

(defn get-audio-data
  [node-data]
  (when (some #{(:type node-data)} ["audio"
                                    "animation-sequence"])
    (merge (select-keys node-data [:start :duration])
           {:key (or (get node-data :audio)
                     (get node-data :id))})))

(defn get-audios-dfs
  ([graph]
   (get-audios-dfs graph [:root :root] []))
  ([graph [prev-node-name node-name] audios]
   (let [node-data (get graph node-name)
         audio (get-audio-data (:data node-data))]
     (reduce
       (fn [audios next-node-name]
         (get-audios-dfs graph [node-name next-node-name] audios))
       (if-not (nil? audio)
         (conj audios audio)
         audios)
       (get-children node-data prev-node-name)))))

(defn get-audios
  [scene-graph]
  (let [graph (->> scene-graph
                   (get-root-nodes)
                   (add-root-node scene-graph))]
    (-> graph
        (get-audios-dfs)
        (distinct))))
