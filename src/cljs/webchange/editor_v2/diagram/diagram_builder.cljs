(ns webchange.editor-v2.diagram.diagram-builder
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramModel DiagramEngine]]
    [webchange.editor-v2.diagram.diagram-items-arranger :refer [arrange-items]]
    [webchange.editor-v2.diagram.diagram-items-creator :refer [create-diagram-items]]
    [webchange.editor-v2.diagram.scene-data-parser.parser :refer [parse-scene-data]]
    [webchange.editor-v2.diagram.custom-diagram-items.custom-factory :refer [get-custom-factory]]))

(defn init-engine
  []
  (let [engine (DiagramEngine.)
        model (DiagramModel.)]
    (.installDefaultFactories engine)
    (.registerNodeFactory engine (get-custom-factory))
    (.setDiagramModel engine model)
    [engine model]))

(defn get-diagram-engine
  [scene-data]
  (let [[engine model] (init-engine)
        [nodes links] (->> scene-data
                           (parse-scene-data)
                           (create-diagram-items)
                           (arrange-items))]
    (doseq [node nodes] (.addNode model node))
    (doseq [link links] (.addLink model link))
    engine))
