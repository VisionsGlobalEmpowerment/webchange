(ns webchange.editor-v2.view-diagram
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramModel DiagramEngine DiagramWidget]]
    [webchange.editor-v2.diagram-builder.index :refer [get-diagram-data]]
    [webchange.editor-v2.scene-parser :refer [get-scene-data]]
    [webchange.editor-v2.diagram-builder.custom-nodes.factories :refer [factory-getters]]))

(defn init-engine
  [factory-getters]
  (let [engine (DiagramEngine.)
        model (DiagramModel.)]
    (.installDefaultFactories engine)
    (doseq [get-node-factory factory-getters]
      (.registerNodeFactory engine (get-node-factory)))
    (.setDiagramModel engine model)
    [engine model]))

(defn diagram
  [id]
  (let [[engine model] (init-engine factory-getters)
        {:keys [nodes links]} (->> id get-scene-data get-diagram-data)]
    (doseq [node nodes] (.addNode model node))
    (doseq [link links] (.addLink model link))
    [:> DiagramWidget {"diagramEngine" engine}]))
