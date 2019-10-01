(ns webchange.editor-v2.diagram-builder.custom-nodes.action-node.action-factory
  (:require
    [reagent.core :as r]
    ["@projectstorm/react-diagrams" :refer [AbstractNodeFactory]]
    [webchange.editor-v2.diagram-builder.custom-nodes.action-node.action-model :refer [get-action-model]]
    [webchange.editor-v2.diagram-builder.custom-nodes.action-node.action-widget :refer [action-widget]]))

(defn node-factory []
  (js/Reflect.construct AbstractNodeFactory #js ["action"] node-factory))

(set! (.. node-factory -prototype)
      (js/Object.create (.-prototype AbstractNodeFactory)))

(set! (.. node-factory -prototype -constructor)
      node-factory)

(set! (.. node-factory -prototype -generateReactWidget)
      (fn [_ node]
        (r/as-element [action-widget {:node node}])))

(set! (.. node-factory -prototype -getNewInstance)
      (fn []
        (get-action-model)))

(defn get-node-factory
  []
  (let [entity (node-factory.)]
    entity))
