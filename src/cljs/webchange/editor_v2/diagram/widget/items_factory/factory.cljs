(ns webchange.editor-v2.diagram.widget.items-factory.factory
  (:require
    [webchange.editor-v2.diagram.widget.items-factory.links-factory :refer [create-links]]
    [webchange.editor-v2.diagram.widget.items-factory.nodes-factory :refer [create-nodes]]))

(defn create-diagram-items
  [graph]
  (let [nodes (create-nodes graph)
        links (create-links graph nodes)]
    {:nodes nodes
     :links links}))
