(ns webchange.editor-v2.diagram.diagram-builder
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramModel DiagramEngine]]
    [webchange.editor-v2.diagram.diagram-items-arranger :refer [arrange-items]]
    [webchange.editor-v2.diagram.diagram-items-factory.factory :refer [create-diagram-items]]
    [webchange.editor-v2.diagram.scene-parser.parser :refer [parse-scene]]
    [webchange.editor-v2.diagram.custom-diagram-items.custom-factory :refer [get-custom-factory]]))

(defonce state (atom {:model  nil
                      :engine nil}))

(defn- init-engine
  []
  (let [engine (DiagramEngine.)
        model (DiagramModel.)]
    (.installDefaultFactories engine)
    (.registerNodeFactory engine (get-custom-factory))
    (.setDiagramModel engine model)
    [engine model]))

(defn reorder-diagram
  []
  (let [model (:model @state)
        engine (:engine @state)]
    (arrange-items model engine)
    (.repaintCanvas engine)))

(defn zoom-to-fit
  []
  (let [engine (:engine @state)]
    (.zoomToFit engine)))

(defn get-diagram-engine
  [scene-data]
  (let [[engine model] (init-engine)
        [nodes links] (->> scene-data
                           (parse-scene)
                           (create-diagram-items))]
    (doseq [node nodes] (.addNode model node))
    (doseq [link links] (.addLink model link))
    (reset! state {:model  model
                   :engine engine})
    (js/setTimeout #(do (reorder-diagram)
                        (zoom-to-fit)) 1000)
    engine))
