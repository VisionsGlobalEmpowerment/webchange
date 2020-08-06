(ns webchange.editor-v2.diagram.diagram-model.init
  (:require
    ["@projectstorm/react-diagrams" :refer [DiagramModel DiagramEngine]]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-factory :refer [get-custom-factory]]
    [webchange.editor-v2.diagram.diagram-utils.utils :refer [reorder zoom-to-fit]]))

(defn- init-engine
  [diagram-mode]
  (let [engine (DiagramEngine.)]
    (.installDefaultFactories engine)
    (.registerNodeFactory engine (get-custom-factory diagram-mode))
    engine))

(defn- init-model
  [engine nodes links {:keys [locked?]}]
  (let [model (DiagramModel.)]
    (.setDiagramModel engine model)
    (doseq [node nodes] (.addNode model node))
    (doseq [link links] (.addLink model link))
    (when locked? (.setLocked model true))
    {:engine engine
     :model  model}))

(defn init-diagram-model
  ([diagram-mode nodes links]
   (init-diagram-model diagram-mode nodes links {}))
  ([diagram-mode nodes links options]
  (-> (init-engine diagram-mode)
      (init-model nodes links options))))
