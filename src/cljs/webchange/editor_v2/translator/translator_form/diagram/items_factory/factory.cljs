(ns webchange.editor-v2.translator.translator-form.diagram.items-factory.factory
  (:require
    [webchange.editor-v2.translator.translator-form.diagram.items-factory.links-factory :refer [create-links]]
    [webchange.editor-v2.translator.translator-form.diagram.items-factory.nodes-factory :refer [create-nodes]]))

(defn create-diagram-items
  [graph]
  (let [nodes (create-nodes graph)
        links (create-links graph nodes)]
    {:nodes nodes
     :links links}))
