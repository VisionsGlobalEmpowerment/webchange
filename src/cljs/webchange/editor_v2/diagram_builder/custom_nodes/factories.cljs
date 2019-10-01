(ns webchange.editor-v2.diagram-builder.custom-nodes.factories
  (:require
    [webchange.editor-v2.diagram-builder.custom-nodes.action-node.action-factory :as action]
    [webchange.editor-v2.diagram-builder.custom-nodes.object-node.object-factory :as object]))

(def factory-getters [action/get-node-factory
                      object/get-node-factory])

(defn register-factories
  [engine]
  )
