(ns webchange.editor-v2.diagram.diagram-builder
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramModel DiagramEngine]]
    [webchange.editor-v2.diagram.diagram-items-arranger :refer [arrange-items]]
    [webchange.editor-v2.diagram.diagram-items-factory.factory :refer [create-diagram-items]]
    [webchange.editor-v2.diagram.scene-parser.parser :refer [parse-scene]]
    [webchange.editor-v2.diagram.custom-diagram-items.custom-factory :refer [get-custom-factory]]
    [webchange.editor-v2.diagram.scene-parser.filters.phrases :refer [get-phrases-graph]]
    ))

(defonce state (atom {:model  nil
                      :engine nil}))

(defn- init-engine
  [diagram-mode]
  (let [engine (DiagramEngine.)
        model (DiagramModel.)]
    (.installDefaultFactories engine)
    (.registerNodeFactory engine (get-custom-factory diagram-mode))
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

(defn get-diagram-graph
  [scene-data diagram-mode]
  (let [scene-graph (parse-scene scene-data)]
    (cond
      (= diagram-mode :translation) (get-phrases-graph scene-graph)
      :default scene-graph)))

(defn get-diagram-engine
  [scene-data diagram-mode]
  (let [[engine model] (init-engine diagram-mode)
        [nodes links] (-> scene-data
                           (get-diagram-graph diagram-mode)
                           (create-diagram-items))]
    (doseq [node nodes] (.addNode model node))
    (doseq [link links] (.addLink model link))
    (reset! state {:model  model
                   :engine engine})
    (js/setTimeout #(do (reorder-diagram)
                        (zoom-to-fit)) 1000)
    engine))
