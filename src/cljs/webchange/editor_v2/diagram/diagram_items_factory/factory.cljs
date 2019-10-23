(ns webchange.editor-v2.diagram.diagram-items-factory.factory
  (:require
    [webchange.editor-v2.diagram.diagram-items-factory.links-factory :refer [create-links]]
    [webchange.editor-v2.diagram.diagram-items-factory.nodes-factory :refer [create-nodes]]))

(defn create-diagram-items
  [scene-data]
  (let [nodes (create-nodes scene-data)
        links (create-links scene-data nodes)]
    [(vals nodes) links]))
