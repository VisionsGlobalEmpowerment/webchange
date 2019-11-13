(ns webchange.editor-v2.diagram.diagram-model.init
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramModel DiagramEngine]]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-factory :refer [get-custom-factory]]
    [webchange.editor-v2.diagram.diagram-model.items-factory.factory :refer [create-diagram-items]]
    [webchange.editor-v2.diagram.diagram-utils.utils :refer [reorder zoom-to-fit]]))

(defn init-engine
  [diagram-mode]
  (let [engine (DiagramEngine.)]
    (.installDefaultFactories engine)
    (.registerNodeFactory engine (get-custom-factory diagram-mode))
    engine))

(defn get-engine-model
  [engine]
  (.getDiagramModel engine))

(defn init-model
  [engine graph]
  (let [model (DiagramModel.)
        [nodes links] (create-diagram-items graph)]
    (.setDiagramModel engine model)
    (doseq [node nodes] (.addNode model node))
    (doseq [link links] (.addLink model link))
    engine))

(defn init-diagram-model
  [graph diagram-mode]
  (let [engine (-> (init-engine diagram-mode)
                   (init-model graph))]
    engine))
