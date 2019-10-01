(ns webchange.editor-v2.diagram-builder.custom-nodes.object-node.object-factory
  (:require
    [reagent.core :as r]
    ["@projectstorm/react-diagrams" :refer [AbstractNodeFactory]]
    [webchange.editor-v2.diagram-builder.custom-nodes.object-node.object-model :refer [get-object-model]]
    [webchange.editor-v2.diagram-builder.custom-nodes.object-node.object-widget :refer [object-widget]]))

(defn node-factory []
  (js/Reflect.construct AbstractNodeFactory #js ["object"] node-factory))

(set! (.. node-factory -prototype)
      (js/Object.create (.-prototype AbstractNodeFactory)))

(set! (.. node-factory -prototype -constructor)
      node-factory)

(set! (.. node-factory -prototype -generateReactWidget)
      (fn [_ node]
        (r/as-element [object-widget {:node node}])))

(set! (.. node-factory -prototype -getNewInstance)
      (fn []
        (get-object-model)))

(defn get-node-factory
  []
  (let [entity (node-factory.)]
    entity))
